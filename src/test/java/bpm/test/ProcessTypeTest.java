package bpm.test;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.repository.type.entity.ProcessType;

public class ProcessTypeTest {
	private ProcessEngine engine;
	
	@Before
	public void init() {
		engine = new ProcessEngineBuilder().build();
	}
	
	@Test
	public void save() {
		ProcessType type = new ProcessType();
		type.setCode("test_code");
		type.setName("测试类型");
		ExecutionResult<ProcessType> result = engine.getRepositoryModule().getTypeService().save(type);
		System.out.println(result);
		System.out.println(type.getId());
	}
	
	@Test
	public void update() {
		ProcessType type = new ProcessType();
		type.setId(4);
		type.setCode("test_code2");
		type.setName("测试类型xxxx");
		System.out.println(engine.getRepositoryModule().getTypeService().update(type));
	}
	
	@Test
	public void delete() {
		System.out.println(engine.getRepositoryModule().getTypeService().delete(3, false));
	}
}
