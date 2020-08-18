package bpm.test;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.repository.type.ProcessType;
import com.douglei.i18n.I18nContext;
import com.douglei.orm.context.SessionFactoryRegister;
import com.douglei.orm.core.metadata.validator.ValidationResult;
import com.douglei.orm.sessionfactory.SessionFactory;

public class ProcessTypeTest {
	private ProcessEngine engine;
	
	@Before
	public void init() {
		ProcessEngineBuilder peBuilder = new ProcessEngineBuilder();
		SessionFactory factory = peBuilder.createSessionFactory();
		new SessionFactoryRegister().registerSessionFactory(factory);
		engine = peBuilder.build(factory);
	}
	
	@Test
	public void save() {
		ProcessType type = new ProcessType();
		type.setCode("test_code1");
		type.setName("测试类型");
		type.setCreateDate(new Date());
		ValidationResult result = engine.getRepository().getProcessTypeService().save(type);
		System.out.println(result);
		System.out.println(I18nContext.getMessage(result));
	}
	
	@Test
	public void edit() {
		ProcessType type = new ProcessType();
		type.setId(1);
		type.setCode("aa");
		type.setName("测试类型1111111111111111");
		type.setUpdateDate(new Date());
		System.out.println(engine.getRepository().getProcessTypeService().edit(type));
	}
	
	@Test
	public void delete() {
		ProcessType type = new ProcessType();
		type.setId(3);
		System.out.println(engine.getRepository().getProcessTypeService().delete(type));
	}
}
