package bpm.test;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.component.ExecutionResult;
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
		parameter.setStartUserId("金石磊");
		
		ExecutionResult<ProcessInstance> result = engine.getRuntimeModule().getInstanceService().start(parameter);
		if(result.isSuccess())
			System.out.println("成功启动的流程实例id为["+result.getResult().getId()+"]");
	}
}
