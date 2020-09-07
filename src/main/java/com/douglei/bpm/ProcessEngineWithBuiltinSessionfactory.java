package com.douglei.bpm;

import com.douglei.orm.context.SessionFactoryRegister;

/**
 * 内置SessionFactory的流程引擎, 在销毁时必须销毁SessionFactory
 * @author DougLei
 */
public class ProcessEngineWithBuiltinSessionfactory extends ProcessEngine {

	ProcessEngineWithBuiltinSessionfactory(String id) {
		super(id);
	}

	@Override
	public void destroy() {
		SessionFactoryRegister.getSingleton().remove(id, true); // 通过外部数据源创建的流程引擎, 在销毁时, 可以直接销毁
	}
}
