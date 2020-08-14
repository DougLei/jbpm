package bpm.test;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.engine.ProcessEngine;
import com.douglei.bpm.engine.ProcessEngineBuilder;
import com.douglei.bpm.module.repository.type.ProcessTypeEntity;
import com.douglei.i18n.Message;
import com.douglei.orm.context.SessionFactoryRegister;
import com.douglei.orm.sessionfactory.SessionFactory;

public class Test1 {
	private ProcessEngine engine;
	
	@Before
	public void init() {
		ProcessEngineBuilder peBuilder = new ProcessEngineBuilder();
		SessionFactory factory = peBuilder.createSessionFactory();
		new SessionFactoryRegister().registerSessionFactory(factory);
		engine = peBuilder.build(factory);
	}
	
	@Test
	public void testProcessTypeService() {
		ProcessTypeEntity type = new ProcessTypeEntity();
		type.setCode("aa");
		type.setName("测试类型");
		type.setCreateDate(new Date());
		
		Message message = engine.getRepository().getProcessTypeService().save(type);
		System.out.println(message.getMessage());
	}
}
