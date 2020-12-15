package com.douglei.bpm.module.runtime.task.assignee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.bean.annotation.DefaultInstance;
import com.douglei.orm.context.SessionContext;

/**
 * 指派信息构建器
 * @author DougLei
 */
@DefaultInstance(clazz = AssignerBuilderImpl.class)
public abstract class AssignerBuilder {

	/**
	 * 根据指派的用户标识, 获取指派信息集合
	 * <p>
	 * 如果需要和数据库交互, 例如查询, 则可以直接使用 {@link SessionContext} 提供的静态方法, 获取和数据库的连接, 具体如下
	 * <pre>
	 * SessionContext.getSession()
	 * SessionContext.getSqlSession()
	 * SessionContext.getTableSession()
	 * SessionContext.getSQLSession()
	 * </pre>
	 * @param assignedUserIds
	 * @return 
	 */
	public final List<Assigner> build(String... assignedUserIds){
		if(assignedUserIds == null || assignedUserIds.length == 0)
			return null;
		return build(Arrays.asList(assignedUserIds));
	}
	
	/**
	 * 同上
	 * @param assignedUserIds
	 * @return
	 */
	public List<Assigner> build(List<String> assignedUserIds) {
		List<Assigner> list = new ArrayList<Assigner>(assignedUserIds.size());
		assignedUserIds.forEach(assignedUserId -> list.add(new Assigner(assignedUserId)));
		return list;
	}
}
