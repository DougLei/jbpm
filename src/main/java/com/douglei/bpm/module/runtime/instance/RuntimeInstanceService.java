package com.douglei.bpm.module.runtime.instance;

import java.util.Arrays;

import com.douglei.bpm.bean.Autowire;
import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.instance.InstanceHandlePolicy;
import com.douglei.bpm.module.runtime.instance.entity.ProcessRuntimeInstance;
import com.douglei.bpm.module.runtime.instance.start.StartParameter;
import com.douglei.bpm.process.ProcessHandler;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 运行实例服务
 * @author DougLei
 */
@Bean
public class RuntimeInstanceService {
	
	@Autowire
	private ProcessHandler processHandler;
	
	/**
	 * 启动流程
	 * @param parameter
	 * @return 
	 */
	@Transaction
	public ExecutionResult<ProcessRuntimeInstance> start(StartParameter parameter) {
//		int processDefinitionId;
//		switch(parameter.getStartingMode()) {
//			case BY_PROCESS_DEFINITION_ID:
//				Object[] processDefinition = SessionContext.getSqlSession().uniqueQuery_("select state from bpm_re_procdef where id=?", Arrays.asList(parameter.getProcessDefinitionId()));
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
	
	
	/**
	 * 判断指定id的流程定义, 是否存在运行实例
	 * @param processDefinitionId
	 * @return
	 */
	@Transaction
	public boolean exists(int processDefinitionId) {
		return Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_("select count(id) from bpm_ru_procinst where procdef_id=?", Arrays.asList(processDefinitionId))[0].toString()) > 0;
	}

	/**
	 * 处理指定id的流程定义, 相关的所有运行实例
	 * @param processDefinitionId
	 * @param policy 对实例的处理策略
	 */
	@Transaction
	public void handle(int processDefinitionId, InstanceHandlePolicy policy) {
		// TODO 处理指定id的流程定义, 相关的所有运行实例
		
	}
	
	/**
	 * 激活指定id的流程实例
	 * @param instanceId
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult<Integer> activate(int instanceId) {
		// TODO 
		
		return null;
	}
	
	/**
	 * 挂起指定id的流程实例
	 * @param instanceId
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult<Integer> suspend(int instanceId) {
		// TODO 
		
		return null;
	}
	
	/**
	 * 终止指定id的流程实例
	 * @param instanceId
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult<Integer> terminate(int instanceId) {
		// TODO 
		
		return null;
	}
	
	/**
	 * 删除指定id的流程实例
	 * @param instanceId
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult<Integer> delete(int instanceId) {
		// TODO 
		
		return null;
	}
}
