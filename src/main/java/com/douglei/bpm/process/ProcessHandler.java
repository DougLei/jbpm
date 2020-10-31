package com.douglei.bpm.process;

import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.bpm.process.container.ProcessContainer;
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
	 * 发布流程
	 * @param processDefined
	 */
	public void publish(ProcessDefinition processDefined) {
		// TODO 解析流程定义, 并放到容器中
		
	}
	
	/**
	 * 取消发布流程
	 * @param processDefinition
	 */
	public void cancelPublish(ProcessDefinition processDefinition) {
		// TODO 从容器中移除流程定义即可
		
	}
}
