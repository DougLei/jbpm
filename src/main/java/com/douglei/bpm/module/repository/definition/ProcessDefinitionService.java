package com.douglei.bpm.module.repository.definition;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.instance.InstanceHandlePolicy;
import com.douglei.bpm.module.history.instance.HistoryInstanceQueryService;
import com.douglei.bpm.module.history.instance.HistoryInstanceService;
import com.douglei.bpm.module.repository.definition.builder.ProcessDefinitionBuilder;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinitionStateConstants;
import com.douglei.bpm.module.runtime.instance.RuntimeInstanceQueryService;
import com.douglei.bpm.module.runtime.instance.RuntimeInstanceService;
import com.douglei.bpm.process.ProcessHandler;

/**
 * 流程定义服务
 * @author DougLei
 */
@Bean
public class ProcessDefinitionService {

	@Autowired
	private RuntimeInstanceQueryService runtimeInstanceQueryService;
	
	@Autowired
	private RuntimeInstanceService runtimeInstanceService;
	
	@Autowired
	private HistoryInstanceQueryService historyInstanceQueryService;
	
	@Autowired
	private HistoryInstanceService historyInstanceService;
	
	@Autowired
	private ProcessHandler processHandler;
	
	/**
	 * 保存流程定义
	 * @param builder
	 * @param strict 是否要强制保存; 针对有实例的流程定义, 如果为true, 则会创建新的子版本并保存, 否则返回错误信息
	 * @return
	 */
	public ExecutionResult<ProcessDefinition> save(ProcessDefinitionBuilder builder, boolean strict) {
//		ProcessDefinition processDefinition = builder.build();
//		ProcessDefinition exProcessDefinition = SessionContext.getSQLSession().queryFirst(ProcessDefinition.class, "ProcessDefinition", "queryProcessDefinition4Save", processDefinition);
//		if(exProcessDefinition == null) {
//			// 新的流程定义, 进行save
//			SessionContext.getTableSession().save(processDefinition); 
//		}else {
//			if(exProcessDefinition.getState() == ProcessDefinitionStateConstants.DELETE)
//				return new ExecutionResult<ProcessDefinition>("已存在code为[%s], version为[%s]的流程定义", "bpm.process.defined.code.version.exists", processDefinition.getCode(), processDefinition.getVersion());
//			
//			if(exProcessDefinition.getSignature().equals(processDefinition.getSignature())){ // 没有修改流程定义的内容, 进行update
//				processDefinition.setId(exProcessDefinition.getId());
//				processDefinition.setSubversion(exProcessDefinition.getSubversion());
//				processDefinition.setState(exProcessDefinition.getState());
//				processDefinition.setContent(null);
//				processDefinition.setSignature(null);
//				SessionContext.getTableSession().update(processDefinition);
//			}else if(!runtimeInstanceQueryService.exists(exProcessDefinition.getId()) && !historyInstanceQueryService.exists(exProcessDefinition.getId())) { // 修改了内容, 但旧的流程定义不存在实例, 进行update
//				processDefinition.setId(exProcessDefinition.getId());
//				processDefinition.setSubversion(exProcessDefinition.getSubversion());
//				processDefinition.setState(exProcessDefinition.getState());
//				SessionContext.getTableSession().update(processDefinition); 
//				
//				if(exProcessDefinition.getState() == ProcessDefinitionStateConstants.DEPLOY) 
//					processHandler.addProcess(processDefinition);
//			}else { // 修改了内容, 且旧的流程定义存在实例, 根据参数strict的值, 进行save, 或提示操作失败
//				if(!strict) 
//					return new ExecutionResult<ProcessDefinition>("操作失败, 当前流程已存在实例", "bpm.process.defined.instance.exists", processDefinition.getName());
//				
//				processDefinition.setSubversion(exProcessDefinition.getSubversion()+1);
//				processDefinition.setState(exProcessDefinition.getState());
//				SessionContext.getTableSession().save(processDefinition); 
//				
//				if(exProcessDefinition.getState() == ProcessDefinitionStateConstants.DEPLOY) 
//					processHandler.addProcess(processDefinition);
//			}
//		}
//		return new ExecutionResult<ProcessDefinition>(processDefinition, strict);
		return null;
	}
	
	/**
	 * 更新流程定义的状态
	 * @param processDefinitionId
	 * @param state {@link ProcessDefinitionStateConstants}
	 */
	private void updateState(int processDefinitionId, byte state) {
//		SessionContext.getSqlSession().executeUpdate("update bpm_re_procdef set state=? where id=?", Arrays.asList(state, processDefinitionId));
	}
	
