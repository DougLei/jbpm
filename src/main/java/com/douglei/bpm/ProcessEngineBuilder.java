package com.douglei.bpm;

import java.util.ArrayList;
import java.util.List;

import com.douglei.orm.configuration.Configuration;
import com.douglei.orm.configuration.ExternalDataSource;
import com.douglei.orm.configuration.environment.mapping.MappingEntity;
import com.douglei.orm.configuration.environment.mapping.MappingType;
import com.douglei.orm.configuration.environment.mapping.ParseMappingException;
import com.douglei.orm.configuration.impl.ConfigurationImpl;
import com.douglei.orm.configuration.impl.element.environment.mapping.AddOrCoverMappingEntity;
import com.douglei.orm.context.SessionFactoryRegister;
import com.douglei.orm.core.dialect.mapping.MappingExecuteException;
import com.douglei.orm.sessionfactory.SessionFactory;
import com.douglei.tools.instances.scanner.FileScanner;

/**
 * 流程引擎构建器
 * @author DougLei
 */
public class ProcessEngineBuilder {
	private static final String DEFAULT_CONFIGURATION_FILE_PATH = "jbpm.conf.xml"; // 默认的配置文件路径
	private static final String MAPPING_FILE_ROOT_PATH = "jbpm-mappings"; // 工作流引擎相关的mapping文件根路径
	private static final ProcessEngineBeanFactory beanFactory = new ProcessEngineBeanFactory(); // 引擎bean工厂
	private static SessionFactoryRegister register;
	
	public ProcessEngineBuilder() {}
	public ProcessEngineBuilder(SessionFactoryRegister register) {
		ProcessEngineBuilder.register = register;
	}
	
//	/**
//	 * [自成服务]使用默认的jdb-orm配置文件, 构建引擎
//	 * @return 
//	 */
//	public ProcessEngine build() {
//		return build(DEFAULT_CONFIGURATION_FILE_PATH);
//	}
//	
//	/**
//	 * [自成服务]根据指定的jdb-orm配置文件, 构建引擎
//	 * @param configurationFilePath 配置文件路径
//	 * @return 
//	 */
//	public ProcessEngine build(String configurationFilePath) {
//		if(ProcessEngineBuilder.register == null)
//			ProcessEngineBuilder.register = new SessionFactoryRegister();
//		
//		Configuration configuration = new ConfigurationImpl(configurationFilePath);
//		return build_(new SessionfactoryWrapper(register, configuration.buildSessionFactory(), true));
//	}
	
	/**
	 * - 关于以下两种build功能的介绍
	 * 
	 * jbpm流程引擎属于扩展功能, 需要集成到其他系统中使用, 所以有以下两种方式集成: 
	 * 
	 * 1. 使用了jdb-orm框架的系统, 使用 {@link ProcessEngineBuilder.build(SessionFactory, boolean)} 方法构建引擎, 传入自己的SessionFactory实例
	 *   - 在流程引擎被销毁时, 不会同时销毁SessionFactory实例, 需要在自己的项目中进行SessionFactory销毁
	 *   
	 * 2. 使用了其他 orm框架的系统, 使用 {@link ProcessEngineBuilder.build(String, ExternalDataSource)} 方法构建引擎, 传入引擎的唯一标识, 以及自己的数据源实例
	 *   - 在流程引擎被销毁时, 会同时销毁SessionFactory实例, 因为该SessionFactory是完全属于流程引擎的
	 * 
	 * - 关于构造函数 {@link ProcessEngineBuilder(SessionFactoryRegister)}, 如果你的系统中使用了jdb-orm框架, 且存在SessionFactoryRegister实例, 则必须使用本构造函数传入该实例; 否则传入null, 或者使用 {@link ProcessEngineBuilder()} (无参)构造函数也可
	 */
	
	/**
	 * 获取SessionFactoryRegister实例, 如果不存在, 则创建出实例
	 * @return
	 */
	private SessionFactoryRegister getRegister() {
		if(ProcessEngineBuilder.register == null)
			ProcessEngineBuilder.register = new SessionFactoryRegister();
		return ProcessEngineBuilder.register;
	}
	
	/**
	 * [集成]使用外部的 {@link SessionFactory}, 构建引擎
	 * @param exSessionFactory 外部的 {@link SessionFactory} 实例
	 * @param scanMappingFile 是否扫描(流程相关的)映射文件, 该值取决于传入参数exSessionFactory在构建时, 是否扫描过 {@link ProcessEngineBuilder#MAPPING_FILE_ROOT_PATH}
	 * @return 
	 * @throws ParseMappingException
	 * @throws MappingExecuteException
	 */
	public ProcessEngine build(SessionFactory exSessionFactory, boolean scanMappingFile) throws ParseMappingException, MappingExecuteException{
		if(scanMappingFile) {
			List<String> mappingFiles = new FileScanner(MappingType.getMappingFileSuffixs()).scan(MAPPING_FILE_ROOT_PATH);
			List<MappingEntity> mappingEntities = new ArrayList<MappingEntity>(mappingFiles.size());
			for (String mappingFile : mappingFiles) 
				mappingEntities.add(new AddOrCoverMappingEntity(mappingFile));
			exSessionFactory.getMappingProcessor().execute(mappingEntities);
		}
		return build_(new SessionfactoryWrapper(getRegister(), exSessionFactory, false));
	}
	
	/**
	 * [集成]通过外部的数据源, 构建引擎
	 * @param engineId 引擎的唯一标识, 在多数据源情况中必须传入; 当只有一个数据源时, 该参数可传入null
	 * @param exDataSource 外部的数据源实例
	 * @return 
	 */
	public ProcessEngine build(String engineId, ExternalDataSource exDataSource) {
		
		
		Configuration configuration = new ConfigurationImpl(DEFAULT_CONFIGURATION_FILE_PATH);
		configuration.setId(engineId);
		configuration.setExternalDataSource(exDataSource);
		return build_(new SessionfactoryWrapper(getRegister(), configuration.buildSessionFactory(), true));
	}
	
	/**
	 * 构建流程引擎
	 * @param sessionfactoryWrapper
	 * @return
	 */
	private ProcessEngine build_(SessionfactoryWrapper sessionfactoryWrapper) {
		ProcessEngine engine = new ProcessEngine(sessionfactoryWrapper);
		beanFactory.setProcessEngineFields(engine);
		return engine;
	}
}
