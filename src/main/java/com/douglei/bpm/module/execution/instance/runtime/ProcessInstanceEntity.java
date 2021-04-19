package com.douglei.bpm.module.execution.instance.runtime;

import java.util.Arrays;

import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class ProcessInstanceEntity {
	private ProcessInstance instance;
	
	public ProcessInstanceEntity(int id) {
		this.instance = SessionContext.getTableSession().uniqueQuery(ProcessInstance.class, "select * from bpm_ru_procinst where id=?", Arrays.asList(id));
		if(instance == null)
			throw new TaskHandleException("不存在id为["+id+"]的流程实例");
	}
	
	public ProcessInstanceEntity(String procinstId) {
		this.instance = SessionContext.getTableSession().uniqueQuery(ProcessInstance.class, "select * from bpm_ru_procinst where procinst_id=?", Arrays.asList(procinstId));
		if(instance == null)
			throw new TaskHandleException("不存在procinst_id为["+procinstId+"]的流程实例");
	}
	
	/**
	 * 获取流程实例
	 * @return
	 */
	public ProcessInstance getProcessInstance() {
		return instance;
	}
}
