package com.douglei.bpm;

import com.douglei.orm.context.SessionFactoryContainer;
import com.douglei.orm.mapping.handler.MappingHandlerException;

/**
 * 内置SessionFactory的流程引擎, 在销毁时必须销毁SessionFactory
 * @author DougLei
 */
class ProcessEngineByBuiltinSessionFactory extends ProcessEngine {

	ProcessEngineByBuiltinSessionFactory(String id) {
		super(id);
	}

	@Override
	public void destroy() throws MappingHandlerException {
		SessionFactoryContainer.getSingleton().remove(id, true);
	}
}
