package bpm.test;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.repository.definition.builder.ClasspathFile;
import com.douglei.bpm.module.repository.definition.builder.ProcessDefinitionBuilder;
import com.douglei.bpm.process.parser.ProcessParseException;

public class ProcessDeployTest {
	private ProcessEngine engine;
	
	@Before
	public void init() {
		engine = new ProcessEngineBuilder().build();
	}
	
	@Test
	public void save() throws ProcessParseException {
		ProcessDefinitionBuilder builder = new ProcessDefinitionBuilder(new ClasspathFile("processTest.bpm.xml"));
		engine.getRepositoryModule().getDefinitionService().save(builder, false);
	}
}
