package bpm.test;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.components.ExecutionResult;
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
		ExecutionResult<ProcessDefinition> result = engine.getRepositoryModule().getDefinitionService().insert(builder, false);
		System.out.println(result);
		System.out.println("插入成功一条流程定义信息, 其id为: "+result.getObject().getId());
	}
	
	@Test
	public void deploy() throws ProcessParseException {
		ExecutionResult<Integer> result = engine.getRepositoryModule().getDefinitionService().deploy(1, null);
		System.out.println(result);
		
		if(result.isSuccess())
			System.out.println("id为["+result.getObject()+"]的流程定义信息部署成功");
	}
}
