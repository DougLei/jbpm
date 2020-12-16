package bpm.test;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.instance.StartParameter;
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
		StartParameter parameter = new StartParameter("vocation", "v2020");
		parameter.addVariable("name", "金石磊");
		parameter.addVariable("day", 15);
		parameter.addVariable("user", Scope.LOCAL, new User("douglei 4 user"));
		parameter.setStartUserId("金石磊");
		
		ExecutionResult result = engine.getRuntimeModule().getProcessInstanceService().start(parameter);
		if(result.isSuccess())
			System.out.println("成功启动的流程实例id为["+processDefinitionId+"]");
		else
			System.out.println(result.getFailMessage());
	}
	
	@Test
	public void complete() {
		int taskId = 1;
		ExecutionResult result = engine.getRuntimeModule().getTaskService().complete(taskId);
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
