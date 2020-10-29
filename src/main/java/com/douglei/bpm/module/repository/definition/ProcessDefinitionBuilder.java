package com.douglei.bpm.module.repository.definition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

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
		Map<String, String> attribute = getProcessAttribute(content);
		this.processDefinition = new ProcessDefinition(attribute.get("name"), attribute.get("code"), attribute.get("version"));
		
		this.processDefinition.setContent(content);
		this.processDefinition.setSignature(DigestUtils.md5Hex(content));
	}
	
	// 获取<process>的属性
	private Map<String, String> getProcessAttribute(String content) {
		Map<String, String> attribute = new HashMap<String, String>(8);
		
		LinkedList<Character> chars = new LinkedList<Character>();
		StringBuilder string = new StringBuilder(50);
		
		// 解析属性, 通过两层循环嵌套处理, 第一层处理name, 第二层使用两个同层循环处理value
		char c, mark; // mark用来记录属性值是由单引号还是双引号包裹的
		String attributeName;
		for(int i = content.indexOf("<process ")+9;;i++) {
			c = content.charAt(i);
			if(c == '>')  // <process 元素结尾, 结束循环
				break;
			if(isBlank(c)) // 忽略空白字符
				continue;
			if(c != '=') { 
				chars.add(c); // 只要不是=, 就是属性名
				continue;
			}
			
			// 记录属性名, 并重置StringBuilder
			while(!chars.isEmpty())
				string.append(chars.removeFirst());
			attributeName = getAttributeName(string.toString());
			string.setLength(0);
			
			for(++i;;i++) {
				c = content.charAt(i);
				if(isBlank(c))
					continue;
				mark = c;
				break;
			}
			
			for(++i;;i++) {
				c = content.charAt(i);
				if(c == mark) {
					if(attributeName != null) {
						while(!chars.isEmpty())
							string.append(chars.removeFirst());
						attribute.put(attributeName, string.toString());
						string.setLength(0);
					}
					break;
				}else {
					if(attributeName != null)
						chars.add(c);
				}
			}
		}
		return attribute;
	}
	// 指定字符是否是空白字符
	private boolean isBlank(char c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}
	// 获取属性名, 这里做一个小优化, 如果属性名不是name,code,version, 就返回null
	private static final String attributeNames = "name,code,version";
	private String getAttributeName(String attributeName) {
		if(attributeNames.indexOf(attributeName) > -1)
			return attributeName;
		return null;
	}
	
	
	/**
	 * 设置流程定义的类型id
	 * @param typeId
	 * @return
	 */
	public ProcessDefinitionBuilder setTypeId(int typeId) {
		processDefinition.setRefTypeId(typeId);
		return this;
	}
	
	/**
	 * 设置流程定义的描述
	 * @param description
	 * @return
	 */
	public ProcessDefinitionBuilder setDescription(String description) {
		processDefinition.setDescription(description);
		return this;
	}
	
	/**
	 * 设置流程定义的创建用户
	 * @param createUser
	 * @return
	 */
	public ProcessDefinitionBuilder setCreateUser(String createUser) {
		processDefinition.setCreateUser(createUser);
		return this;
	}

	/**
	 * 设置流程定义的创建时间
	 * @param createDate
	 * @return
	 */
	public ProcessDefinitionBuilder setCreateDate(Date createDate) {
		processDefinition.setCreateDate(createDate);
		return this;
	}

	/**
	 * 设置流程定义的修改用户
	 * @param updateUser
	 * @return
	 */
	public ProcessDefinitionBuilder setUpdateUser(String updateUser) {
		processDefinition.setUpdateUser(updateUser);
		return this;
	}

	/**
	 * 设置流程定义的修改时间
	 * @param updateDate
	 * @return
	 */
	public ProcessDefinitionBuilder setUpdateDate(Date updateDate) {
		processDefinition.setUpdateDate(updateDate);
		return this;
	}

	/**
	 * 构建流程定义实例
	 * @return
	 */
	public ProcessDefinition build() {
		return processDefinition;
	}
}
