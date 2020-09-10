package com.douglei.bpm.module.repository.definition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import com.douglei.tools.instances.file.reader.FileBufferedReader;

/**
 * 流程定义的构建器
 * @author DougLei
 */
public class ProcessDefinitionBuilder {
	private String content;
	private int typeId;
	private String description;
	
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
		this.content = content;
	}
	

	/**
	 * 设置流程的类型id
	 * @param typeId
	 * @return
	 */
	public ProcessDefinitionBuilder setTypeId(int typeId) {
		this.typeId = typeId;
		return this;
	}
	
	/**
	 * 设置流程的描述
	 * @param description
	 * @return
	 */
	public ProcessDefinitionBuilder setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * 构建流程定义实例
	 * @return
	 */
	public ProcessDefinition buildProcessDefinition() {
		ProcessDefinition processDefinition = new ProcessDefinition();
		
		// 获取name, code, version的值
		char c;
		for(int i=content.indexOf("<process ");;i++) {
			c = content.charAt(i);
			if(c == ' ' || c == '=' || c == '"' || c == '\'' || c == '\t' || c == '\n' || c == '\r')
				continue;
			if(c == '>')
				break;
			if(c == 'c')
				i+=4;
				processDefinition.setCode(code);
			if(c == 'v')
				processDefinition.setVersion(version);
			if(c == 'n')
				processDefinition.setName(name);
		}
		
		processDefinition.setRefTypeId(typeId);
		processDefinition.setDescription(description);
		return processDefinition;
	}
}
