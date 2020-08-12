package bpm.test;

import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.orm.context.SessionFactoryRegister;
import com.douglei.orm.sessionfactory.SessionFactory;

public class Test1 {
	
	@Test
	public void init() {
		ProcessEngineBuilder peBuilder = new ProcessEngineBuilder();
		SessionFactory factory = peBuilder.createSessionFactory();
		new SessionFactoryRegister().registerSessionFactory(factory);
		
		ProcessEngine engine = peBuilder.build(factory);
		System.out.println(engine);
	}
}
