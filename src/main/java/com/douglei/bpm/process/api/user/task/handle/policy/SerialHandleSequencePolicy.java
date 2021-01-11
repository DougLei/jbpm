package com.douglei.bpm.process.api.user.task.handle.policy;

import java.util.List;

import com.douglei.bpm.bean.annotation.MultiInstance;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;

/**
 * 串行办理的顺序策略
 * <p>
 * 
 * 如果需要访问数据库, 可直接调用以下方法:
 * <pre>
 * 	SessionContext.getSqlSession()
 * 	SessionContext.getTableSession()
 * 	SessionContext.getSQLSession()
 * </pre>
 * 
 * @author DougLei
 */
@MultiInstance
public interface SerialHandleSequencePolicy {
	
	/**
	 * 默认串行办理的顺序策略名称
	 */
	public static final String DEFAULT_POLICY_NAME = "ByClaimTimeSequence";
	
	/**
	 * 获取策略名称, 必须唯一; 默认值为类名全路径
	 * @return
	 */
	default String getName() {
		return getClass().getName();
	}
	
	/**
	 * 能否办理
	 * @param currentHandleUser 当前办理的用户实例
	 * @param claimedAssigneeList 已认领的指派信息集合, 集合中可能包含多个currentHandleUser的信息
	 * @return 需要等待的人数, 返回0表示可以进行办理
	 */
	int canHandle(UserBean currentHandleUser, List<Assignee> claimedAssigneeList);
}
