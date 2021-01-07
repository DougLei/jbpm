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
	private Map<String, CanFinishPolicy> canFinishPolicyMap = new HashMap<String, CanFinishPolicy>();
	private Map<String, SerialHandleSequencePolicy> serialHandleSequencePolicyMap = new HashMap<String, SerialHandleSequencePolicy>();
	
	@SuppressWarnings("unchecked")
	@Override
	public void setFields(Map<Class<?>, Object> beanContainer) {
		((List<CanFinishPolicy>)beanContainer.get(CanFinishPolicy.class)).forEach(policy -> {
			canFinishPolicyMap.put(policy.getName(), policy);
		});
		((List<SerialHandleSequencePolicy>)beanContainer.get(SerialHandleSequencePolicy.class)).forEach(policy -> {
			serialHandleSequencePolicyMap.put(policy.getName(), policy);
		});
	}
	
	/**
	 * 获取指定name的策略
	 * <p>
	 * (判断)任务是否可以结束的策略
	 * @param name
	 * @return
	 */
	public CanFinishPolicy getCanFinishPolicy(String name) {
		return canFinishPolicyMap.get(name);
	}
	
	/**
	 * 获取指定name的策略
	 * <p>
	 * 串行办理任务时的办理顺序策略
	 * @param name
	 * @return
	 */
	public SerialHandleSequencePolicy getSerialHandleSequencePolicy(String name) {
		return serialHandleSequencePolicyMap.get(name);
	}
}
