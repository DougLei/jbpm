package com.douglei.bpm.module.repository.definition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.douglei.bpm.process.mapping.parser.ProcessParseException;
import com.douglei.tools.StringUtil;
import com.douglei.tools.file.reader.FileBufferedReader;

/**
 * 
 * @author DougLei
 */
public class ProcessDefinitionBuilder {
	private ProcessDefinition definition;
	private boolean ignore; // 是否忽略流程实例
	private boolean strict; // 是否强制保存流程定义
	
	/**
	 * 根据文件构建流程定义实例
	 * @param file
	 * @throws FileNotFoundException
	 */
	public ProcessDefinitionBuilder(File file) throws FileNotFoundException {
		try(FileBufferedReader reader = new FileBufferedReader(new FileInputStream(file))){
			init(reader.readAll(1000));
		}
	}
	
	/**
	 * 根据classpath下的资源文件构建流程定义实例
	 * @param file
	 */
	public ProcessDefinitionBuilder(ClasspathFile file) {
		try(FileBufferedReader reader = new FileBufferedReader(file.getPath())){
			init(reader.readAll(1000));
		}
	}
	
	/**
	 * 根据流程配置内容构建流程定义实例
	 * @param content
	 */
	public ProcessDefinitionBuilder(String content) {
		init(content);
	}
	
	// 根据流程配置内容构建流程定义实例
	private void init(String content) {
		Map<String, String> attributeMap = getProcessAttributeMap(content);
		this.definition = new ProcessDefinition(attributeMap.get("name"), attributeMap.get("code"), attributeMap.get("version"));
		this.definition.setContent(content);
		this.definition.setSignature(DigestUtils.md5Hex(content));
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
	 * 设置是否忽略流程实例
	 * @param ignore
	 */
	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}
	
	/**
	 * 设置是否强制保存流程定义
	 * @param strict
	 */
	public void setStrict(boolean strict) {
		this.strict = strict;
	}
	
	/**
	 * 设置流程定义的类型id
	 * @param typeId
	 * @return
	 */
	public ProcessDefinitionBuilder setTypeId(int typeId) {
		definition.setTypeId(typeId);
		return this;
	}
	
	/**
	 * 设置流程定义的描述
	 * @param description
	 * @return
	 */
	public ProcessDefinitionBuilder setDescription(String description) {
		definition.setDescription(description);
		return this;
	}
	
	/**
	 * 设置流程定义的租户
	 * @param description
	 * @return
	 */
	public ProcessDefinitionBuilder setTenantId(String tenantId) {
		definition.setTenantId(tenantId);
		return this;
	}
	
	/**
	 * 构建流程定义实例
	 * @return
	 */
	public ProcessDefinition build() {
		return definition;
	}
	
	/**
	 * 是否忽略流程实例
	 * @return
	 */
	boolean isIgnore() {
		return ignore;
	}
	
	/**
	 * 是否强制保存流程定义
	 * @return
	 */
	boolean isStrict() {
		return strict;
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
