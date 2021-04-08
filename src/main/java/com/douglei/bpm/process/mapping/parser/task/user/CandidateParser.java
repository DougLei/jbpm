package com.douglei.bpm.process.mapping.parser.task.user;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimPolicy;
import com.douglei.bpm.process.api.user.task.handle.policy.TaskHandlePolicyContainer;
import com.douglei.bpm.process.api.user.task.handle.policy.impl.SerialHandleByClaimTimePolicy;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.handle.ClaimPolicyEntity;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.handle.DispatchPolicyEntity;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.handle.HandlePolicy;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.handle.SerialHandlePolicyEntity;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;
import com.douglei.tools.StringUtil;

/**
 * 
 * @author DougLei
 */
@Bean
public class CandidateParser {
	
	@Autowired
	private TaskHandlePolicyContainer taskHandlePolicyContainer;
	
	@Autowired
	private AssignPolicyParser assignPolicyParser;
	
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
		
		AssignPolicy assignPolicy = assignPolicyParser.parse(id, name, "<candidate>", element);
		HandlePolicy handlePolicy = parseHandlePolicy(id, name, element.element("handlePolicy"));
		return new Candidate(assignPolicy, handlePolicy);
	}
	
	// **************************************************
	// 解析办理策略
	// **************************************************
	private HandlePolicy parseHandlePolicy(String id, String name, Element element) {
		if(element == null)
			return null;
		
		return new HandlePolicy(
				"true".equalsIgnoreCase(element.attributeValue("suggest")), 
				"true".equalsIgnoreCase(element.attributeValue("attitude")), 
				parseClaimPolicyEntity(id, name, element.element("claim")),
				parseSerialHandlePolicyEntity(id, name, element.element("serialHandle")),
				parseDispatchPolicyEntity(id, name, element.element("dispatch")));
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
		if(claimPolicy.valueIsRequired()) {
			policyValue = element.attributeValue("value");
			if(StringUtil.isEmpty(policyValue))
				throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><handlePolicy><claim>的value属性值不能为空");
			if(!claimPolicy.validateValue(policyValue))
				throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><handlePolicy><claim>的value属性值["+policyValue+"]不合法");
		}
		return new ClaimPolicyEntity(policyName, policyValue);
	}
	// 解析串行办理策略
	private SerialHandlePolicyEntity parseSerialHandlePolicyEntity(String id, String name, Element element) {
		if(element == null)
			return null;
		
		// 获取串行办理的策略名称, 并对其进行验证
		String policyName = element.attributeValue("name");
		if(StringUtil.isEmpty(policyName)) {
			policyName = SerialHandleByClaimTimePolicy.NAME;
		}else if(taskHandlePolicyContainer.getSerialHandlePolicy(policyName) == null){
			throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><handlePolicy><serialHandle>的name属性值["+policyName+"]不合法");
		}
		return new SerialHandlePolicyEntity(policyName); 
	}
	// 解析调度策略
	private DispatchPolicyEntity parseDispatchPolicyEntity(String id, String name, Element element) {
		if(element == null)
			return null; 
		
		// 获取调度策略名称, 并对其进行验证
		String policyName = element.attributeValue("name");
		if(StringUtil.isEmpty(policyName)) 
			return null;
		
		if(taskHandlePolicyContainer.getDispatchPolicy(policyName) == null)
			throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><handlePolicy><dispatch>的name属性值["+policyName+"]不合法");
		return new DispatchPolicyEntity(policyName); 
	}
}
