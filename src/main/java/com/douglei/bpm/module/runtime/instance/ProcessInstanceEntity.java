package com.douglei.bpm.module.runtime.instance;

import java.util.Arrays;

import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class ProcessInstanceEntity {
	private ProcessInstance processInstance;
	
	ProcessInstanceEntity(int id) {
		this.processInstance = SessionContext.getTableSession().uniqueQuery(ProcessInstance.class, "select * from bpm_ru_procinst where id=?", Arrays.asList(id));
		if(processInstance == null)
			throw new TaskHandleException("不存在id为["+id+"]的流程实例");
	}
	
	ProcessInstanceEntity(String procinstId) {
		this.processInstance = SessionContext.getTableSession().uniqueQuery(ProcessInstance.class, "select * from bpm_ru_procinst where procinst_id=?", Arrays.asList(procinstId));
		if(processInstance == null)
			throw new TaskHandleException("不存在procinst_id为["+procinstId+"]的流程实例");
	}
	
	/**
	 * 获取流程实例
	 * @return
	 */
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}
}
