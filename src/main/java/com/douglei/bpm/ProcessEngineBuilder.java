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
	private ProcessEngine engine;
	
	/**
	 * 使用默认的jdb-orm配置文件, 创建SessionFactory实例
	 * @return 将返回的SessionFactory实例, 使用 {@link SessionFactoryRegister}进行注册
	 */
	public SessionFactory createSessionFactory() {
		return createSessionFactory(DEFAULT_CONFIGURATION_FILE_PATH);
	}
	
	/**
	 * 根据指定的jdb-orm配置文件, 创建SessionFactory实例
	 * @param configurationFilePath 配置文件路径
	 * @return 将返回的SessionFactory实例, 使用 {@link SessionFactoryRegister}进行注册
	 */
	public SessionFactory createSessionFactory(String configurationFilePath) {
		Configuration configuration = new ConfigurationImpl(configurationFilePath);
		return configuration.buildSessionFactory();
	}
	
	/**
	 * 通过外部的数据源, 创建SessionFactory实例
	 * @param engineId 引擎的唯一标识, 在多数据源情况中可以使用, 当只有一个数据源时, 该参数可传入null
	 * @param exDataSource 外部的数据源实例
	 * @return 将返回的SessionFactory实例, 使用 {@link SessionFactoryRegister}进行注册
	 */
	public SessionFactory createSessionFactory(String engineId, ExternalDataSource exDataSource) {
		Configuration configuration = new ConfigurationImpl(DEFAULT_CONFIGURATION_FILE_PATH);
		configuration.setId(engineId);
		configuration.setExternalDataSource(exDataSource);
		return configuration.buildSessionFactory();
	}
	
	/**
	 * 使用外部的 {@link SessionFactory}, 创建SessionFactory实例
	 * @param exSessionFactory 外部的 {@link SessionFactory} 实例
	 * @param scanMappingFile 是否扫描(流程相关的)映射文件, 该值取决于传入参数exSessionFactory在构建时, 是否扫描过 {@link ProcessEngineBuilder#MAPPING_FILE_ROOT_PATH}
	 * @return 将返回的SessionFactory实例, 使用 {@link SessionFactoryRegister}进行注册, 如传入的exSessionFactory已经注册过, 则可以不用注册
	 * @throws ParseMappingException
	 * @throws MappingExecuteException
	 */
	public SessionFactory createSessionFactory(SessionFactory exSessionFactory, boolean scanMappingFile) throws ParseMappingException, MappingExecuteException{
		if(scanMappingFile) {
			List<String> mappingFiles = new FileScanner(MappingType.getMappingFileSuffixs()).scan(MAPPING_FILE_ROOT_PATH);
			List<MappingEntity> mappingEntities = new ArrayList<MappingEntity>(mappingFiles.size());
			for (String mappingFile : mappingFiles) 
				mappingEntities.add(new AddOrCoverMappingEntity(mappingFile));
			exSessionFactory.getMappingProcessor().execute(mappingEntities);
		}
		return exSessionFactory;
	}

	/**
	 * 构建流程引擎
	 * @param sessionFactory
	 * @return
	 */
	public ProcessEngine build(SessionFactory sessionFactory) {
		if(engine == null) {
			ProcessEngineBeanFactory factory = new ProcessEngineBeanFactory();
			engine = new ProcessEngine(sessionFactory.getId());
			factory.setProcessEngineFields(engine);
		}
		return engine;
	}
}
