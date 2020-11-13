package com.douglei.bpm.module.repository.definition;

import java.util.Arrays;

import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.common.InstanceHandlePolicy;
import com.douglei.bpm.module.common.service.ExecutionResult;
import com.douglei.bpm.module.history.instance.HistoryInstanceService;
import com.douglei.bpm.module.runtime.instance.RuntimeInstanceService;
import com.douglei.bpm.process.ProcessHandler;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 流程定义服务
 * @author DougLei
 */
@Bean
public class ProcessDefinitionService {

	@Attribute
	private RuntimeInstanceService runtimeInstanceService;
	
	@Attribute
	private HistoryInstanceService historyInstanceService;
	
	@Attribute
	private ProcessHandler processHandler;
	
	/**
	 * 保存流程定义
	 * @param builder
	 * @param strict 是否要强制保存; 针对有实例的流程定义, 如果为true, 则会创建新的子版本并保存, 否则返回错误信息
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult save(ProcessDefinitionBuilder builder, boolean strict) {
		ProcessDefinition processDefinition = builder.build();
		ProcessDefinition exProcessDefinition = SessionContext.getTableSession().queryFirst(ProcessDefinition.class, "select id, subversion, signature, state from bpm_re_procdef where code=? and version=? order by subversion desc", Arrays.asList(processDefinition.getCode(), processDefinition.getVersion()));
		if(exProcessDefinition == null) {
			// 新的流程定义, 进行save
			SessionContext.getTableSession().save(processDefinition); 
		}else {
			if(exProcessDefinition.getState() == ProcessDefinitionStateConstants.DELETE)
				return new ExecutionResult(null, "已存在code为[%s], version为[%s]的流程定义", "bpm.process.defined.code.version.exists", processDefinition.getCode(), processDefinition.getVersion());
			
			if(exProcessDefinition.getSignature().equals(processDefinition.getSignature())){ // 没有修改流程定义的内容, 进行update
				processDefinition.setId(exProcessDefinition.getId());
				processDefinition.setSubversion(exProcessDefinition.getSubversion());
				processDefinition.setState(exProcessDefinition.getState());
				processDefinition.setContent(null);
				processDefinition.setSignature(null);
				SessionContext.getTableSession().update(processDefinition);
			}else if(!runtimeInstanceService.exists(exProcessDefinition.getId()) && !historyInstanceService.exists(exProcessDefinition.getId())) { // 修改了内容, 但旧的流程定义不存在实例, 进行update
				processDefinition.setId(exProcessDefinition.getId());
				processDefinition.setSubversion(exProcessDefinition.getSubversion());
				processDefinition.setState(exProcessDefinition.getState());
				SessionContext.getTableSession().update(processDefinition); 
				
				if(exProcessDefinition.getState() == ProcessDefinitionStateConstants.PUBLISHED) 
					processHandler.put(processDefinition);
			}else { // 修改了内容, 且旧的流程定义存在实例, 根据参数strict的值, 进行save, 或提示操作失败
				if(!strict) 
					return new ExecutionResult(null, "操作失败, 流程[%s]已经存在实例", "bpm.process.defined.instance.exists", processDefinition.getName());
				
				processDefinition.setSubversion(exProcessDefinition.getSubversion()+1);
				processDefinition.setState(exProcessDefinition.getState());
				SessionContext.getTableSession().save(processDefinition); 
				
				if(exProcessDefinition.getState() == ProcessDefinitionStateConstants.PUBLISHED) 
					processHandler.put(processDefinition);
			}
		}
		return null;
	}
	
	/**
	 * 更新流程定义的状态
	 * @param processDefinitionId
	 * @param state {@link ProcessDefinitionStateConstants}
	 */
	private void updateState(int processDefinitionId, byte state) {
		SessionContext.getSqlSession().executeUpdate("update bpm_re_procdef set state=? where id=?", Arrays.asList(state, processDefinitionId));
	}
	
