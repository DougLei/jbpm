package com.douglei.bpm.process;

import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.bpm.process.container.ProcessContainer;
import com.douglei.bpm.process.executer.Process;
import com.douglei.bpm.process.parser.ProcessParser;

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
		Process process = processParser.parse(processDefined.getId(), processDefined.getContent());
		container.addProcess(process);
		return process;
	}
	
	/**
	 * 删除流程
	 * @param processId
	 */
	public void remove(int processId) {
		container.deleteProcess(processId);
	}

	/**
	 * 获取流程
	 * @param processId
	 * @return
	 */
	public Process get(int processId) {
		return container.getProcess(processId);
	}
}
