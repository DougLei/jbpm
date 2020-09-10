package bpm.test;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.orm.context.IdDuplicateException;

public class ProcessDeployTest {
	private ProcessEngine engine;
	
	@Before
	public void init() throws IdDuplicateException {
		engine = ProcessEngineBuilder.getSingleton().build();
	}
	
	@Test
	public void deploy() {
		ProcessDefinition processDefinition = new ProcessDefinition("D:\\workspace4\\jbpm\\src\\test\\resources\\processTest.bpm.xml");
		engine.getRepository().getDefinitionService().deploy(processDefinition);
	}
}
