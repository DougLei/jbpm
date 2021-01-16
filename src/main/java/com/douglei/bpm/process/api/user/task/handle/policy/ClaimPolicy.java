package com.douglei.bpm.process.api.user.task.handle.policy;

import java.util.List;

import com.douglei.bpm.bean.annotation.MultiInstance;
import com.douglei.bpm.module.runtime.task.Assignee;

/**
 * 任务认领策略
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
public interface ClaimPolicy {

	/**
	 * 获取策略名称, 必须唯一; 默认值为类名全路径
	 * @return
	 */
	default String getName() {
		return getClass().getName();
	}
	
	/**
	 * 流程xml配置文件中, userTask -> candidate -> handlePolicy -> claim里的value属性值是否必须配置; 默认值为true
	 * <p>
	 * 当值为true时, 引擎在解析表达式时, 会取配置的value值, 并验证是否为空; 反之引擎会忽略value值
	 * @return
	 */
	default boolean isValueRequired() {
		return true;
	}
	
	/**
	 * 验证value值是否合法; 默认值为true
	 * <p>
	 * 当 isValueRequired() 返回true时, 引擎会调用该方法去验证value值是否合法
	 * @param value
	 * @return
	 */
	default boolean validateValue(String value) {
		return true;
	}
	
	/**
	 * 认领验证
	 * @param value 配置的表达式值, 即流程xml配置文件中, userTask -> candidate -> handlePolicy -> claim里的value属性值
	 * @param currentClaimUserId 当前进行认领的用户id
	 * @param currentAssigneeList 当前进行认领的用户的指派信息集合
	 * @param unclaimAssigneeList 未认领的指派信息集合, HandleState = [UNCLAIM]; 集合中可能包含多个currentClaimUserId的指派信息; 该集合参数不会为null
	 * @param claimedAssigneeList 已经认领的指派信息集合, HandleState = [CLAIMED]; 该集合参数可能为null
	 * @param finishedAssigneeList 已办理完成的指派信息集合, HandleState = [FINISHED]; 集合中可能包含多个currentClaimUserId的指派信息; 该集合参数可能为null
	 * @return 不能认领时, 使用 #{link ClaimResult.CAN_NOT_CLAIM} 常量
	 */
	ClaimResult claimValidate(String value, String currentClaimUserId, List<Assignee> currentAssigneeList, List<Assignee> unclaimAssigneeList, List<Assignee> claimedAssigneeList, List<Assignee> finishedAssigneeList);
}