	/**
	 * 发布流程
	 * @param processDefinitionId 
	 * @param runtimeInstancePolicy 对运行实例的处理策略, 如果传入null, 则不进行任何处理
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult publish(int processDefinitionId, InstanceHandlePolicy runtimeInstancePolicy) {
		ProcessDefinition processDefinition = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, state, content_ from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefinition == null || processDefinition.getState() == ProcessDefinitionStateConstants.DELETE)
			return new ExecutionResult("id", "操作失败, 不存在id为[%d]的流程定义", "bpm.process.defined.id.unexists", processDefinitionId);
		if(processDefinition.getState() == ProcessDefinitionStateConstants.PUBLISHED)
			return new ExecutionResult("id", "操作失败, id为[%d]的流程定义已经发布", "bpm.process.defined.already.publish", processDefinitionId);
		
		if(runtimeInstancePolicy != null && runtimeInstanceService.exists(processDefinitionId))
			runtimeInstanceService.process(processDefinitionId, runtimeInstancePolicy);
		
		updateState(processDefinitionId, ProcessDefinitionStateConstants.PUBLISHED);
		processHandler.put(processDefinition);
		return null;
	}
	
	/**
	 * 取消发布流程
	 * @param processDefinitionId
	 * @param runtimeInstancePolicy 对运行实例的处理策略, 如果传入null, 则不进行任何处理
	 * @param historyInstancePolicy 对历史实例的处理策略, 如果传入null, 则不进行任何处理
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult cancelPublish(int processDefinitionId, InstanceHandlePolicy runtimeInstancePolicy, InstanceHandlePolicy historyInstancePolicy) {
		ProcessDefinition processDefinition = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, code, version, subversion, state from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefinition == null || processDefinition.getState() == ProcessDefinitionStateConstants.DELETE)
			return new ExecutionResult("id", "操作失败, 不存在id为[%d]的流程定义", "bpm.process.defined.id.unexists", processDefinitionId);
		if(processDefinition.getState() == ProcessDefinitionStateConstants.UNPUBLISHED)
			return new ExecutionResult("id", "操作失败, id为[%d]的流程定义已经处于未发布状态", "bpm.process.defined.already.unpublish", processDefinitionId);
		
		if(runtimeInstancePolicy != null && runtimeInstanceService.exists(processDefinitionId)) 
			runtimeInstanceService.process(processDefinitionId, runtimeInstancePolicy);
		
		if(historyInstancePolicy != null && historyInstanceService.exists(processDefinitionId)) 
			historyInstanceService.process(processDefinitionId, historyInstancePolicy);
		
		updateState(processDefinitionId, ProcessDefinitionStateConstants.UNPUBLISHED);
		processHandler.remove(processDefinitionId);
		return null;
	}
	
	/**
	 * 删除流程定义
	 * @param processDefinitionId
	 * @param strict 是否强制删除; 针对有实例的流程定义, 如果为true, 则会修改流程定义的状态为删除(逻辑删除), 否则返回错误信息
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult delete(int processDefinitionId, boolean strict) {
		ProcessDefinition processDefinition = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select name, state from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefinition == null || processDefinition.getState() == ProcessDefinitionStateConstants.DELETE)
			return new ExecutionResult("id", "操作失败, 不存在id为[%d]的流程定义", "bpm.process.defined.id.unexists", processDefinitionId);
		if(processDefinition.getState() == ProcessDefinitionStateConstants.PUBLISHED)
			return new ExecutionResult("id", "操作失败, id为[%d]的流程定义已经发布, 请先取消发布", "bpm.process.defined.cancel.publish.first", processDefinitionId);
		
		if(runtimeInstanceService.exists(processDefinitionId) || historyInstanceService.exists(processDefinitionId)) {
			if(!strict)
				return new ExecutionResult("id", "操作失败, 流程[%s]已经存在实例", "bpm.process.defined.instance.exists", processDefinition.getName());
			updateState(processDefinitionId, ProcessDefinitionStateConstants.DELETE);
		} else {
			SessionContext.getSqlSession().executeUpdate("delete bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		}
		return null;
	}
}
