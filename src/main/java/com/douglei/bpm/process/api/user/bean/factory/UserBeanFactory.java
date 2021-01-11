package com.douglei.bpm.process.api.user.bean.factory;

import java.util.List;

import com.douglei.bpm.bean.annotation.DefaultInstance;
import com.douglei.bpm.process.api.user.bean.factory.impl.UserBeanFactoryImpl;

/**
 * 用户Bean工厂接口
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
@DefaultInstance(clazz = UserBeanFactoryImpl.class)
public interface UserBeanFactory {
	
	/**
	 * 创建单个UserBean实例
	 * @param userId
	 * @return
	 */
	UserBean create(String userId);
	
	/**
	 * 创建UserBean实例集合
	 * @param userIds
	 * @return
	 */
	List<UserBean> create(String... userIds);
	
	/**
	 * 创建UserBean实例集合
	 * @param userIds
	 * @return
	 */
	List<UserBean> create(List<String> userIds);
}
