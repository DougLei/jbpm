package bpm.test;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.module.runtime.instance.entity.ProcessInstance;
import com.douglei.bpm.process.parser.ProcessParseException;

public class ProcessRuntimeTest {
	private ProcessEngine engine;
	
	@Before
	public void init() {
		engine = new ProcessEngineBuilder().build();
	}
	
	@Test
	public void start() throws ProcessParseException {
		StartParameter parameter = new StartParameter(1);
		parameter.addVariable("name", "金石磊");
		parameter.addVariable("user", new User("douglei 4 user"));
		parameter.setStartUserId("金石磊");
		
		ExecutionResult<ProcessInstance> result = engine.getRuntimeModule().getInstanceService().start(parameter);
		if(result.isSuccess())
			System.out.println("成功启动的流程实例id为["+result.getResult().getId()+"]");
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
