package com.douglei.bpm.process.api.user.task.handle.policy;

import java.util.List;

import com.douglei.bpm.bean.annotation.MultiInstance;

/**
 * 任务调度策略
 * <p>
 * 
 * 如果需要访问数据库, 可直接调用以下方法:
 * <pre>
 * 	SessionContext.getSqlSession()
 * 	SessionContext.getTableSession()
 * 	SessionContext.getSQLSession()
 * 	SessionContext.getSQLQuerySession()
 * </pre>
 * 
 * @author DougLei
 */
@MultiInstance
public interface DispatchPolicy {
	
	/**
	 * 获取策略名称, 必须唯一; 默认值为类名全路径
	 * @return
	 */
	default String getName() {
		return getClass().getName();
	}
	
	/**
	 * 获取可进行任务调度的userId
	 * @param lastHandleUserId 最后办理的用户id
	 * @param handledUserIds 办理过当前任务的用户id集合(包含最后办理的用户id)
	 * @return 
	 */
	String getUserId(String lastHandleUserId, List<String> handledUserIds);
}
