package com.douglei.bpm.process.executor.event.start;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.runtime.task.entity.Assignee;
import com.douglei.bpm.module.runtime.task.entity.Task;
import com.douglei.bpm.module.runtime.task.entity.Variable;
import com.douglei.bpm.process.executor.Executor;
import com.douglei.bpm.process.executor.Executors;
import com.douglei.bpm.process.metadata.node.event.StartEventMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Executor.class)
public class StartEventExecutor extends Executor<StartEventMetadata, StartEventExecutionParameter> {
	
	@Autowired
	private Executors executors;
	
	@Override
	public Class<StartEventMetadata> getMetadataClass() {
		return StartEventMetadata.class;
	}

	@Override
	public void execute(StartEventMetadata startEvent, StartEventExecutionParameter parameter) {
		
//		if(起始事件判断当前的启动条件不满足)
//			return new ExecutionResult<ProcessRuntimeInstance>("不能启动", "");
	
	
	
		// 创建流程运行任务
		Task processRuntimeTask = startEvent.createProcessRuntimeTask(parameter.getProcessDefinitionId(), parameter.getProcessInstanceId());
		SessionContext.getTableSession().save(processRuntimeTask);
		
		Assignee assignee = new Assignee();
		assignee.setTaskId(processRuntimeTask.getId());
		assignee.setMode(1);
		assignee.setUserId(parameter.getStartParameter().getStartUserId());
		SessionContext.getTableSession().save(assignee);
		
		List<Variable> variables = new ArrayList<Variable>();
		parameter.getStartParameter().getVariables().forEach((key, value) ->{
			Variable variable = new Variable();
			variable.setProcdefId(parameter.getProcessDefinitionId());
			variable.setProcinstId(parameter.getProcessInstanceId());
			variable.setTaskId(processRuntimeTask.getId());
			variable.setScope(1);
			variable.setName(key);
			variable.setDataType("string");
			variable.setStringVal(value.toString());
			
			variables.add(variable);
		});
		SessionContext.getTableSession().save(variables);
		
		
		
	}
}
