package com.douglei.bpm.module.execution.instance.history;

import java.util.Arrays;

import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class HistoryProcessInstanceEntity {
	private HistoryProcessInstance instance;
	
	public HistoryProcessInstanceEntity(int id) {
		this.instance = SessionContext.getTableSession().uniqueQuery(HistoryProcessInstance.class, "select * from bpm_hi_procinst where id=?", Arrays.asList(id));
		if(instance == null)
			throw new TaskHandleException("不存在id为["+id+"]的流程实例");
	}
	
	public HistoryProcessInstanceEntity(String procinstId) {
		this.instance = SessionContext.getTableSession().uniqueQuery(HistoryProcessInstance.class, "select * from bpm_hi_procinst where procinst_id=?", Arrays.asList(procinstId));
		if(instance == null)
			throw new TaskHandleException("不存在procinst_id为["+procinstId+"]的流程实例");
	}
	
	/**
	 * 获取流程实例
	 * @return
	 */
	public HistoryProcessInstance getProcessInstance() {
		return instance;
	}
}