	/**
	 * 流程部署
	 * @param processDefinitionId 
	 * @param runtimeInstancePolicy 对运行实例的处理策略, 如果传入null, 则不进行任何处理
	 * @return
	 */
	public ExecutionResult<Integer> deploy(int processDefinitionId, InstanceHandlePolicy runtimeInstancePolicy) {
//		ProcessDefinition processDefinition = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, state, content_ from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
//		if(processDefinition == null || processDefinition.getState() == ProcessDefinitionStateConstants.DELETE)
//			return new ExecutionResult<Integer>("操作失败, 不存在id为[%d]的流程定义", "bpm.process.defined.id.unexists", processDefinitionId);
//		if(processDefinition.getState() == ProcessDefinitionStateConstants.DEPLOY)
//			return new ExecutionResult<Integer>("操作失败, 当前流程已部署", "bpm.process.defined.already.deploy", processDefinitionId);
//		
//		if(runtimeInstancePolicy != null && runtimeInstanceQueryService.exists(processDefinitionId))
//			runtimeInstanceService.handle(processDefinitionId, runtimeInstancePolicy);
//		
//		updateState(processDefinitionId, ProcessDefinitionStateConstants.DEPLOY);
//		processHandler.addProcess(processDefinition);
//		return new ExecutionResult<Integer>(processDefinitionId, runtimeInstancePolicy);
		return null;
	}
	
	/**
	 * 取消流程部署
	 * @param processDefinitionId
	 * @param runtimeInstancePolicy 对运行实例的处理策略, 如果传入null, 则不进行任何处理
	 * @param historyInstancePolicy 对历史实例的处理策略, 如果传入null, 则不进行任何处理
	 * @return
	 */
	public ExecutionResult<Integer> undeploy(int processDefinitionId, InstanceHandlePolicy runtimeInstancePolicy, InstanceHandlePolicy historyInstancePolicy) {
//		ProcessDefinition processDefinition = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, code, version, subversion, state from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
//		if(processDefinition == null || processDefinition.getState() == ProcessDefinitionStateConstants.DELETE)
//			return new ExecutionResult<Integer>("操作失败, 不存在id为[%d]的流程定义", "bpm.process.defined.id.unexists", processDefinitionId);
//		if(processDefinition.getState() == ProcessDefinitionStateConstants.UNDEPLOY)
//			return new ExecutionResult<Integer>("操作失败, 当前流程未部署", "bpm.process.defined.undeploy", processDefinitionId);
//		
//		if(runtimeInstancePolicy != null && runtimeInstanceQueryService.exists(processDefinitionId)) 
//			runtimeInstanceService.handle(processDefinitionId, runtimeInstancePolicy);
//		
//		if(historyInstancePolicy != null && historyInstanceQueryService.exists(processDefinitionId)) 
//			historyInstanceService.handle(processDefinitionId, historyInstancePolicy);
//		
//		updateState(processDefinitionId, ProcessDefinitionStateConstants.UNDEPLOY);
//		processHandler.deleteProcess(processDefinitionId);
//		return new ExecutionResult<Integer>(processDefinitionId, runtimeInstancePolicy, historyInstancePolicy);
		return null;
	}
	
	/**
	 * 删除流程定义
	 * @param processDefinitionId
	 * @param strict 是否强制删除; 针对有实例的流程定义, 如果为true, 则会修改流程定义的状态为删除(逻辑删除), 否则返回错误信息
	 * @return
	 */
	public ExecutionResult<Integer> delete(int processDefinitionId, boolean strict) {
//		List<Object> paramList = Arrays.asList(processDefinitionId);
//		
//		ProcessDefinition processDefinition = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select name, state from bpm_re_procdef where id=?", paramList);
//		if(processDefinition == null || processDefinition.getState() == ProcessDefinitionStateConstants.DELETE)
//			return new ExecutionResult<Integer>("操作失败, 不存在id为[%d]的流程定义", "bpm.process.defined.id.unexists", processDefinitionId);
//		if(processDefinition.getState() == ProcessDefinitionStateConstants.DEPLOY)
//			return new ExecutionResult<Integer>("操作失败, 当前流程已部署, 请先取消部署", "bpm.process.defined.undeploy.first", processDefinitionId);
//		
//		if(runtimeInstanceQueryService.exists(processDefinitionId) || historyInstanceQueryService.exists(processDefinitionId)) {
//			if(!strict)
//				return new ExecutionResult<Integer>("操作失败, 当前流程已存在实例", "bpm.process.defined.instance.exists");
//			updateState(processDefinitionId, ProcessDefinitionStateConstants.DELETE);
//		} else {
//			SessionContext.getSqlSession().executeUpdate("delete bpm_re_procdef where id=?", paramList);
//		}
//		return new ExecutionResult<Integer>(processDefinitionId, strict);
		return null;
	}
}
