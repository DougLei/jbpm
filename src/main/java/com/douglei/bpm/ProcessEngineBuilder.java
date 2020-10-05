package com.douglei.bpm;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.douglei.bpm.bean.BeanFactory;
import com.douglei.orm.Configuration;
import com.douglei.orm.ExternalDataSource;
import com.douglei.orm.context.IdDuplicateException;
import com.douglei.orm.context.RegistrationResult;
import com.douglei.orm.context.SessionFactoryContainer;
import com.douglei.orm.mapping.handler.MappingHandlerException;
import com.douglei.orm.mapping.handler.entity.AddOrCoverMappingEntity;
import com.douglei.orm.mapping.handler.entity.MappingEntity;
import com.douglei.orm.mapping.handler.entity.ParseMappingException;
import com.douglei.orm.mapping.type.MappingTypeHandler;
import com.douglei.orm.sessionfactory.SessionFactory;
import com.douglei.tools.instances.resource.scanner.impl.ResourceScanner;

/**
 * 流程引擎构建器
 * @author DougLei
 */
public class ProcessEngineBuilder {
	private static final String DEFAULT_CONFIGURATION_FILE_PATH = "jbpm.conf.xml"; // 默认的配置文件路径
	private static final String MAPPING_FILE_ROOT_PATH = "jbpm-mappings"; // 工作流引擎相关的mapping文件根路径
	private BeanFactory beanFactory; // 引擎bean工厂
	
	public ProcessEngineBuilder() {
		this.beanFactory = BeanFactory.getSingleton();
	}
	
	/**
	 * - 关于以下四种build功能的介绍
	 * 
	 * -------------------------------------------------------------------------------------------------
	 * jbpm流程引擎单独作为服务, 有以下两种方式构建:
	 * 
	 * 1. 使用默认的jdb-orm配置文件(即jbpm.conf.xml), 构建引擎, 方法为: {@link ProcessEngineBuilder.build()}
	 * 2. 使用指定name的jdb-orm配置文件, 构建引擎, 方法为: {@link ProcessEngineBuilder.build(String)}
	 *    - 在流程引擎被销毁时, 会同时销毁SessionFactory实例, 因为该SessionFactory是完全属于流程引擎的, 同时将SessionFactory从 {@link SessionFactoryContainer} 中移除
	 * 
	 * -------------------------------------------------------------------------------------------------
	 * jbpm流程引擎属于扩展功能, 需要集成到其他系统中使用, 有以下两种方式构建: 
	 * 
	 * 1. 使用了其他 orm框架的系统, 使用 {@link ProcessEngineBuilder.build(String, DataSource)} 方法构建引擎, 传入引擎的唯一标识, 以及自己的数据源实例
	 *    - 在流程引擎被销毁时, 会同时销毁SessionFactory实例, 因为该SessionFactory是完全属于流程引擎的, 同时将SessionFactory从 {@link SessionFactoryContainer} 中移除
	 *    
	 * 2. 使用了jdb-orm框架的系统, 使用 {@link ProcessEngineBuilder.build(SessionFactory)} 方法构建引擎, 传入自己的SessionFactory实例(externalSessionFactory)
	 *    - 在流程引擎被销毁时, 不会同时销毁SessionFactory实例, 流程引擎具体销毁的逻辑如下: 
	 *    - 如果externalSessionFactory之前在 {@link SessionFactoryContainer} 中已经注册过, 则只会将流程引擎相关的mapping从externalSessionFactory中移除
	 *    - 反之会将流程引擎相关的mapping从externalSessionFactory中移除, 并将externalSessionFactory从 {@link SessionFactoryContainer} 中移除
	 *    
	 * -------------------------------------------------------------------------------------------------
	 * 特别说明, 以上四种build方式构建出的流程引擎, 在销毁的时候, 都不会对流程相关的表结构和其中的数据产生影响
	 */
	
	
	/**
	 * 使用默认的jdb-orm配置文件, 构建引擎
	 * @return 
	 * @throws IdDuplicateException 
	 */
	public ProcessEngine build() throws IdDuplicateException {
		return build(DEFAULT_CONFIGURATION_FILE_PATH);
	}
	
	/**
	 * 根据指定name的jdb-orm配置文件, 构建引擎
	 * @param configurationFilePath 配置文件路径
	 * @return 
	 * @throws IdDuplicateException 
	 */
	public ProcessEngine build(String configurationFilePath) throws IdDuplicateException {
		Configuration configuration = new Configuration(configurationFilePath);
		
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		registerSessionFactory(sessionFactory);
		return beanFactory.initEngineAttributes(new ProcessEngineWithBuiltinSessionFactory(sessionFactory.getId()));
	}
	
	/**
	 * 通过外部的数据源, 构建引擎
	 * @param engineId 引擎的唯一标识, 在多数据源情况中必须传入; 当只有一个数据源时, 该参数可传入null, 使用默认值defaultProcessEngine
	 * @param dataSource 外部的数据源实例
	 * @return 
	 * @throws IdDuplicateException
	 */
	public ProcessEngine build(String engineId, DataSource dataSource) throws IdDuplicateException {
		Configuration configuration = new Configuration(DEFAULT_CONFIGURATION_FILE_PATH);
		configuration.setId(engineId);
		configuration.setExternalDataSource(new ExternalDataSource(dataSource));
		
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		registerSessionFactory(sessionFactory);
		return beanFactory.initEngineAttributes(new ProcessEngineWithBuiltinSessionFactory(sessionFactory.getId()));
	}
	
	/**
	 * 使用外部的 {@link SessionFactory}实例, 构建引擎
	 * @param externalSessionFactory 外部的 {@link SessionFactory} 实例
	 * @return 
	 * @throws ParseMappingException
	 * @throws MappingHandlerException
	 * @throws IdDuplicateException 
	 */
	public ProcessEngine build(SessionFactory externalSessionFactory) throws ParseMappingException, MappingHandlerException, IdDuplicateException{
		List<String> mappingFiles = new ResourceScanner(MappingTypeHandler.getFileSuffixes().toArray(new String[MappingTypeHandler.getFileSuffixes().size()])).scan(MAPPING_FILE_ROOT_PATH);
		List<MappingEntity> mappingEntities = new ArrayList<MappingEntity>(mappingFiles.size());
		for (String mappingFile : mappingFiles) 
			mappingEntities.add(new AddOrCoverMappingEntity(mappingFile));
		externalSessionFactory.getMappingHandler().execute(mappingEntities);
		
		RegistrationResult result = registerSessionFactory(externalSessionFactory);
		return beanFactory.initEngineAttributes(new ProcessEngineOfExternalSessionFactory(externalSessionFactory.getId(), result == RegistrationResult.SUCCESS));
	}
	
	/**
	 * 注册SessionFactory
	 * @param sessionFactory
	 * @return 
	 * @throws IdDuplicateException 
	 */
	private RegistrationResult registerSessionFactory(SessionFactory sessionFactory) throws IdDuplicateException {
		try {
			return SessionFactoryContainer.getSingleton().register(sessionFactory);
		} catch (IdDuplicateException e) {
			throw new IdDuplicateException("已经存在id为"+sessionFactory.getId()+"的流程引擎");
		}
	}
}
