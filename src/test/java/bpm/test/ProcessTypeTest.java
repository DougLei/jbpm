package bpm.test;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.common.service.ExecutionResult;
import com.douglei.bpm.module.repository.type.ProcessType;
import com.douglei.i18n.I18nContext;
import com.douglei.orm.context.IdDuplicateException;

public class ProcessTypeTest {
	private ProcessEngine engine;
	
	@Before
	public void init() throws IdDuplicateException {
		engine = ProcessEngineBuilder.getSingleton().build();
	}
	
	@Test
	public void save() {
		ProcessType type = new ProcessType();
		type.setCode("test_code123");
		type.setName("测试类型");
		type.setCreateDate(new Date());
		ExecutionResult result = engine.getRepository().getTypeService().save(type);
		System.out.println(result);
		System.out.println(I18nContext.getMessage(result));
		System.out.println(type.getId());
	}
	
	@Test
	public void edit() {
		ProcessType type = new ProcessType();
		type.setId(1);
		type.setCode("aa");
		type.setName("测试类型1111111111111111");
		type.setUpdateDate(new Date());
		System.out.println(engine.getRepository().getTypeService().edit(type));
	}
	
	@Test
	public void delete() {
		ProcessType type = new ProcessType();
		type.setId(3);
		System.out.println(engine.getRepository().getTypeService().delete(type, false));
	}
}
