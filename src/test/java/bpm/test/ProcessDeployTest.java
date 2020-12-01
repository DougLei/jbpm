package bpm.test;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.module.repository.definition.entity.builder.ClasspathFile;
import com.douglei.bpm.module.repository.definition.entity.builder.ProcessDefinitionBuilder;
import com.douglei.bpm.process.parser.ProcessParseException;

public class ProcessDeployTest {
	private ProcessEngine engine;
	
	@Before
	public void init() {
		engine = new ProcessEngineBuilder().build();
	}
	
	@Test
	public void insert() throws ProcessParseException {
		ProcessDefinitionBuilder builder = new ProcessDefinitionBuilder(new ClasspathFile("test.bpm.xml"));
		ProcessDefinition processDefinition = engine.getRepositoryModule().getDefinitionService().insert(builder, false);
		System.out.println("插入成功一条流程定义信息, 其id为: "+processDefinition.getId());
	}
	
	@Test
	public void deploy() throws ProcessParseException {
		int processDefinitionId= 1;
		engine.getRepositoryModule().getDefinitionService().deploy(processDefinitionId, null);
		System.out.println("id为["+processDefinitionId+"]的流程定义信息部署成功");
	}
}
