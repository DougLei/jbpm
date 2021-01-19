package com.douglei.bpm.process.parser.task.user;

import java.util.List;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpression;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpressionContainer;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignNumber;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignableUserExpressionEntity;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.tools.utils.StringUtil;

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
	 * @param id 所属userTask的id
	 * @param name 所属userTask的name
	 * @param struct 传入当前解析的xml自有结构
	 * @param candidateElement
	 * @return
	 * @throws ProcessParseException
	 */
	@SuppressWarnings("unchecked")
	public final AssignPolicy parse(String id, String name, String struct, Element candidateElement) throws ProcessParseException{
		Element element = candidateElement.element("assignPolicy");
		if(element == null)
			throw new ProcessParseException(String.format("<userTask id=%s name=%s>%s下必须配置<assignPolicy>", id, name, struct));
		
		AssignPolicy assignPolicy = parseAssignPolicy(id, name, struct, element);
		setAssignUserExpressionEntities(id, name, struct, assignPolicy, element.elements("expression"));
		return assignPolicy;
	}
	
	// 解析指派策略
	protected AssignPolicy parseAssignPolicy(String id, String name, String struct, Element element) {
		AssignNumber assignNumber = null; 
		boolean isDynamic = !"false".equalsIgnoreCase(element.attributeValue("isDynamic"));
		if(isDynamic) 
			assignNumber = parseAssignNumber(id, name, struct, element);
		return createInstance(isDynamic, assignNumber);
	}
	
	// 创建实例
	protected AssignPolicy createInstance(boolean isDynamic, AssignNumber assignNumber) {
		return new AssignPolicy(isDynamic, assignNumber); 
	}

	// 解析指派人数表达式
	private AssignNumber parseAssignNumber(String id, String name, String struct, Element element) throws ProcessParseException{
		String assignNum = element.attributeValue("assignNum");
		if(StringUtil.isEmpty(assignNum))
			return null;
		
		AssignNumber assignNumber = assignNumberParser.parse(assignNum);
		if(assignNumber == null)
			throw new ProcessParseException(String.format("<userTask id=%s name=%s>%s<assignPolicy>的assignNum属性值[%s]不合法", id, name, struct, assignNum));
		return assignNumber;
	}
	
	// 设置可指派的用户表达式实体集合
	private void setAssignUserExpressionEntities(String id, String name, String struct, AssignPolicy assignPolicy, List<Element> elements) {
		if(elements.isEmpty())
			throw new ProcessParseException(String.format("<userTask id=%s name=%s>%s<assignPolicy>下至少配置一个<expression>", id, name, struct));
		
		elements.forEach(element -> {
			assignPolicy.addAssignableUserExpressionEntity(parseAssignableUserExpressionEntity(id, name, struct, element));
		});
	}
	
	// 解析可指派的用户表达式实体
	private AssignableUserExpressionEntity parseAssignableUserExpressionEntity(String id, String name, String struct, Element element) {
		String expressionName = element.attributeValue("name");
		if(StringUtil.isEmpty(expressionName))
			throw new ProcessParseException(String.format("<userTask id=%s name=%s>%s<assignPolicy><expression>的name属性值不能为空", id, name, struct));
		
		AssignableUserExpression assignUserExpression = assignUserExpressionContainer.get(expressionName);
		if(assignUserExpression == null)
			throw new ProcessParseException(String.format("<userTask id=%s name=%s>%s<assignPolicy><expression>的name属性值[%s]不合法", id, name, struct, expressionName));
		
		String expressionValue = null;
		if(assignUserExpression.valueIsRequired()) {
			expressionValue = element.attributeValue("value");
			if(StringUtil.isEmpty(expressionValue))
				throw new ProcessParseException(String.format("<userTask id=%s name=%s>%s<assignPolicy><expression>的value属性值不能为空", id, name, struct));
			if(!assignUserExpression.validateValue(expressionValue))
				throw new ProcessParseException(String.format("<userTask id=%s name=%s>%s<assignPolicy><expression>的value属性值[%s]不合法", id, name, struct, expressionValue));
		}
		return new AssignableUserExpressionEntity(expressionName, expressionValue);
	}
}
