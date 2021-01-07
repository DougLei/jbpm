package com.douglei.bpm.process.api.user.bean.factory;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author DougLei
 */
public class UserBeanFactoryImpl implements UserBeanFactory{
	
	@Override
	public UserBean create(String userId) {
		if(userId == null)
			return null;
		return new UserBean(userId);
	}
	
	@Override
	public List<UserBean> create(String... userIds) {
		if(userIds == null || userIds.length == 0)
			return new ArrayList<UserBean>();
		
		List<UserBean> users = new ArrayList<UserBean>(userIds.length);
		for (String userId : userIds) 
			users.add(new UserBean(userId));
		return users;
	}
	
	@Override
	public List<UserBean> create(List<String> userIds){
		if(userIds == null || userIds.isEmpty())
			return new ArrayList<UserBean>();
		
		List<UserBean> users = new ArrayList<UserBean>(userIds.size());
		userIds.forEach(userId -> users.add(new UserBean(userId)));
		return users;
	}
}
