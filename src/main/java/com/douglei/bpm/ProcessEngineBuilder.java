package com.douglei.bpm;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.bpm.bean.BeanFactory;
import com.douglei.bpm.process.container.ProcessContainer;
import com.douglei.orm.configuration.Configuration;
import com.douglei.orm.configuration.ExternalDataSource;
import com.douglei.orm.context.RegistrationResult;
import com.douglei.orm.context.SessionFactoryContainer;
import com.douglei.orm.mapping.handler.entity.MappingEntity;
import com.douglei.orm.mapping.handler.entity.impl.AddOrCoverMappingEntity;
import com.douglei.orm.mapping.type.MappingTypeContainer;
import com.douglei.orm.sessionfactory.SessionFactory;
import com.douglei.tools.instances.resource.scanner.impl.ResourceScanner;
import com.douglei.tools.utils.ExceptionUtil;

/**
 * 流程引擎构建器, 一个构建器只能构建一个引擎
 * @author DougLei
 */
public class ProcessEngineBuilder {
	private static final Logger logger = LoggerFactory.getLogger(ProcessEngineBuilder.class);
	private static final String DEFAULT_CONFIGURATION_FILE_PATH = "jbpm.conf.xml"; // 默认的配置文件路径
	private boolean isBuild;
	private ProcessEngine engine;
	private BeanFactory beanFactory = new BeanFactory();
	
	/**
	 * - 关于以下四种构建函数的介绍
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
	 * 特别说明, 构建出的流程引擎, 在销毁的时候, 都不会对流程相关的表结构和其中的数据产生影响
	 */
	
	/**
	 * 使用默认的jbpm.conf.xml配置文件, 构建引擎
	 */
	public ProcessEngineBuilder() {
		this(DEFAULT_CONFIGURATION_FILE_PATH); // 默认的配置文件路径
	}
	/**
	 * 根据指定name的jbpm.conf.xml配置文件, 构建引擎
	 * @param configurationFilePath 配置文件路径
	 */
	public ProcessEngineBuilder(String configurationFilePath) {
		try {
			Configuration configuration = new Configuration(configurationFilePath);
			SessionFactory sessionFactory = configuration.buildSessionFactory();
			SessionFactoryContainer.getSingleton().register(sessionFactory);
			this.engine = new ProcessEngineByBuiltinSessionFactory(sessionFactory.getId());
		} catch (Exception e) {
			logger.error("构建流程引擎时出现异常: {}", ExceptionUtil.getExceptionDetailMessage(e));
			throw new JBPMException("构建流程引擎时出现异常", e);
		}
	}
	
	/**
	 * 通过外部的数据源, 构建引擎
	 * @param dataSource 外部的数据源实例
	 */
	public ProcessEngineBuilder(DataSource dataSource) {
		this(null, dataSource);
	}
	/**
	 * 通过外部的数据源, 构建引擎
	 * @param engineId 引擎的唯一标识, 在多数据源情况中必须传入; 当只有一个数据源时, 该参数可传入null, 使用默认值 defaultJBPMId
	 * @param dataSource 外部的数据源实例
	 */
	public ProcessEngineBuilder(String engineId, DataSource dataSource) {
		try {
			Configuration configuration = new Configuration(DEFAULT_CONFIGURATION_FILE_PATH);
			configuration.setId(engineId);
			configuration.setExternalDataSource(new ExternalDataSource(dataSource));
			
			SessionFactory sessionFactory = configuration.buildSessionFactory();
			SessionFactoryContainer.getSingleton().register(sessionFactory);
			this.engine = new ProcessEngineByBuiltinSessionFactory(sessionFactory.getId());
		} catch (Exception e) {
			logger.error("构建流程引擎时出现异常: {}", ExceptionUtil.getExceptionDetailMessage(e));
			throw new JBPMException("构建流程引擎时出现异常", e);
		}
	}
	
	/**
	 * 使用外部的 {@link SessionFactory}实例, 构建引擎
	 * @param externalSessionFactory 外部的 {@link SessionFactory} 实例
	 */
	public ProcessEngineBuilder(SessionFactory externalSessionFactory) {
		try {
			List<String> mappingFiles = new ResourceScanner(MappingTypeContainer.getFileSuffixes().toArray(new String[MappingTypeContainer.getFileSuffixes().size()])).scan("jbpm-mappings");// 工作流引擎相关的mapping文件根路径
			List<MappingEntity> mappingEntities = new ArrayList<MappingEntity>(mappingFiles.size());
			mappingFiles.forEach(mappingFile -> mappingEntities.add(new AddOrCoverMappingEntity(mappingFile)));
			externalSessionFactory.getMappingHandler().execute(mappingEntities);
			
			RegistrationResult result = SessionFactoryContainer.getSingleton().register(externalSessionFactory);
			this.engine = new ProcessEngineByExternalSessionFactory(externalSessionFactory.getId(), result == RegistrationResult.SUCCESS, mappingFiles);
		} catch (Exception e) {
			logger.error("构建流程引擎时出现异常: {}", ExceptionUtil.getExceptionDetailMessage(e));
			throw new JBPMException("构建流程引擎时出现异常", e);
		}
	}
	
	/**
	 * 设置流程容器
	 * @param container
	 * @return
	 */
	public ProcessEngineBuilder setContainer(ProcessContainer container) {
		beanFactory.registerCustomImplBean(ProcessContainer.class, container);
		return this;
	}
	
	/**
	 * 构建流程引擎
	 * @return
	 */
	public ProcessEngine build() {
		if(isBuild)
			return engine;
		
		beanFactory.registerCustomImplBean(ProcessEngine.class, engine);
		beanFactory.setBeanAttributes();
		isBuild = true;
		return engine;
	}
}
