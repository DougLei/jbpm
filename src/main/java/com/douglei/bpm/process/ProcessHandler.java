package com.douglei.bpm.process;

import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.bpm.process.executer.Process;
import com.douglei.bpm.process.parser.impl.ProcessParser;

/**
 * 流程处理程序
 * @author DougLei
 */
@Bean(transaction = false)
public class ProcessHandler {
	
	@Attribute
	private ProcessParser processParser;
	
	/**
	 * 根据配置内容, 解析流程实例
	 * @param content
	 * @param subversion 
	 */
	public void parse(String content, int subversion) {
	}
	
	/**
	 * 删除流程实例
	 * @param processDefinition
	 */
	public void delete(ProcessDefinition processDefinition) {
	}
	
	/**
	 * 获取流程实例
	 * @param code
	 * @param version
	 * @param subversion
	 * @return
	 */
	public Process get(String code, String version, int subversion) {
		return null;
	}
}
