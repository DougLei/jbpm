package com.douglei.bpm.process.api.user.task.handle.policy.impl;

import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.task.handle.policy.DispatchPolicy;

/**
 * 最后办理人进行调度(的策略)
 * @author DougLei
 */
@Bean(clazz = DispatchPolicy.class)
public class LastHandleUserDispatchPolicy implements DispatchPolicy{
	public static final String NAME = "lastHandleUser";
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getUserId(String lastHandleUserId, List<String> handledUserIds) {
		return lastHandleUserId;
	}
}
