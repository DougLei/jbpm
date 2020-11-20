package com.douglei.bpm.module.runtime.instance.command;

import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.runtime.instance.entity.ProcessRuntimeInstance;
import com.douglei.bpm.module.runtime.instance.start.StartParameter;

/**
 * 
 * @author DougLei
 */
public class ProcessStartCommand implements Command<ProcessRuntimeInstance> {
	private StartParameter parameter;
	
	public ProcessStartCommand(StartParameter parameter) {
		this.parameter = parameter;
	}

	@Override
	public ExecutionResult<ProcessRuntimeInstance> execute() {
		
		
//		int processDefinitionId;
//		switch(parameter.getStartingMode()) {
//			case BY_PROCESS_DEFINITION_ID:
//				Object[] processDefinition = session.getSqlSession().uniqueQuery_("select state from bpm_re_procdef where id=?", Arrays.asList(parameter.getProcessDefinitionId()));
//				if(processDefinition == null || Byte.parseByte(processDefinition[0].toString()) == ProcessDefinitionStateConstants.DELETE)
//					throw new NullPointerException("启动失败, 不存在id为["+parameter.getProcessDefinitionId()+"]的流程定义");
//				if(Byte.parseByte(processDefinition[0].toString()) == ProcessDefinitionStateConstants.UNDEPLOY)
//					throw new IllegalArgumentException("启动失败, id为["+parameter.getProcessDefinitionId()+"]的流程定义还未发布");
//				break;
//			case BY_PROCESS_DEFINITION_CODE_VERSION:
//				break;
//		}
		
		
//		int getProcessDefinitionIdAfterDBValidate(SqlSession session) {
//			if(processDefinitionId > 0) {
//				Object[] processDefinition = session.uniqueQuery_("select state from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
//				if(processDefinition == null || Byte.parseByte(processDefinition[0].toString()) == ProcessDefinitionStateConstants.DELETE)
//					throw new NullPointerException("启动失败, 不存在id为["+processDefinitionId+"]的流程定义");
//				if(Byte.parseByte(processDefinition[0].toString()) == ProcessDefinitionStateConstants.UNPUBLISHED)
//					throw new IllegalArgumentException("启动失败, id为["+processDefinitionId+"]的流程定义还未发布");
//			}else {
//				Object[] processDefinition = session.queryFirst_("select state, id from bpm_re_procdef where code=? and version=? order by subversion desc", Arrays.asList(code, version));
//				if(processDefinition == null || Byte.parseByte(processDefinition[0].toString()) == ProcessDefinitionStateConstants.DELETE)
//					throw new NullPointerException("启动失败, 不存在code为["+code+"], version为["+version+"]的流程定义");
//				if(Byte.parseByte(processDefinition[0].toString()) == ProcessDefinitionStateConstants.UNPUBLISHED)
//					throw new IllegalArgumentException("启动失败, code为["+code+"], version为["+version+"]的流程定义还未发布");
//				this.processDefinitionId = Integer.parseInt(processDefinition[1].toString());
//			}
//			return this.processDefinitionId;
//		}
		
//		int processDefinitionId = parameter.getProcessDefinitionIdAfterDBValidate(SessionContext.getSqlSession());
//		Process process = processHandler.get(processDefinitionId);
//		
//		// TODO 
//		return process.start(starter);
		
		
		return null;
	}
}
