package com.douglei.bpm.process.api.user.assignable.expression;

import java.util.List;

import com.douglei.bpm.bean.annotation.MultiInstance;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;

/**
 * 可指派的用户表达式
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
public interface AssignableUserExpression {
	
	/**
	 * 获取表达式名称, 必须唯一; 默认值为类名全路径
	 * @return
	 */
	default String getName() {
		return getClass().getName();
	}
	
	/**
	 * 流程xml配置文件中, userTask -> candidate -> assignPolicy -> expression里的value属性值是否必须配置; 默认值为true
	 * <p>
	 * 当值为true时, 引擎在解析表达式时, 会取配置的value值, 并验证是否为空; 反之引擎会忽略value值
	 * @return
	 */
	default boolean valueIsRequired() {
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
	 * 获取具体指派的用户集合
	 * @param value  配置的表达式值, 即流程xml配置文件中, userTask -> candidate -> assignPolicy -> expression里的value属性值
	 * @param parameter 
	 * @return 
	 */
	List<UserBean> getAssignUserList(String value, AssignableUserExpressionParameter parameter);
}
