package com.douglei.bpm.core.process;

import java.io.File;

import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.core.process.executer.Process;
import com.douglei.bpm.core.process.parser.impl.ProcessParser;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.orm.context.SessionFactoryRegister;
import com.douglei.tools.utils.serialize.JdkSerializeProcessor;

/**
 * 流程处理程序
 * @author DougLei
 */
@Bean(transaction = false)
public class ProcessHandler {
	private SessionFactoryRegister register = SessionFactoryRegister.getSingleton();
	
	@Attribute
	private ProcessParser processParser;
	
	// 获取对应的bpm序列化文件全路径
	private String getBpmFilePath(String code, String version, int subversion) {
		return ProcessSerializationFolderContainer.getFolder(register.get().getId()) + code + "." + version + "." + subversion;
	}
	
	// 创建序列化文件
	private void createFile(Process process, int subversion) {
		JdkSerializeProcessor.serialize2File(process, getBpmFilePath(process.getCode(), process.getVersion(), subversion));
	}
	
	/**
	 * 根据配置内容, 解析流程实例
	 * @param content
	 * @param subversion 
	 */
	public void parse(String content, int subversion) {
		createFile(processParser.parse(content), subversion);
	}
	
	/**
	 * 删除流程实例
	 * @param processDefinition
	 */
	public void delete(ProcessDefinition processDefinition) {
		File file = new File(getBpmFilePath(processDefinition.getCode(), processDefinition.getVersion(), processDefinition.getSubversion()));
		if(file.exists()) 
			file.delete();
	}
	
	/**
	 * 获取流程实例
	 * @param code
	 * @param version
	 * @param subversion
	 * @return
	 */
	public Process get(String code, String version, int subversion) {
		return JdkSerializeProcessor.deserializeFromFile(Process.class, getBpmFilePath(code, version, subversion));
	}
}
