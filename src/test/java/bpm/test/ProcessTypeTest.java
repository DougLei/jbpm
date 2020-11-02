package bpm.test;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.common.service.ExecutionResult;
import com.douglei.bpm.module.repository.type.ProcessType;
import com.douglei.i18n.I18nContext;
import com.douglei.orm.context.IdRepeatedException;

public class ProcessTypeTest {
	private ProcessEngine engine;
	
	@Before
	public void init() throws IdRepeatedException {
		engine = new ProcessEngineBuilder().build();
	}
	
	@Test
	public void save() {
		ProcessType type = new ProcessType();
		type.setCode("test_code2");
		type.setName("测试类型");
		type.setCreateDate(new Date());
		ExecutionResult result = engine.getRepositoryModule().getTypeService().save(type);
		System.out.println(result);
		System.out.println(I18nContext.getMessage(result));
		System.out.println(type.getId());
	}
	
	@Test
	public void update() {
		ProcessType type = new ProcessType();
		type.setId(4);
		type.setCode("test_code2");
		type.setName("测试类型xxxx");
		type.setUpdateDate(new Date());
		System.out.println(engine.getRepositoryModule().getTypeService().update(type));
	}
	
	@Test
	public void delete() {
		ProcessType type = new ProcessType();
		type.setId(3);
		System.out.println(engine.getRepositoryModule().getTypeService().delete(type, false));
	}
}
