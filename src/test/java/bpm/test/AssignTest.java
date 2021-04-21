package bpm.test;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.instance.command.parameter.StartParameter;
import com.douglei.bpm.module.execution.instance.runtime.ProcessInstance;
import com.douglei.bpm.module.execution.task.command.dispatch.impl.SettargetDispatchExecutor;
import com.douglei.bpm.module.execution.task.command.parameter.DispatchTaskParameter;
import com.douglei.bpm.module.execution.task.command.parameter.HandleTaskParameter;
import com.douglei.bpm.module.execution.task.history.Attitude;
import com.douglei.bpm.module.repository.definition.ClasspathFile;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.bpm.module.repository.definition.ProcessDefinitionBuilder;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;

public class AssignTest {
	private ProcessEngine engine;
	
	@Before
	public void init() {
		engine = new ProcessEngineBuilder().build();
	}
	
	@Test
	public void insert() throws ProcessParseException {
		ProcessDefinitionBuilder builder = new ProcessDefinitionBuilder(new ClasspathFile("bpm/Assign.bpm.xml"));
		builder.setTypeId(3);
		builder.setStrict(true);
		Result result = engine.getRepositoryModule().getDefinitionService().insert(builder);
		if(result.isSuccess())
			System.out.println("插入成功一条流程定义信息, 其id为: "+ ((ProcessDefinition)result.getObject()).getId());
		else
			System.out.println(result.getMessage());
	}
	
	@Test
	public void deploy() throws ProcessParseException {
		int processDefinitionId= 2;
		Result result = engine.getRepositoryModule().getDefinitionService().deploy(processDefinitionId);
		if(result.isSuccess())
			System.out.println("id为["+processDefinitionId+"]的流程定义信息部署成功");
		else
			System.out.println(result.getMessage());
	}
	
	@Test
	public void start() {
		int processDefinitionId =5;
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
	
	@Test
	public void test() {
		Result result = engine.getExecutionModule().getProcessInstanceService().wake(2);
//		Result result = engine.getExecutionModule().getProcessInstanceService().suspend(1);
//		Result result = engine.getExecutionModule().getProcessInstanceService().terminate(1, "金石磊", "测试流程终止");
//		Result result = engine.getExecutionModule().getProcessInstanceService().recovery(1, false);
		if(result.isSuccess())
			System.out.println("成功");
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
		parameter.setSuggest("意见内容: 同意").setAttitude(Attitude.AGREE).setUserId("wangwu");
		
		Result result = engine.getExecutionModule().getTaskService().handle(3, parameter);
		if(result.isSuccess())
			System.out.println("成功完成id为["+taskId+"]的任务");
		else
			System.out.println(result.getMessage());
	}
	
	@Test
	public void dispatch() {
		DispatchTaskParameter parameter = new DispatchTaskParameter();
		parameter.setUserId("wangwu");
		parameter.setDispatchExecutor(new SettargetDispatchExecutor("endEvent1"));
		
		engine.getExecutionModule().getTaskService().dispatch(3, parameter );
		
	}
}
