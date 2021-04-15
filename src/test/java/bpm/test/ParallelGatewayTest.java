package bpm.test;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.Attitude;
import com.douglei.bpm.module.repository.definition.ClasspathFile;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.bpm.module.repository.definition.ProcessDefinitionEntity;
import com.douglei.bpm.module.runtime.instance.ProcessInstance;
import com.douglei.bpm.module.runtime.instance.command.parameter.StartParameter;
import com.douglei.bpm.module.runtime.task.command.parameter.HandleTaskParameter;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;

public class ParallelGatewayTest {
	private ProcessEngine engine;
	
	@Before
	public void init() {
		engine = new ProcessEngineBuilder().build();
	}
	
	@Test
	public void insert() throws ProcessParseException {
		ProcessDefinitionEntity builder = new ProcessDefinitionEntity(new ClasspathFile("bpm/ParallelGateway.bpm.xml"));
		ExecutionResult result = engine.getRepositoryModule().getDefinitionService().insert(builder);
		if(result.isSuccess())
			System.out.println("插入成功一条流程定义信息, 其id为: "+ ((ProcessDefinition)result.getObject()).getId());
		else
			System.out.println(result.getMessage());
	}
	
	@Test
	public void deploy() throws ProcessParseException {
		int processDefinitionId= 1;
		ExecutionResult result = engine.getRepositoryModule().getDefinitionService().deploy(processDefinitionId);
		if(result.isSuccess())
			System.out.println("id为["+processDefinitionId+"]的流程定义信息部署成功");
		else
			System.out.println(result.getMessage());
	}
	
	@Test
	public void start() {
		int processDefinitionId = 1;
		StartParameter parameter = new StartParameter(processDefinitionId);
		parameter.addVariable("name", "金石磊");
		parameter.addVariable("day", 15);
		parameter.setUserId("金石磊");
		parameter.addAssignedUserId("douglei");
		
		
		ExecutionResult result = engine.getRuntimeModule().getProcessInstanceService().start(parameter);
		if(result.isSuccess())
			System.out.println("成功启动的流程实例id为["+result.getObject(ProcessInstance.class).getId()+"]");
		else
			System.out.println(result.getMessage());
	}
	
	private int taskId = 7;
	@Test
	public void claim() {
		String userId = "douglei";
		ExecutionResult result = engine.getRuntimeModule().getTaskService().claim(taskId, userId);
		if(result.isSuccess())
			System.out.println("成功认领id为["+taskId+"]的任务");
		else
			System.out.println(result.getMessage());
	}
	
	@Test
	public void handle() {
		String userId = "douglei";
		
		HandleTaskParameter parameter = new HandleTaskParameter();
		parameter.addAssignedUserId("douglei");
		parameter.setSuggest("同意了").setAttitude(Attitude.AGREE).setUserId(userId);
		
		ExecutionResult result = engine.getRuntimeModule().getTaskService().handle(taskId, parameter);
		if(result.isSuccess())
			System.out.println("成功完成id为["+taskId+"]的任务");
		else
			System.out.println(result.getMessage());
	}
}
