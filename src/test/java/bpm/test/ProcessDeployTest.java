package bpm.test;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.repository.definition.ClasspathFile;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.bpm.module.repository.definition.ProcessDefinitionEntity;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;

public class ProcessDeployTest {
	private ProcessEngine engine;
	
	@Before
	public void init() {
		engine = new ProcessEngineBuilder().build();
	}
	
	@Test
	public void insert() throws ProcessParseException {
		ProcessDefinitionEntity builder = new ProcessDefinitionEntity(new ClasspathFile("test.bpm.xml"));
		builder.setTypeId(2);
		Result result = engine.getRepositoryModule().getDefinitionService().insert(builder);
		if(result.isSuccess())
			System.out.println("插入成功一条流程定义信息, 其id为: "+ ((ProcessDefinition)result.getObject()).getId());
		else
			System.out.println(result.getMessage());
	}
	
	@Test
	public void deploy() throws ProcessParseException {
		int processDefinitionId= 1;
		Result result = engine.getRepositoryModule().getDefinitionService().deploy(processDefinitionId);
		if(result.isSuccess())
			System.out.println("id为["+processDefinitionId+"]的流程定义信息部署成功");
		else
			System.out.println(result.getMessage());
	}
}
