package com.douglei.bpm.process.api.user.task.handle.policy.impl;

import java.util.Comparator;
import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.process.api.user.task.handle.policy.SerialHandlePolicy;

/**
 * 串行办理时, 根据认领时间正序办理(的策略)
 * @author DougLei
 */
@Bean(clazz = SerialHandlePolicy.class)
public class SerialHandleByClaimTimePolicy implements SerialHandlePolicy{
	public static final String POLICY_NAME = "byClaimTimeASC";
	
	@Override
	public String getName() {
		return POLICY_NAME;
	}

	@Override
	public int canHandle(String currentHandleUserId, List<Assignee> claimedAssigneeList) {
		claimedAssigneeList.sort(BY_CLAIM_TIME_ASC);

		int waitForPersonNumber = 0; // 需要等待的人数
		for (Assignee assignee : claimedAssigneeList) {
			if(assignee.getUserId().equals(currentHandleUserId))
				break;
			else
				waitForPersonNumber++;
		}
		return waitForPersonNumber;
	}
	
	// Assignee的排序比较器, 根据认领日期正序排列
	private static final Comparator<Assignee> BY_CLAIM_TIME_ASC = new Comparator<Assignee>() {
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
