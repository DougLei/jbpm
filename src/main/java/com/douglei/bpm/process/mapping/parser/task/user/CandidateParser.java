package com.douglei.bpm.process.mapping.parser.task.user;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimPolicy;
import com.douglei.bpm.process.api.user.task.handle.policy.TaskHandlePolicyContainer;
import com.douglei.bpm.process.api.user.task.handle.policy.impl.SerialHandleByClaimTimePolicy;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
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
	 * @param metadata
	 * @param element
	 * @return
	 * @throws ProcessParseException
	 */
	Candidate parse(UserTaskMetadata metadata, Element element) throws ProcessParseException{
		if(element == null) 
			throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+">下必须配置<candidate>");
		
		AssignPolicy assignPolicy = assignPolicyParser.parse(metadata, "<candidate>", element);
		HandlePolicy handlePolicy = parseHandlePolicy(metadata, element.element("handlePolicy"));
		return new Candidate(assignPolicy, handlePolicy);
	}
	
	// **************************************************
	// 解析办理策略
	// **************************************************
	private HandlePolicy parseHandlePolicy(UserTaskMetadata metadata, Element element) {
		if(element == null)
			return null;
		
		return new HandlePolicy(
				"true".equalsIgnoreCase(element.attributeValue("suggest")), 
				"true".equalsIgnoreCase(element.attributeValue("attitude")), 
				parseClaimPolicyEntity(metadata, element.element("claim")),
				parseSerialHandlePolicyEntity(metadata, element.element("serialHandle")),
				parseDispatchPolicyEntity(metadata, element.element("dispatch")));
	}
	// 解析认领策略
	private ClaimPolicyEntity parseClaimPolicyEntity(UserTaskMetadata metadata, Element element) {
		if(element == null)
			return null;
		
		String policyName = element.attributeValue("name");
		if(StringUtil.isEmpty(policyName))
			throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+"><candidate><handlePolicy><claim>的name属性值不能为空");
		
		ClaimPolicy claimPolicy = taskHandlePolicyContainer.getClaimPolicy(policyName);
		if(claimPolicy == null)
			throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+"><candidate><handlePolicy><claim>的name属性值["+policyName+"]不合法");
		
		String policyValue = null;
		if(claimPolicy.valueIsRequired()) {
			policyValue = element.attributeValue("value");
			if(StringUtil.isEmpty(policyValue))
				throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+"><candidate><handlePolicy><claim>的value属性值不能为空");
			if(!claimPolicy.validateValue(policyValue))
				throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+"><candidate><handlePolicy><claim>的value属性值["+policyValue+"]不合法");
		}
		return new ClaimPolicyEntity(policyName, policyValue);
	}
	// 解析串行办理策略
	private SerialHandlePolicyEntity parseSerialHandlePolicyEntity(UserTaskMetadata metadata, Element element) {
		if(element == null)
			return null;
		
		// 获取串行办理的策略名称, 并对其进行验证
		String policyName = element.attributeValue("name");
		if(StringUtil.isEmpty(policyName)) {
			policyName = SerialHandleByClaimTimePolicy.NAME;
		}else if(taskHandlePolicyContainer.getSerialHandlePolicy(policyName) == null){
			throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+"><candidate><handlePolicy><serialHandle>的name属性值["+policyName+"]不合法");
		}
		return new SerialHandlePolicyEntity(policyName); 
	}
	// 解析调度策略
	private DispatchPolicyEntity parseDispatchPolicyEntity(UserTaskMetadata metadata, Element element) {
		if(element == null)
			return null; 
		
		// 获取调度策略名称, 并对其进行验证
		String policyName = element.attributeValue("name");
		if(StringUtil.isEmpty(policyName)) 
			return null;
		
		if(taskHandlePolicyContainer.getDispatchPolicy(policyName) == null)
			throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+"><candidate><handlePolicy><dispatch>的name属性值["+policyName+"]不合法");
		return new DispatchPolicyEntity(policyName); 
	}
}
