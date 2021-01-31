package com.douglei.bpm.module.repository.definition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.tools.StringUtil;
import com.douglei.tools.file.reader.FileBufferedReader;

/**
 * 
 * @author DougLei
 */
public class ProcessDefinitionEntity {
	private ProcessDefinition processDefinition;
	private boolean strict; // 如果构建出的流程定义已经存在实例, 根据该参数决定是无法保存本次的流程定义(false), 或是强制更新子版本来保存(true)
	private boolean validate; // 是否对流程定义内容进行校验, 校验失败时会抛出 {@link ProcessParseException} 
	
	/**
	 * 根据文件构建流程定义实例
	 * @param file
	 * @throws FileNotFoundException
	 */
	public ProcessDefinitionEntity(File file) throws FileNotFoundException {
		this(new FileBufferedReader(new FileInputStream(file)).readAll(1000).toString());
	}
	
	/**
	 * 根据classpath下的资源文件构建流程定义实例
	 * @param file
	 */
	public ProcessDefinitionEntity(ClasspathFile file) {
		this(new FileBufferedReader(file.getName()).readAll(1000).toString());
	}
	
	/**
	 * 根据流程配置内容构建流程定义实例
	 * @param content
	 */
	public ProcessDefinitionEntity(String content) {
		Map<String, String> attributeMap = getProcessAttributeMap(content);
		this.processDefinition = new ProcessDefinition(attributeMap.get("name"), attributeMap.get("code"), attributeMap.get("version"));
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
	 * 设置是否强制保存流程定义
	 * @param strict
	 */
	public void setStrict(boolean strict) {
		this.strict = strict;
	}
	
	/**
	 * 设置是否对流程定义的内容进行校验, 校验失败时会抛出 {@link ProcessParseException} 
	 * @param validate
	 */
	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	/**
	 * 设置流程定义的类型id
	 * @param typeId
	 * @return
	 */
	public ProcessDefinitionEntity setTypeId(int typeId) {
		processDefinition.setTypeId(typeId);
		return this;
	}
	
	/**
	 * 设置流程定义的描述
	 * @param description
	 * @return
	 */
	public ProcessDefinitionEntity setDescription(String description) {
		processDefinition.setDescription(description);
		return this;
	}
	
	/**
	 * 设置流程定义的租户
	 * @param description
	 * @return
	 */
	public ProcessDefinitionEntity setTenantId(String tenantId) {
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
	 * 是否要强制保存
	 * @return
	 */
	boolean isStrict() {
		return strict;
	}
	
	/**
	 * 是否对流程定义的内容进行校验, 校验失败时会抛出 {@link ProcessParseException} 
	 * @return
	 */
	boolean isValidate() {
		return validate;
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
