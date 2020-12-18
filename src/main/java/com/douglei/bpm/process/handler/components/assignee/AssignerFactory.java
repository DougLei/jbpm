package com.douglei.bpm.process.handler.components.assignee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.bean.annotation.DefaultInstance;
import com.douglei.orm.context.SessionContext;

/**
 * 指派人信息工厂
 * @author DougLei
 */
@DefaultInstance(clazz = AssignerFactoryImpl.class)
public abstract class AssignerFactory {

	/**
	 * 根据指派人的id, 获取指派人的信息集合
	 * <p>
	 * 当需要和数据库交互,  则可以直接使用 {@link SessionContext} 提供的静态方法, 获取和数据库的连接, 具体方法如下: 
	 * <pre>
	 * SessionContext.getSession()
	 * SessionContext.getSqlSession()
	 * SessionContext.getTableSession()
	 * SessionContext.getSQLSession()
	 * </pre>
	 * @param assignedUserIds
	 * @return 
	 */
	public final List<Assigner> create(String... assignedUserIds){
		if(assignedUserIds == null || assignedUserIds.length == 0)
			return null;
		return create(Arrays.asList(assignedUserIds));
	}
	
	/**
	 * 同上
	 * @param assignedUserIds
	 * @return
	 */
	public List<Assigner> create(List<String> assignedUserIds) {
		List<Assigner> list = new ArrayList<Assigner>(assignedUserIds.size());
		assignedUserIds.forEach(assignedUserId -> list.add(new Assigner(assignedUserId)));
		return list;
	}
}
