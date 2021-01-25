package com.douglei.bpm.process.api.user.task.handle.policy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.CustomAutowired;
import com.douglei.bpm.bean.annotation.Bean;

/**
 * 任务办理策略容器
 * @author DougLei
 */
@Bean
public class TaskHandlePolicyContainer implements CustomAutowired{
	private Map<String, ClaimPolicy> claimPolicyMap = new HashMap<String, ClaimPolicy>();
	private Map<String, SerialHandlePolicy> serialHandlePolicyMap = new HashMap<String, SerialHandlePolicy>();
	
	@SuppressWarnings("unchecked")
	@Override
	public void setFields(Map<Class<?>, Object> beanContainer) {
		((List<ClaimPolicy>)beanContainer.get(ClaimPolicy.class)).forEach(policy -> {
			claimPolicyMap.put(policy.getName(), policy);
		});
		((List<SerialHandlePolicy>)beanContainer.get(SerialHandlePolicy.class)).forEach(policy -> {
			serialHandlePolicyMap.put(policy.getName(), policy);
		});
	}
	
	/**
	 * 获取指定name的任务认领策略
	 * @param name
	 * @return
	 */
	public ClaimPolicy getClaimPolicy(String name) {
		return claimPolicyMap.get(name);
	}
	
	/**
	 * 获取指定name的串行办理策略
	 * @param name
	 * @return
	 */
	public SerialHandlePolicy getSerialHandlePolicy(String name) {
		return serialHandlePolicyMap.get(name);
	}
}
