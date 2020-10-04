package com.douglei.bpm;

import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.core.process.ProcessSerializationFolderContainer;
import com.douglei.bpm.module.history.HistoryModule;
import com.douglei.bpm.module.repository.RepositoryModule;
import com.douglei.bpm.module.runtime.RuntimeModule;
import com.douglei.orm.mapping.execute.MappingExecuteException;

/**
 * 流程引擎
 * @author DougLei
 */
public abstract class ProcessEngine {
	protected String id; // 引擎id, 即SessionFactory的id, 所以也可以从SessionFactoryContainer.get().getId()来获取
	
	@Attribute
	private RepositoryModule repository;
	
	@Attribute
	private RuntimeModule runtime;
	
	@Attribute
	private HistoryModule history;
	
	protected ProcessEngine(String id) {
		this.id = id;
		ProcessSerializationFolderContainer.createFolder(this.id);
	}
	
	public String getId() {
		return id;
	}
	public RepositoryModule getRepository() {
		return repository;
	}
	public RuntimeModule getRuntime() {
		return runtime;
	}
	public HistoryModule getHistory() {
		return history;
	}
	
	protected void destroy() throws MappingExecuteException{
		ProcessSerializationFolderContainer.deleteFolder(this.id);
	}
}
