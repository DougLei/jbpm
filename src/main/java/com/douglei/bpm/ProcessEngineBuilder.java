package com.douglei.bpm;

import javax.sql.DataSource;

import com.douglei.bpm.bean.BeanFactory;
import com.douglei.orm.context.SessionFactoryContainer;
import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * 
 * @author DougLei
 */
public class ProcessEngineBuilder {
	private String engineId; // 引擎唯一标识
	private ProcessEngine engine;
	private BeanFactory beanFactory = new BeanFactory();
	
	public ProcessEngineBuilder() {}
	public ProcessEngineBuilder(String engineId) {
		this.engineId = engineId;
	}

	/**
	 * 使用默认filepath的jbpm.conf.xml配置文件(基于java resource), 构建引擎
	 * @return
	 */
	public ProcessEngine build() {
		return build("jbpm.conf.xml");
	}
	
	/**
	 * 根据指定filepath的jbpm.conf.xml配置文件(基于java resource), 构建引擎
	 * @param filepath
	 * @return
	 */
	public ProcessEngine build(String filepath) {
		if(engine == null) {
			engine = new ProcessEngine(engineId, filepath);
			
			beanFactory.registerCustomBean(ProcessEngine.class, engine);
			beanFactory.registerCustomBean(SessionFactoryContainer.class, SessionFactoryContainer.getSingleton());
			beanFactory.autowireBeans();
		}
		return engine;
	}
	
	/**
	 * 使用外部的SessionFactory, 构建引擎
	 * @param factory
	 * @return
	 */
	public ProcessEngine build(SessionFactory factory) {
		if(engine == null) {
			engine = new ProcessEngine(engineId, factory);
			
			beanFactory.registerCustomBean(ProcessEngine.class, engine);
			beanFactory.registerCustomBean(SessionFactoryContainer.class, SessionFactoryContainer.getSingleton());
			beanFactory.autowireBeans();
		}
		return engine;
	}
	
	/**
	 * 通过外部的数据源, 构建引擎
	 * @param dataSourceId 数据源唯一标识
	 * @param dataSource 数据源
	 * @param closeName 数据源的关闭方法名, 可为null; 在销毁引擎时, 如果为null, 则不会自动关闭数据源
	 * @return
	 */
	public ProcessEngine build(String dataSourceId, DataSource dataSource, String closeName) {
		if(engine == null) {
			engine = new ProcessEngine(engineId, dataSourceId, dataSource, closeName);
			
			beanFactory.registerCustomBean(ProcessEngine.class, engine);
			beanFactory.registerCustomBean(SessionFactoryContainer.class, SessionFactoryContainer.getSingleton());
			beanFactory.autowireBeans();
		}
		return engine;
	}
}
