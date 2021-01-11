package com.douglei.bpm.process.parser.task.user;

import java.util.List;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpression;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpressionContainer;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimPolicy;
import com.douglei.bpm.process.api.user.task.handle.policy.SerialHandleSequencePolicy;
import com.douglei.bpm.process.api.user.task.handle.policy.TaskHandlePolicyContainer;
import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignNumber;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignableUserExpressionEntity;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.ClaimPolicyEntity;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.HandlePolicy;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.MultiHandlePolicyEntity;
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
			throw new ProcessParseException("<userTask id="+id+" name="+name+">下必须配置<candidate>");
		
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
			throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate>下必须配置<assignPolicy>");
		
		AssignNumber assignNumber = null; 
		boolean isDynamic = !"false".equalsIgnoreCase(element.attributeValue("isDynamic"));
		if(isDynamic) 
			assignNumber = parseAssignNumber(id, name, element.attributeValue("assignNum"));
		
		AssignPolicy assignPolicy = new AssignPolicy(isDynamic, assignNumber);
		setAssignUserExpressionEntities(id, name, assignPolicy, element.elements("expression"));
		return assignPolicy;
	}
	// 解析指派人数表达式
	private AssignNumber parseAssignNumber(String id, String name, String assignNum) throws ProcessParseException{
		if(StringUtil.isEmpty(assignNum))
			return null;
		
		if(assignNum.charAt(0) != '-') {
			int percentSignIndex = 0; // 百分号的下标
			while(percentSignIndex < assignNum.length()) {
				if(assignNum.charAt(percentSignIndex) == '%') 
					break;
				percentSignIndex++;
			}
			
			if(percentSignIndex > 0) {
				String str = assignNum.substring(0, percentSignIndex);
				if(VerifyTypeMatchUtil.isInteger(str)) {
					int number = Integer.parseInt(str);
					if(number > 0) {
						if(percentSignIndex == assignNum.length()) // 证明没有%号 
							return new AssignNumber(number, false, false);
						
						if(number <= 100 && (assignNum.length()-percentSignIndex) < 3) { // 大于100%的属于不合法的值; 总长度-百分号下标, 最大不能超过2, 给+/-预留一位
							if(assignNum.length() == percentSignIndex+1) // 没有配置+/-, 所以总长度=百分号下标+1
								return new AssignNumber(number, true, false); 
							
							char c = assignNum.charAt(percentSignIndex+1); // 否则证明配置了+/-, 百分号下标+1取对应的字符进行处理
							if(c == '+')
								return new AssignNumber(number, true, true);
							if(c == '-')
								return new AssignNumber(number, true, false); 
						}
					}
				}
			}
		}
		throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><assignPolicy>的assignNum属性值["+assignNum+"]不合法");
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
			if(!assignUserExpression.validateValue(expressionValue))
				throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><assignPolicy><expression>的value属性值["+expressionValue+"]不合法");
		}
		return new AssignableUserExpressionEntity(expressionName, expressionValue);
	}
	
	// **************************************************
	// 解析办理策略
	// **************************************************
	private HandlePolicy parseHandlePolicy(String id, String name, Element element) {
		if(element == null)
			return null;
		
		return new HandlePolicy(
				Boolean.parseBoolean(element.attributeValue("suggest")), 
				Boolean.parseBoolean(element.attributeValue("attitude")), 
				parseClaimPolicyEntity(id, name, element.element("claim")),
				parseMultiHandlePolicyEntity(id, name, element.element("multiple")));
	}
	// 解析认领策略
	private ClaimPolicyEntity parseClaimPolicyEntity(String id, String name, Element element) {
		if(element == null)
			return null;
		
		String policyName = element.attributeValue("name");
		if(StringUtil.isEmpty(policyName))
			throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><handlePolicy><claim>的name属性值不能为空");
		
		ClaimPolicy claimPolicy = taskHandlePolicyContainer.getClaimPolicy(policyName);
		if(claimPolicy == null)
			throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><handlePolicy><claim>的name属性值["+policyName+"]不合法");
		
		String policyValue = null;
		if(claimPolicy.isValueRequired()) {
			policyValue = element.attributeValue("value");
			if(StringUtil.isEmpty(policyValue))
				throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><handlePolicy><claim>的value属性值不能为空");
			if(!claimPolicy.validateValue(policyValue))
				throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><handlePolicy><claim>的value属性值["+policyValue+"]不合法");
		}
		return new ClaimPolicyEntity(policyName, policyValue);
	}
	// 解析多人办理策略
	private MultiHandlePolicyEntity parseMultiHandlePolicyEntity(String id, String name, Element element) {
		if(element == null)
			return null;
		
		if(Boolean.parseBoolean(element.attributeValue("serialHandle"))) {
			// 当是串行办理时, 获取串行办理任务时的办理顺序策略名称, 并对其进行验证
			String serialHandleSequencePolicyName = element.attributeValue("serialHandleSequence");
			if(StringUtil.isEmpty(serialHandleSequencePolicyName)) {
				serialHandleSequencePolicyName = SerialHandleSequencePolicy.DEFAULT_POLICY_NAME;
			}else if(taskHandlePolicyContainer.getSerialHandleSequencePolicy(serialHandleSequencePolicyName) == null){
				throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><handlePolicy><multiple>的serialHandleSequence属性值["+serialHandleSequencePolicyName+"]不合法");
			}
			return new MultiHandlePolicyEntity(true, serialHandleSequencePolicyName); 
		}
		return new MultiHandlePolicyEntity(false, null);
	}
}
