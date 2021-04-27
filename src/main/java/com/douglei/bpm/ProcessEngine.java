package com.douglei.bpm;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.module.execution.ExecutionModule;
import com.douglei.bpm.module.repository.RepositoryModule;
import com.douglei.orm.configuration.Configuration;
import com.douglei.orm.configuration.ExternalDataSource;
import com.douglei.orm.context.RegistrationResult;
import com.douglei.orm.context.SessionFactoryContainer;
import com.douglei.orm.context.SessionFactoryIdHolder;
import com.douglei.orm.mapping.MappingTypeContainer;
import com.douglei.orm.mapping.handler.entity.AddOrCoverMappingEntity;
import com.douglei.orm.mapping.handler.entity.DeleteMappingEntity;
import com.douglei.orm.mapping.handler.entity.MappingEntity;
import com.douglei.orm.sessionfactory.SessionFactory;
import com.douglei.tools.file.scanner.impl.ResourceScanner;

/**
 * 流程引擎
 * @author DougLei
 */
public class ProcessEngine {
	private SessionFactoryMapper mapper = new SessionFactoryMapper();
	private String id; // 引擎唯一标识
	
	ProcessEngine(String id, String filepath) {
		SessionFactory factory = new Configuration().buildSessionFactory(filepath);
		SessionFactoryContainer.getSingleton().register(factory);
		this.mapper.put(factory, SessionFactoryType.BUILTIN);
		this.id = id;
	}
	ProcessEngine(String id, SessionFactory factory) {
		RegistrationResult result = SessionFactoryContainer.getSingleton().register(factory);
		mapper.put(factory, (result==RegistrationResult.SUCCESS)?SessionFactoryType.HALF:SessionFactoryType.EXTERNAL);
		this.id = id;
	}
	ProcessEngine(String id, String dataSourceId, DataSource dataSource, String closeName) {
		addDataSource(dataSourceId, dataSource, closeName);
		this.id = id;
	}
	
	@Autowired
	private ProcessEngineBeans processEngineBeans;
	
	@Autowired
	private RepositoryModule repositoryModule;
	
	@Autowired
	private ExecutionModule executionModule;
	
	/**
	 * (多数据源)给引擎添加新的数据源
	 * @param dataSourceId 数据源唯一标识
	 * @param dataSource 数据源
	 * @param closeName 数据源的关闭方法名, 可为null; 在销毁引擎时, 如果为null, 则不会自动关闭数据源
	 */
	public void addDataSource(String dataSourceId, DataSource dataSource, String closeName) {
		Configuration configuration = new Configuration();
		configuration.setId(dataSourceId);
		configuration.setExternalDataSource(new ExternalDataSource(dataSource, closeName));
		
		SessionFactory factory = configuration.buildSessionFactory("jbpm.conf.xml");
		SessionFactoryContainer.getSingleton().register(factory);
		this.mapper.put(factory, SessionFactoryType.BUILTIN);
	}
	
	/**
	 * (多数据源)设置流程引擎要使用的数据源
	 * @param dataSourceId 数据源唯一标识
	 */
	public void setDataSourceId(String dataSourceId) {
		SessionFactoryIdHolder.setId(dataSourceId);
	}
	
	/**
	 * 获取引擎唯一标识
	 * @return
	 */
	public String geId() {
		return id;
	}
	/**
	 * 获取流程(可对外提供)的bean实例
	 * @return
	 */
	public ProcessEngineBeans getProcessEngineBeans() {
		return processEngineBeans;
	}
	/**
	 * 获取RepositoryModule实例
	 * @return
	 */
	public RepositoryModule getRepositoryModule() {
		return repositoryModule;
	}
	/**
	 * 获取ExecutionModule实例
	 * @return
	 */
	public ExecutionModule getExecutionModule() {
		return executionModule;
	}
	/**
	 * 销毁引擎
	 */
	public void destroy() {
		SessionFactory factory = SessionFactoryContainer.getSingleton().get();
		switch(mapper.get(factory.getId())) {
			case BUILTIN:
				SessionFactoryContainer.getSingleton().remove(factory.getId(), true);
				return;
			case HALF:
				SessionFactoryContainer.getSingleton().remove(factory.getId(), false);
				break;
			case EXTERNAL:
				break;
		}
		
		List<MappingEntity> entities = new ArrayList<MappingEntity>(mapper.getMappingFiles().size());
		mapper.getMappingFiles().forEach(mappingFile -> {
			String filename = mappingFile.substring(mappingFile.lastIndexOf(File.separatorChar)+1);
			entities.add(new DeleteMappingEntity(filename.substring(0, filename.indexOf(".")), false));
		});
		factory.getMappingHandler().execute(entities);
	}
	
	
	/**
	 * 
	 * @author DougLei
	 */
	private class SessionFactoryMapper {
		private Map<String, SessionFactoryType> map = new HashMap<String, SessionFactoryType>(); // key=SessionFactory的id, value为SessionFactory的类型
		private List<String> mappingFiles;
		
		/**
		 * 
		 * @param factory
		 * @param type
		 * @param parser
		 */
		public void put(SessionFactory factory, SessionFactoryType type) {
			map.put(factory.getId(), type);
			
			if(type != SessionFactoryType.BUILTIN) {
				// 加载工作流相关mapping文件
				if(this.mappingFiles == null)
					this.mappingFiles = new ResourceScanner(MappingTypeContainer.getFileSuffixes()).scan("jbpm-mappings");
				
				List<MappingEntity> mappingEntities = new ArrayList<MappingEntity>(mappingFiles.size());
				this.mappingFiles.forEach(mappingFile -> mappingEntities.add(new AddOrCoverMappingEntity(mappingFile)));
				factory.getMappingHandler().execute(mappingEntities);
			}
		}
		
		/**
		 * 
		 * @param dataSourceId
		 * @return
		 */
		public SessionFactoryType get(String dataSourceId) {
			SessionFactoryType type = map.get(dataSourceId);
			if(type == null)
				throw new ProcessEngineException("流程引擎中不存在id为"+dataSourceId+"的数据源");
			return type;
		}

		/**
		 * 
		 * @return
		 */
		public List<String> getMappingFiles() {
			return mappingFiles;
		}
	}

	/**
	 * 
	 * @author DougLei
	 */
	private enum SessionFactoryType {
		BUILTIN, 
		EXTERNAL,
		HALF;
	}
}