package bpm.test;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
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
		type.setCode("t_code");
		type.setName("测试类型");
		type.setTenantId("douglei");
		type = engine.getRepositoryModule().getTypeService().insert(type);
		System.out.println("流程类型插入成功: id=" + type.getId());
	}
	
	@Test
	public void update() {
		ProcessType type = new ProcessType();
		type.setId(4);
		type.setCode("test_code2");
		type.setName("测试类型xxxx");
		type = engine.getRepositoryModule().getTypeService().update(type);
		System.out.println("流程类型修改成功: id=" + type.getId());
	}
	
	@Test
	public void delete() {
		int typeId = 3;
		engine.getRepositoryModule().getTypeService().delete(typeId, false);
		System.out.println("流程类型删除成功: id=" + typeId);
	}
}
