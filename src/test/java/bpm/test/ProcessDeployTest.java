package bpm.test;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.core.process.parser.ProcessParseException;
import com.douglei.bpm.module.repository.definition.ClasspathFile;
import com.douglei.bpm.module.repository.definition.ProcessDefinitionBuilder;
import com.douglei.orm.context.IdDuplicateException;

public class ProcessDeployTest {
	private ProcessEngine engine;
	
	@Before
	public void init() throws IdDuplicateException {
		engine = new ProcessEngineBuilder().build();
	}
	
	@Test
	public void save() throws ProcessParseException {
		ProcessDefinitionBuilder builder = new ProcessDefinitionBuilder(new ClasspathFile("processTest.bpm.xml"));
		engine.getRepository().getDefinitionService().save(builder, false);
	}
}
