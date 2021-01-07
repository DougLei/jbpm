package com.douglei.bpm.process.api.user.assignable.expression;

import java.util.List;

import com.douglei.bpm.bean.annotation.MultiInstance;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;

/**
 * 可指派的用户表达式
 * @author DougLei
 */
@MultiInstance
public interface AssignableUserExpression {
	ParameterType[] DEFAULT_REQUIRED_PARAMETER_TYPES = { ParameterType.EXPRESSION_VALUE, ParameterType.EXPRESSION_EXTEND_VALUE }; // 默认需要的参数
	
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
	default boolean isValueRequired() {
		return true;
	}
	
	/**
	 * 验证value值是否合法; 默认值为true
	 * <p>
	 * 当 isValueRequired() 返回true时, 引擎会调用该方法去验证value值是否合法
	 * @param expressionValue
	 * @return
	 */
	default boolean validateValue(String expressionValue) {
		return true;
	}
	
	/**
	 * 当前表达式在获取具体指派的用户集合时, 需要的参数; 默认值为: EXPRESSION_VALUE, EXPRESSION_EXTEND_VALUE
	 * <p>
	 * 引擎会将需要的参数封装到 {@link Parameter} 实例中, 并传入 getAssignUserList(Parameter) 方法
	 * @return
	 */
	default ParameterType[] requiredParameters() {
		return DEFAULT_REQUIRED_PARAMETER_TYPES;
	}
	
	/**
	 * 获取具体指派的用户集合
	 * @param parameter
	 * @return 
	 */
	List<UserBean> getAssignUserList(Parameter parameter);
}
