package bpm.test;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.instance.command.parameter.StartParameter;
import com.douglei.bpm.module.execution.instance.runtime.ProcessInstance;
import com.douglei.bpm.module.execution.task.command.parameter.HandleTaskParameter;
import com.douglei.bpm.module.execution.task.history.Attitude;
import com.douglei.bpm.module.execution.variable.Scope;

public class ProcessRuntimeTest {
	private ProcessEngine engine;
	
	@Before
	public void init() {
		engine = new ProcessEngineBuilder().build();
	}
	
	@Test
	public void start() {
		int processDefinitionId = 1;
		StartParameter parameter = new StartParameter(processDefinitionId);
		parameter.getVariableEntities().addVariable("name", Scope.GLOBAL, "金石磊");
		parameter.getVariableEntities().addVariable("day", Scope.GLOBAL, 15);
		parameter.getVariableEntities().addVariable("user", Scope.LOCAL, new User("douglei 4 user"));
		parameter.setUserId("金石磊");
		parameter.getAssignEntity().addAssignedUserId("douglei");
		
		Result result = engine.getExecutionModule().getProcessInstanceService().start(parameter);
		if(result.isSuccess())
			System.out.println("成功启动的流程实例id为["+result.getObject(ProcessInstance.class).getId()+"]");
		else
			System.out.println(result.getMessage());
	}
	
	@Test
	public void claim() {
		int taskId = 6;
		String userId = "张三";
		Result result = engine.getExecutionModule().getTaskService().claim(taskId, userId);
		if(result.isSuccess())
			System.out.println("成功认领id为["+taskId+"]的任务");
		else
			System.out.println(result.getMessage());
	}
	
	@Test
	public void handle() {
		int taskId = 6;
		String userId = "张三";
		
		HandleTaskParameter parameter = new HandleTaskParameter();
		parameter.setSuggest("同意了").setAttitude(Attitude.AGREE).setUserId(userId);
		
		Result result = engine.getExecutionModule().getTaskService().handle(taskId, parameter);
		if(result.isSuccess())
			System.out.println("成功完成id为["+taskId+"]的任务");
		else
			System.out.println(result.getMessage());
	}
}

class User implements Serializable{
	private static final long serialVersionUID = -1624039524127599909L;
	private String name;
	public User(String name) {
		this.name = name;
	}
	public String toString() {
		return "User [name=" + name + "]";
	}
}
