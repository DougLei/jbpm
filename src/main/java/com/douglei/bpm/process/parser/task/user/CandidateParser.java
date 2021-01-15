package com.douglei.bpm.process.parser.task.user;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimPolicy;
import com.douglei.bpm.process.api.user.task.handle.policy.TaskHandlePolicyContainer;
import com.douglei.bpm.process.api.user.task.handle.policy.impl.SerialHandleByClaimTimeSequencePolicy;
import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.ClaimPolicyEntity;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.HandlePolicy;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.MultiHandlePolicyEntity;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.tools.utils.StringUtil;

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
				serialHandleSequencePolicyName = SerialHandleByClaimTimeSequencePolicy.POLICY_NAME;
			}else if(taskHandlePolicyContainer.getSerialHandleSequencePolicy(serialHandleSequencePolicyName) == null){
				throw new ProcessParseException("<userTask id="+id+" name="+name+"><candidate><handlePolicy><multiple>的serialHandleSequence属性值["+serialHandleSequencePolicyName+"]不合法");
			}
			return new MultiHandlePolicyEntity(true, serialHandleSequencePolicyName); 
		}
		return new MultiHandlePolicyEntity(false, null);
	}
}
