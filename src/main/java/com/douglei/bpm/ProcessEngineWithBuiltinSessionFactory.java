package com.douglei.bpm;

import com.douglei.orm.context.SessionFactoryContainer;
import com.douglei.orm.mapping.handler.MappingHandlerException;

/**
 * 内置SessionFactory的流程引擎, 在销毁时必须销毁SessionFactory
 * @author DougLei
 */
class ProcessEngineWithBuiltinSessionFactory extends ProcessEngine {

	ProcessEngineWithBuiltinSessionFactory(String id) {
		super(id);
	}

	@Override
	public void destroy() throws MappingHandlerException {
		SessionFactoryContainer.getSingleton().remove(id, true); // 通过外部数据源创建的流程引擎, 在销毁时, 可以直接销毁
		super.destroy();
	}
}
