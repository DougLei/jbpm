package com.douglei.bpm.module.repository.definition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.tools.instances.file.reader.FileBufferedReader;
import com.douglei.tools.utils.StringUtil;

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
		Map<String, String> attribute = getProcessAttributeMap(content);
		this.processDefinition = new ProcessDefinition(attribute.get("name"), attribute.get("code"), attribute.get("version"));
		this.processDefinition.setContent(content);
		this.processDefinition.setSignature(DigestUtils.md5Hex(content));
	}
	
	// 获取<process>的基础属性map
	private Map<String, String> getProcessAttributeMap(String content) {
		Map<String, String> attributeMap = new HashMap<String, String>(8);
		
		LinkedList<Character> chars = new LinkedList<Character>();
		StringBuilder string = new StringBuilder(50);
		
		// 解析属性, 通过两层循环嵌套处理, 第一层处理name, 第二层使用两个同层循环处理value
		char c, mark; // mark用来记录属性值是由单引号还是双引号包裹的
		ProcessAttribute attribute;
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
			attribute = getProcessAttribute(string.toString());
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
					if(attribute != null) {
						while(!chars.isEmpty())
							string.append(chars.removeFirst());
						
						if(StringUtil.isEmpty(string) && attribute.required)
							throw new ProcessParseException("流程定义中的["+attribute.name+"]属性值不能为空");
						attributeMap.put(attribute.name, string.toString());
						string.setLength(0);
					}
					break;
				}else if(attribute != null){
					chars.add(c);
				}
			}
		}
		return attributeMap;
	}
	// 指定字符是否是空白字符
	private boolean isBlank(char c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}
	// 根据解析出的属性名, 获取对应的ProcessAttribute枚举实例
	private ProcessAttribute getProcessAttribute(String attributeName) {
		if(ProcessAttribute.NAME.name.equals(attributeName))
			return ProcessAttribute.NAME;
		if(ProcessAttribute.CODE.name.equals(attributeName))
			return ProcessAttribute.CODE;
		if(ProcessAttribute.VERSION.name.equals(attributeName))
			return ProcessAttribute.VERSION;
		return null;
	}
	
	/**
	 * 设置流程定义的类型id
	 * @param typeId
	 * @return
	 */
	public ProcessDefinitionBuilder setTypeId(int typeId) {
		processDefinition.setTypeId(typeId);
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
	 * 设置流程定义的租户
	 * @param description
	 * @return
	 */
	public ProcessDefinitionBuilder setTenantId(String tenantId) {
		processDefinition.setTenantId(tenantId);
		return this;
	}
	
	/**
	 * 获取流程定义实例
	 * @return
	 */
	ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}
	
	
	/**
	 * 流程的基础属性
	 * @author DougLei
	 */
	private enum ProcessAttribute {
		NAME(false),
		CODE(true),
		VERSION(true);
		
		String name;
		boolean required;
		private ProcessAttribute(boolean required) {
			this.name = name().toLowerCase();
			this.required = required;
		}
	}
}
