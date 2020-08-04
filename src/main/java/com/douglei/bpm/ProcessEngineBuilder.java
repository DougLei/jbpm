package com.douglei.bpm;

import com.douglei.orm.configuration.Configuration;
import com.douglei.orm.configuration.ExternalDataSource;
import com.douglei.orm.configuration.impl.xml.XmlConfiguration;

/**
 * 流程引擎构建器
 * @author DougLei
 */
public class ProcessEngineBuilder {
	
	/**
	 * 根据jdb-orm的配置文件, 构建流程引擎
	 * @param jdbOrmConfigurationFile
	 * @return
	 */
	public ProcessEngine build(String jdbOrmConfigurationFile) {
		return build(jdbOrmConfigurationFile, null);
	}
	
	/**
	 * 根据jdb-orm的配置文件, 以及外部传入的数据源, 构建流程引擎
	 * @param jdbOrmConfigurationFile
	 * @param exDataSource
	 * @return
	 */
	public ProcessEngine build(String jdbOrmConfigurationFile, ExternalDataSource exDataSource) {
		Configuration configuration = new XmlConfiguration(jdbOrmConfigurationFile);
		if(exDataSource != null)
			configuration.setExternalDataSource(exDataSource);
		return new ProcessEngine(configuration.buildSessionFactory());
	}
}
