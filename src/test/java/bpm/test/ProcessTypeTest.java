package bpm.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.repository.type.ProcessType;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.impl.AbstractParameter;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.impl.LogicalOperator;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.impl.Operator;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.impl.Parameter;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.impl.ParameterGroup;

public class ProcessTypeTest {
	private ProcessEngine engine;
	
	@Before
	public void init() {
		engine = new ProcessEngineBuilder().build();
	}
	
	@Test
	public void query() {
		List<AbstractParameter> parameters = new ArrayList<AbstractParameter>();
		parameters.add(new Parameter(false, Operator.EQ, "NAME", "病假", "产假").setNext(LogicalOperator.OR));
		
		ParameterGroup group = new ParameterGroup();
		group.addParameter(new Parameter(false, Operator.EQ, "CODE", "nianjia").setNext(LogicalOperator.OR).setNext(LogicalOperator.AND));
		group.addParameter(new Parameter(false, Operator.EQ, "id", 5));
		parameters.add(group);
		
		List<ProcessType> list = engine.getRepositoryModule().getTypeService().query(parameters);
		for (ProcessType processType : list) {
			System.out.println(processType.getId() + "\t" + processType.getName());
		}
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
			System.out.println("流程类型插入失败: " + result.getMessage());
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
			System.out.println("流程类型修改失败: " + result.getMessage());
	}
	
	@Test
	public void delete() {
		int typeId = 3;
		ExecutionResult result = engine.getRepositoryModule().getTypeService().delete(typeId, false);
		
		if(result.isSuccess())
			System.out.println("流程类型删除成功: id=" + typeId);
		else
			System.out.println("流程类型删除失败: " + result.getMessage());
	}
}
