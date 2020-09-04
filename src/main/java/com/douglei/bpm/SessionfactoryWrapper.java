package com.douglei.bpm;

import com.douglei.orm.context.SessionFactoryRegister;
import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * Sessionfactory包装器
 * @author DougLei
 */
public class SessionfactoryWrapper {
	private SessionFactoryRegister register;
	
	private SessionFactory sessionFactory;
	private boolean isPrivate; // sessionFactory是否是ProcessEngine私有的
	
	public SessionfactoryWrapper(SessionFactoryRegister register, SessionFactory sessionFactory, boolean isPrivate) {
		this.register = register;
		this.sessionFactory = sessionFactory;
		this.isPrivate = isPrivate;
		
		register.registerSessionFactory(sessionFactory);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public String getId() {
		return sessionFactory.getId();
	}

	public void destroy() {
		if(isPrivate)
			register.destroySessionFactory(sessionFactory.getId());
	}
}
