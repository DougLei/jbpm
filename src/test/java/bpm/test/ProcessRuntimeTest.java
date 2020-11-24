package bpm.test;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.runtime.instance.start.StartParameter;
import com.douglei.bpm.process.parser.ProcessParseException;

public class ProcessRuntimeTest {
	private ProcessEngine engine;
	
	@Before
	public void init() {
		engine = new ProcessEngineBuilder().build();
	}
	
	@Test
	public void start() throws ProcessParseException {
		StartParameter starter = new StartParameter(1);
		engine.getRuntimeModule().getInstanceService().start(starter );
	}
}
