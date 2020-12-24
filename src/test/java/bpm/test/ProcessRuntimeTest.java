package bpm.test;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.instance.ProcessInstance;
import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.module.runtime.task.Attitude;
import com.douglei.bpm.module.runtime.task.TaskHandleParameter;
import com.douglei.bpm.module.runtime.variable.Scope;

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
		parameter.addVariable("name", "金石磊");
		parameter.addVariable("day", 15);
		parameter.addVariable("user", Scope.LOCAL, new User("douglei 4 user"));
		parameter.setStartUserId("金石磊");
		parameter.addAssignedUserId("douglei");
		
		ExecutionResult result = engine.getRuntimeModule().getProcessInstanceService().start(parameter);
		if(result.isSuccess())
			System.out.println("成功启动的流程实例id为["+result.getObject(ProcessInstance.class).getId()+"]");
		else
			System.out.println(result.getFailMessage());
	}
	
	@Test
	public void claim() {
		int taskId = 2;
		String userId = "chengrong";
		ExecutionResult result = engine.getRuntimeModule().getTaskService().claim(taskId, userId);
		if(result.isSuccess())
			System.out.println("成功认领id为["+taskId+"]的任务");
		else
			System.out.println(result.getFailMessage());
	}
	
	@Test
	public void handle() {
		int taskId = 2;
		String userId = "chengrong";
		ExecutionResult result = engine.getRuntimeModule().getTaskService().handle(taskId, new TaskHandleParameter(userId, "同意了", Attitude.AGREE));
		if(result.isSuccess())
			System.out.println("成功完成id为["+taskId+"]的任务");
		else
			System.out.println(result.getFailMessage());
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
