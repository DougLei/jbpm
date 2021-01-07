package com.douglei.bpm.process.parser.task.user;

import java.util.List;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpression;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpressionContainer;
import com.douglei.bpm.process.api.user.task.handle.policy.TaskHandlePolicyContainer;
import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.candidate.DefaultAssignPolicy;
import com.douglei.bpm.process.metadata.task.user.candidate.DefaultCandidate;
import com.douglei.bpm.process.metadata.task.user.candidate.DefaultHandlePolicy;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignableUserExpressionEntity;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.HandleNumber;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.HandlePolicy;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.MultiHandlePolicy;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.tools.utils.StringUtil;
import com.douglei.tools.utils.datatype.VerifyTypeMatchUtil;

/**
 * 
 * @author DougLei
 */
@Bean
public class CandidateParser {
	
	@Autowired
	private AssignableUserExpressionContainer assignUserExpressionContainer;
	
	@Autowired
	private TaskHandlePolicyContainer taskHandlePolicyContainer;
	
	/**
	 * 解析候选人配置
	 * @param id
	 * @param name
	 * @param element
	 * @return
	 * @throws ProcessParseException
	 */
	Candidate parse(String id, String name, Element element) throws ProcessParseException{
		if(element == null) 
			return DefaultCandidate.getSingleton();
		
		AssignPolicy assignPolicy = parseAssignPolicy(id, name, element.element("assignPolicy"));
		HandlePolicy handlePolicy = parseHandlePolicy(id, name, element.element("handlePolicy"));
		return new Candidate(assignPolicy, handlePolicy);
	}
	
	// **************************************************
	// 解析指派策略
	// **************************************************
	@SuppressWarnings("unchecked")
	private AssignPolicy parseAssignPolicy(String id, String name, Element element) {
		if(element == null)
			return DefaultAssignPolicy.getSingleton();
		
		// 解析是否是动态指派
		String str = element.attributeValue("isDynamic");
		boolean isDynamic = StringUtil.isEmpty(str) || !str.equalsIgnoreCase("false");
		
		AssignPolicy assignPolicy = new AssignPolicy(
				isDynamic, 
				isDynamic?parseHandleNumber(element.attributeValue("assignNum"), "<userTask id="+id+" name="+name+"><candidate><assignPolicy>的assignNum属性值[%s]不合法"):null);
		setAssignUserExpressionEntities(id, name, assignPolicy, element.elements("expression"));
		return assignPolicy;
	}
	// 设置可指派的用户表达式实体集合
	private void setAssignUserExpressionEntities(String id, String name, AssignPolicy assignPolicy, List<Element> elements) {
		if(elements.isEmpty())
			throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><assignPolicy>下至少配置一个<expression>");
		
		elements.forEach(element -> {
			assignPolicy.addAssignableUserExpressionEntity(parseAssignableUserExpressionEntity(id, name, element));
		});
	}
	// 解析可指派的用户表达式实体
	private AssignableUserExpressionEntity parseAssignableUserExpressionEntity(String id, String name, Element element) {
		String expressionName = element.attributeValue("name");
		if(StringUtil.isEmpty(expressionName))
			throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><assignPolicy><expression>的name属性值不能为空");
		
		AssignableUserExpression assignUserExpression = assignUserExpressionContainer.get(expressionName);
		if(assignUserExpression == null)
			throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><assignPolicy><expression>的name属性值["+expressionName+"]不合法");
		
		String expressionValue = null;
		if(assignUserExpression.isValueRequired()) {
			expressionValue = element.attributeValue("value");
			if(StringUtil.isEmpty(expressionValue))
				throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><assignPolicy><expression>的value属性值不能为空");
			if(assignUserExpression.validateValue(expressionValue))
				throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><assignPolicy><expression>的value属性值["+expressionValue+"]不合法");
		}
		return new AssignableUserExpressionEntity(expressionName, expressionValue, element.attributeValue("extendValue"));
	}
	
	// **************************************************
	// 解析办理策略
	// **************************************************
	private HandlePolicy parseHandlePolicy(String id, String name, Element element) {
		if(element == null)
			return DefaultHandlePolicy.getSingleton();
		return new HandlePolicy(
				Boolean.parseBoolean(element.attributeValue("suggest")), 
				Boolean.parseBoolean(element.attributeValue("attitude")), 
				parseMultiHandlePolicy(id, name, element.element("multiple")));
	}
	// 解析多人办理策略
	private MultiHandlePolicy parseMultiHandlePolicy(String id, String name, Element element) {
		if(element == null)
			return null;
		
		// 获取办理人数表达式
		HandleNumber handleNumber = parseHandleNumber(element.attributeValue("handleNum"), "<userTask id="+id+" name="+name+"><candidate><handlePolicy><multiple>的handleNum属性值[%s]不合法");
		
		// 获取(判断)任务是否可以结束的策略名称, 并对其进行验证
		String canFinishPolicyName = element.attributeValue("canFinish");
		if(StringUtil.notEmpty(canFinishPolicyName) && taskHandlePolicyContainer.getCanFinishPolicy(canFinishPolicyName) == null)
			throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><handlePolicy><multiple>的canFinish属性值["+canFinishPolicyName+"]不合法");
		
		if(Boolean.parseBoolean(element.attributeValue("serialHandle"))) {
			// 当是串行办理时, 获取串行办理任务时的办理顺序策略名称, 并对其进行验证
			String serialHandleSequencePolicyName = element.attributeValue("serialHandleSequence");
			if(StringUtil.notEmpty(serialHandleSequencePolicyName) && taskHandlePolicyContainer.getSerialHandleSequencePolicy(serialHandleSequencePolicyName) == null)
				throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><handlePolicy><multiple>的serialHandleSequence属性值["+serialHandleSequencePolicyName+"]不合法");
			
			return new MultiHandlePolicy(handleNumber, true, serialHandleSequencePolicyName, canFinishPolicyName); 
		}
		return new MultiHandlePolicy(handleNumber, false, null, canFinishPolicyName);
	}
	// 解析办理/指派人数表达式, (HandleNumber extends AssignNumber)
	private HandleNumber parseHandleNumber(String configNum, String errorMessage) throws ProcessParseException{
		if(StringUtil.isEmpty(configNum))
			return null;
		
		if(configNum.charAt(0) != '-') {
			int percentSignIndex = 0; // 百分号的下标
			while(percentSignIndex < configNum.length()) {
				if(configNum.charAt(percentSignIndex) == '%') 
					break;
				percentSignIndex++;
			}
			
			if(percentSignIndex > 0) {
				String str = configNum.substring(0, percentSignIndex);
				if(VerifyTypeMatchUtil.isInteger(str)) {
					int number = Integer.parseInt(str);
					if(number > 0) {
						if(percentSignIndex == configNum.length()) // 证明没有%号 
							return new HandleNumber(number, false, null);
						
						if(number <= 100 && (configNum.length()-percentSignIndex) < 3) { // 大于100%的属于不合法的值; 总长度-百分号下标, 最大不能超过2, 给+/-预留一位
							if(configNum.length() == percentSignIndex+1) // 没有配置+/-, 所以总长度=百分号下标+1
								return new HandleNumber(number, true, null); 
							
							char c = configNum.charAt(percentSignIndex+1); // 否则证明配置了+/-, 百分号下标+1取对应的字符进行处理
							if(c == '+')
								return new HandleNumber(number, true, number==100?null:true);
							if(c == '-')
								return new HandleNumber(number, true, number==100?null:false); 
						}
					}
				}
			}
		}
		throw new ProcessParseException(String.format(errorMessage, configNum));
	}
}
