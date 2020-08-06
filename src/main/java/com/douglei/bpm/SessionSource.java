package com.douglei.bpm;

import com.douglei.orm.sessionfactory.SessionFactory;
import com.douglei.orm.sessionfactory.sessions.Session;

/**
 * 
 * @author DougLei
 */
public class SessionSource {
	private SessionFactory sessionFactory;
	
	SessionSource(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
	public Session getSession() {
		return sessionFactory.openSession();
	}
}
