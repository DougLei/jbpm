package bpm.test;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.repository.type.ProcessType;

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
		ExecutionResult result = engine.getRepositoryModule().getTypeService().insert(type);
		
		if(result.isSuccess())
			System.out.println("流程类型插入成功: id=" + type.getId());
		else
			System.out.println("流程类型插入失败: " + result.getFailMessage());
	}
	
	@Test
	public void update() {
		ProcessType type = new ProcessType();
		type.setId(4);
		type.setCode("test_code2");
		type.setName("测试类型xxxx");
		ExecutionResult result = engine.getRepositoryModule().getTypeService().update(type);
		
		if(result.isSuccess())
			System.out.println("流程类型修改成功: id=" + type.getId());
		else
			System.out.println("流程类型修改失败: " + result.getFailMessage());
	}
	
	@Test
	public void delete() {
		int typeId = 3;
		ExecutionResult result = engine.getRepositoryModule().getTypeService().delete(typeId, false);
		
		if(result.isSuccess())
			System.out.println("流程类型删除成功: id=" + typeId);
		else
			System.out.println("流程类型删除失败: " + result.getFailMessage());
	}
}
