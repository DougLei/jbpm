package com.douglei.bpm.process;

import java.util.Arrays;

import com.douglei.bpm.bean.Autowire;
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
	
	@Autowire
	private ProcessParser parser;
	
	@Autowire
	private ProcessContainer container;
	
	/**
	 * 添加指定id的流程
	 * @param processDefinition
	 * @return 返回解析出的流程执行器实例
	 */
	public Process add(ProcessDefinition processDefinition) {
		Process process = parser.parse(processDefinition);
		container.addProcess(process);
		return process;
	}
	
	/**
	 * 删除指定key的流程
	 * @param key
	 */
	public void delete(String key) {
		container.deleteProcess(key);
	}

	/**
	 * 获取指定id的流程
	 * @param id
	 * @return
	 */
	@Transaction
	public Process get(String id) {
		Process process =  container.getProcess(id);
		if(process == null) {
			ProcessDefinition processDefinition = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, content_ from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
			if(processDefinition == null)
				throw new NullPointerException("不存在id为["+id+"]的流程定义");
			process = add(processDefinition);
		}
		return process;
	}
}
