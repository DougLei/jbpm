package com.douglei.bpm.core.process;

import com.douglei.bpm.annotation.ProcessEngineBean;
import com.douglei.bpm.core.process.executer.Process;

/**
 * 流程处理程序
 * @author DougLei
 */
@ProcessEngineBean
public class ProcessHandler {
	private ProcessSerializationHandler processSerializationHandler;
	
	/**
	 * 根据配置内容, 解析流程实例
	 * @param content
	 * @param subversion 
	 */
	public void parse(String content, int subversion) {
		// TODO Auto-generated method stub

	
	}
	
	/**
	 * 获取流程实例
	 * @param code
	 * @param version
	 * @param subversion
	 * @return
	 */
	public Process get(String code, String version, int subversion) {
		return processSerializationHandler.deserialize(code, version, subversion);
	}
	
	/**
	 * 删除流程实例
	 * @param code
	 * @param version
	 * @param subversion
	 */
	public void delete(String code, String version, int subversion) {
		processSerializationHandler.deleteFile(code, version, subversion);
	}
}
