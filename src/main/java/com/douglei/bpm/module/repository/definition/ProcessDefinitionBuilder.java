package com.douglei.bpm.module.repository.definition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.douglei.tools.instances.file.reader.ClasspathFile;
import com.douglei.tools.instances.file.reader.FileBufferedReader;

/**
 * 流程定义的构建器
 * @author DougLei
 */
public class ProcessDefinitionBuilder {
	private ProcessDefinition processDefinition;

	/**
	 * 根据文件构建流程定义实例
	 * @param file
	 * @throws FileNotFoundException
	 */
	public ProcessDefinitionBuilder(File file) throws FileNotFoundException {
		this(new FileBufferedReader(new FileInputStream(file)).readAll(1000).toString());
	}
	
	/**
	 * 根据classpath下的资源文件构建流程定义实例
	 * @param file
	 */
	public ProcessDefinitionBuilder(ClasspathFile file) {
		this(new FileBufferedReader(file.getName()).readAll(1000).toString());
	}
	
	/**
	 * 根据流程配置内容构建流程定义实例
	 * @param content
	 */
	public ProcessDefinitionBuilder(String content) {
		// 解析content, 只获取name, code, version
		
		
		
		this.processDefinition = new ProcessDefinition();
	}
	

	/**
	 * 设置流程的类型id
	 * @param typeId
	 * @return
	 */
	public ProcessDefinitionBuilder setRefTypeId(int typeId) {
		processDefinition.setRefTypeId(typeId);
		return this;
	}
	
	/**
	 * 设置流程的描述
	 * @param description
	 * @return
	 */
	public ProcessDefinitionBuilder setDescription(String description) {
		processDefinition.setDescription(description);
		return this;
	}

	/**
	 * 获取流程定义实例
	 * @return
	 */
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}
}
