package com.douglei.bpm.process;

import java.util.Arrays;

import com.douglei.bpm.bean.Attribute;
import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.process.container.ProcessContainer;
import com.douglei.bpm.process.executor.Process;
import com.douglei.bpm.process.parser.ProcessParser;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 
 * @author DougLei
 */
@Bean
public class ProcessHandler {
	
	@Attribute
	private ProcessParser processParser;
	
	@Attribute
	private ProcessContainer container;
	
	/**
	 * 添加流程
	 * @param processDefinition
	 * @return 返回解析出的流程执行器实例
	 */
	public Process put(ProcessDefinition processDefinition) {
		Process process = processParser.parse(processDefinition.getId(), processDefinition.getContent());
		container.addProcess(process);
		return process;
	}
	
	/**
	 * 移除流程
	 * @param processDefinitionId
	 */
	public void remove(int processDefinitionId) {
		container.deleteProcess(processDefinitionId);
	}

	/**
	 * 获取流程
	 * @param processDefinitionId
	 * @return
	 */
	@Transaction
	public Process get(int processDefinitionId) {
		Process process =  container.getProcess(processDefinitionId);
		if(process == null) {
			ProcessDefinition processDefinition = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, content_ from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
			if(processDefinition == null)
				throw new NullPointerException("不存在id为["+processDefinitionId+"]的流程定义");
			process = put(processDefinition);
		}
		return process;
	}
}
