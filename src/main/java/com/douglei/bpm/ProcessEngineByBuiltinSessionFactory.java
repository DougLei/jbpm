package com.douglei.bpm;

import com.douglei.orm.mapping.handler.MappingHandlerException;
import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * 内置SessionFactory的流程引擎, 在销毁时必须销毁SessionFactory
 * @author DougLei
 */
class ProcessEngineByBuiltinSessionFactory extends ProcessEngine {

	ProcessEngineByBuiltinSessionFactory(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public void destroy() throws MappingHandlerException {
		super.sessionFactory.destroy();
	}
}
