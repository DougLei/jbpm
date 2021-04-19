package bpm.test;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.repository.type.Type;

public class ProcessTypeTest {
	private ProcessEngine engine;
	
	@Before
	public void init() {
		engine = new ProcessEngineBuilder().build();
	}
	
	@Test
	public void query() {
//		List<AbstractParameter> parameters = new ArrayList<AbstractParameter>();
//		parameters.add(new Parameter(false, Operator.EQ, "NAME", "病假", "产假").setNext(LogicalOperator.OR));
//		
//		ParameterGroup group = new ParameterGroup();
//		group.addParameter(new Parameter(false, Operator.EQ, "CODE", "nianjia").setNext(LogicalOperator.OR).setNext(LogicalOperator.AND));
//		group.addParameter(new Parameter(false, Operator.EQ, "id", 5));
//		parameters.add(group);
		
		
//		UniqueQueryExecutor executor = new UniqueQueryExecutor();
		
//		QueryExecutorImpl executor = new QueryExecutorImpl();
		
//		RecursiveQueryExecutor executor = new RecursiveQueryExecutor(new RecursiveEntity().setValue(0));
		
//		List<AbstractParameter> parameters = new ArrayList<AbstractParameter>();
//		parameters.add(new Parameter(false, Operator.EQ, "code", "qingjia"));
//		
//		Object result = engine.getRepositoryModule().getTypeService().query(executor, parameters);
//		System.out.println(JSONObject.toJSONString(result, SerializerFeature.PrettyFormat));
	}
	
	@Test
	public void save() {
		Type type = new Type();
//		type.setParentId(2);
		type.setCode("baoxiao");
		type.setName("报销");
		type.setTenantId("douglei");
		Result result = engine.getRepositoryModule().getTypeService().insert(type);
		
		if(result.isSuccess())
			System.out.println("流程类型插入成功: id=" + type.getId());
		else
			System.out.println("流程类型插入失败: " + result.getMessage());
	}
	
	@Test
	public void update() {
		Type type = new Type();
		type.setId(3);
		type.setParentId(2);
		type.setCode("chanjia-gai");
		type.setName("产假-改111");
		Result result = engine.getRepositoryModule().getTypeService().update(type);
		
		if(result.isSuccess())
			System.out.println("流程类型修改成功: id=" + type.getId());
		else
			System.out.println("流程类型修改失败: " + result.getMessage());
	}
	
	@Test
	public void delete() {
		int typeId = 2;
		Result result = engine.getRepositoryModule().getTypeService().delete(typeId, true);
		
		if(result.isSuccess())
			System.out.println("流程类型删除成功: id=" + typeId);
		else
			System.out.println("流程类型删除失败: " + result.getMessage());
	}
}
