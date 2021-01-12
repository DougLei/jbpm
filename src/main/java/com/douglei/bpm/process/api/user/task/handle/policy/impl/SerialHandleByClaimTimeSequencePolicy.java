package com.douglei.bpm.process.api.user.task.handle.policy.impl;

import java.util.Comparator;
import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;
import com.douglei.bpm.process.api.user.task.handle.policy.SerialHandleSequencePolicy;

/**
 * 串行办理时, 根据认领时间正序办理(的策略)
 * @author DougLei
 */
@Bean(clazz = SerialHandleSequencePolicy.class)
public class SerialHandleByClaimTimeSequencePolicy implements SerialHandleSequencePolicy{
	public static final String POLICY_NAME = "byClaimTimeSequence";
	
	@Override
	public String getName() {
		return POLICY_NAME;
	}

	@Override
	public int canHandle(UserBean currentHandleUser, List<Assignee> claimedAssigneeList) {
		claimedAssigneeList.sort(comparator);

		int waitForPersonNumber = 0; // 需要等待的人数
		for (Assignee assignee : claimedAssigneeList) {
			if(assignee.getUserId().equals(currentHandleUser.getUserId()))
				break;
			else
				waitForPersonNumber++;
		}
		return waitForPersonNumber;
	}
	
	// Assignee的排序比较器, 根据认领日期正序排列
	private static final Comparator<Assignee> comparator = new Comparator<Assignee>() {
		@Override
		public int compare(Assignee a1, Assignee a2) {
			if(a1.getClaimTime_() < a2.getClaimTime_())
				return -1;
			if(a1.getClaimTime_() > a2.getClaimTime_())
				return 1;
			return 0;
		}
	};
}
