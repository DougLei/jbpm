package bpm.test;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.instance.command.parameter.StartParameter;
import com.douglei.bpm.module.execution.instance.runtime.ProcessInstance;
import com.douglei.bpm.module.execution.task.command.parameter.HandleTaskParameter;
import com.douglei.bpm.module.execution.task.history.Attitude;
import com.douglei.bpm.module.repository.definition.ClasspathFile;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.bpm.module.repository.definition.ProcessDefinitionEntity;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;

public class AssignTest {
	private ProcessEngine engine;
	
	@Before
	public void init() {
		engine = new ProcessEngineBuilder().build();
	}
	
	@Test
	public void insert() throws ProcessParseException {
		ProcessDefinitionEntity builder = new ProcessDefinitionEntity(new ClasspathFile("bpm/Assign.bpm.xml"));
		Result result = engine.getRepositoryModule().getDefinitionService().insert(builder);
		if(result.isSuccess())
			System.out.println("插入成功一条流程定义信息, 其id为: "+ ((ProcessDefinition)result.getObject()).getId());
		else
			System.out.println(result.getMessage());
	}
	
	@Test
	public void deploy() throws ProcessParseException {
		int processDefinitionId= 1;
		Result result = engine.getRepositoryModule().getDefinitionService().deploy(processDefinitionId);
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
		
		
		Result result = engine.getExecutionModule().getProcessInstanceService().start(parameter);
		if(result.isSuccess())
			System.out.println("成功启动的流程实例id为["+result.getObject(ProcessInstance.class).getId()+"]");
		else
			System.out.println(result.getMessage());
	}
	
	private int taskId = 4;
	private String userId = "jinshilei";
	
	@Test
	public void claim() {
		Result result = engine.getExecutionModule().getTaskService().claim(taskId, userId);
		if(result.isSuccess())
			System.out.println("成功认领id为["+taskId+"]的任务");
		else
			System.out.println(result.getMessage());
	}
	
	@Test
	public void delegate() {
		Result result = engine.getExecutionModule().getTaskService().delegate(taskId, userId, "haha", "测试委托功能");
		if(result.isSuccess())
			System.out.println("成功委托id为["+taskId+"]的任务");
		else
			System.out.println(result.getMessage());
	}
	
	@Test
	public void transfer() {
		Result result = engine.getExecutionModule().getTaskService().transfer(taskId, userId, "douglei", "测试转办功能");
		if(result.isSuccess())
			System.out.println("成功转办id为["+taskId+"]的任务");
		else
			System.out.println(result.getMessage());
	}
	
	@Test
	public void unclaim() {
		Result result = engine.getExecutionModule().getTaskService().unclaim(taskId, userId, false);
		if(result.isSuccess())
			System.out.println("成功取消认领id为["+taskId+"]的任务");
		else
			System.out.println(result.getMessage());
	}
	
	@Test
	public void handle() {
		HandleTaskParameter parameter = new HandleTaskParameter();
		parameter.addAssignedUserId("test");
		parameter.setSuggest("同意了").setAttitude(Attitude.AGREE).setUserId(userId);
		
		Result result = engine.getExecutionModule().getTaskService().handle(taskId, parameter);
		if(result.isSuccess())
			System.out.println("成功完成id为["+taskId+"]的任务");
		else
			System.out.println(result.getMessage());
	}
}
