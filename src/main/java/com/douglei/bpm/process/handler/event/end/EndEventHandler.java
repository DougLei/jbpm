package com.douglei.bpm.process.handler.event.end;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.handler.AbstractTaskHandler;
import com.douglei.bpm.process.handler.GeneralExecuteParameter;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.handler.components.scheduler.TaskDispatchParameter;
import com.douglei.bpm.process.metadata.node.event.StartEventMetadata;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=TaskHandler.class)
public class EndEventHandler extends AbstractTaskHandler implements TaskHandler<StartEventMetadata, TaskDispatchParameter, GeneralExecuteParameter> {

	@Override
	public ExecutionResult startup(StartEventMetadata taskMetadata, TaskDispatchParameter executeParameter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecutionResult execute(StartEventMetadata taskMetadata, GeneralExecuteParameter executeParameter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Type getType() {
		return Type.END_EVENT;
	}
}
