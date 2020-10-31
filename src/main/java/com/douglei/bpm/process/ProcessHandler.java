package com.douglei.bpm.process;

import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.bpm.process.container.ProcessContainer;
import com.douglei.bpm.process.executer.Process;
import com.douglei.bpm.process.parser.impl.ProcessParser;

/**
 * 流程处理器
 * @author DougLei
 */
@Bean(transaction = false)
public class ProcessHandler {
	
	@Attribute
	private ProcessParser processParser;
	
	@Attribute
	private ProcessContainer container;
	
	/**
	 * 保存流程
	 * @param processDefined
	 * @return 返回解析出的流程实例
	 */
	public Process put(ProcessDefinition processDefined) {
		// TODO 解析流程定义, 并放到容器中
		
		return null;
	}
	
	/**
	 * 删除流程
	 * @param processDefinition
	 */
	public void remove(ProcessDefinition processDefinition) {
		// TODO 从容器中移除流程定义即可
		
	}

	/**
	 * 获取流程
	 * @param processDefinitionId
	 * @return
	 */
	public Process get(int processDefinitionId) {
		return container.getProcess(processDefinitionId);
	}
}
