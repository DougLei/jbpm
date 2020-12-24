package com.douglei.bpm.process.handler;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.bean.annotation.DefaultInstance;

/**
 * 用户信息工厂
 * @author DougLei
 */
@DefaultInstance(clazz = UserFactoryImpl.class)
public abstract class UserFactory {
	
	/**
	 * 创建User实例
	 * @param userId
	 * @return
	 */
	public User create(String userId) {
		if(userId == null)
			return null;
		return new User(userId);
	}
	
	/**
	 * 创建User集合实例
	 * @param userIds
	 * @return
	 */
	public List<User> create(String... userIds) {
		if(userIds == null || userIds.length == 0)
			return new ArrayList<User>();
		
		List<User> users = new ArrayList<User>(userIds.length);
		for (String userId : userIds) 
			users.add(new User(userId));
		return users;
	}
	
	/**
	 * 
	 * @param userIds
	 * @return
	 */
	public List<User> create(List<String> userIds){
		if(userIds == null || userIds.isEmpty())
			return new ArrayList<User>();
		
		List<User> users = new ArrayList<User>(userIds.size());
		userIds.forEach(userId -> users.add(new User(userId)));
		return users;
	}
}
