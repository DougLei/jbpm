package com.douglei.bpm.process.mapping.parser.task.user;

import java.util.List;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpression;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpressionContainer;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignNumber;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignableUserExpressionEntity;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;
import com.douglei.tools.StringUtil;

/**
 * 
 * @author DougLei
 */
@Bean
public class AssignPolicyParser {
	
	@Autowired
	private AssignNumberParser assignNumberParser;
	
	@Autowired
	private AssignableUserExpressionContainer assignUserExpressionContainer;
	
	/**
	 * 解析指派策略
	 * @param metadata
	 * @param struct 传入当前解析的xml自有结构
	 * @param candidateElement
	 * @return
	 * @throws ProcessParseException
	 */
	@SuppressWarnings("unchecked")
	public final AssignPolicy parse(UserTaskMetadata metadata, String struct, Element candidateElement) throws ProcessParseException{
		Element element = candidateElement.element("assignPolicy");
		if(element == null)
			throw new ProcessParseException(String.format("<userTask id=%s name=%s>%s下必须配置<assignPolicy>", metadata.getId(), metadata.getName(), struct));
		
		AssignPolicy assignPolicy = parseAssignPolicy(metadata, struct, element);
		setAssignUserExpressionEntities(metadata, struct, assignPolicy, element.elements("expression"));
		return assignPolicy;
	}
	
	// 解析指派策略
	protected AssignPolicy parseAssignPolicy(UserTaskMetadata metadata, String struct, Element element) {
		AssignNumber assignNumber = null; 
		boolean isDynamic = !"false".equalsIgnoreCase(element.attributeValue("isDynamic"));
		if(isDynamic) 
			assignNumber = parseAssignNumber(metadata, struct, element);
		return new AssignPolicy(isDynamic, assignNumber);
	}
	
	// 解析指派人数表达式
	protected final AssignNumber parseAssignNumber(UserTaskMetadata metadata, String struct, Element element) throws ProcessParseException{
		String assignNum = element.attributeValue("assignNum");
		if(StringUtil.isEmpty(assignNum))
			return null;
		
		AssignNumber assignNumber = assignNumberParser.parse(assignNum);
		if(assignNumber == null)
			throw new ProcessParseException(String.format("<userTask id=%s name=%s>%s<assignPolicy>的assignNum属性值[%s]不合法", metadata.getId(), metadata.getName(), struct, assignNum));
		return assignNumber;
	}
	
	// 设置可指派的用户表达式实体集合
	private void setAssignUserExpressionEntities(UserTaskMetadata metadata, String struct, AssignPolicy assignPolicy, List<Element> elements) {
		if(elements.isEmpty())
			throw new ProcessParseException(String.format("<userTask id=%s name=%s>%s<assignPolicy>下至少配置一个<expression>", metadata.getId(), metadata.getName(), struct));
		
		elements.forEach(element -> {
			assignPolicy.addAssignableUserExpressionEntity(parseAssignableUserExpressionEntity(metadata, struct, element));
		});
	}
	
	// 解析可指派的用户表达式实体
	private AssignableUserExpressionEntity parseAssignableUserExpressionEntity(UserTaskMetadata metadata, String struct, Element element) {
		String expressionName = element.attributeValue("name");
		if(StringUtil.isEmpty(expressionName))
			throw new ProcessParseException(String.format("<userTask id=%s name=%s>%s<assignPolicy><expression>的name属性值不能为空", metadata.getId(), metadata.getName(), struct));
		
		AssignableUserExpression assignUserExpression = assignUserExpressionContainer.get(expressionName);
		if(assignUserExpression == null)
			throw new ProcessParseException(String.format("<userTask id=%s name=%s>%s<assignPolicy><expression>的name属性值[%s]不合法", metadata.getId(), metadata.getName(), struct, expressionName));
		
		String expressionValue = null;
		if(assignUserExpression.valueIsRequired()) {
			expressionValue = element.attributeValue("value");
			if(StringUtil.isEmpty(expressionValue))
				throw new ProcessParseException(String.format("<userTask id=%s name=%s>%s<assignPolicy><expression>的value属性值不能为空", metadata.getId(), metadata.getName(), struct));
			if(!assignUserExpression.validateValue(expressionValue))
				throw new ProcessParseException(String.format("<userTask id=%s name=%s>%s<assignPolicy><expression>的value属性值[%s]不合法", metadata.getId(), metadata.getName(), struct, expressionValue));
		}
		return new AssignableUserExpressionEntity(expressionName, expressionValue);
	}
}
