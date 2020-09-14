package com.douglei.bpm.module.repository.definition;

import java.util.Arrays;

import com.douglei.bpm.annotation.ProcessEngineField;
import com.douglei.bpm.annotation.ProcessEngineTransactionBean;
import com.douglei.bpm.module.common.service.ExecutionResult;
import com.douglei.bpm.module.history.HistoryProcessInstanceService;
import com.douglei.bpm.module.runtime.RuntimeProcessInstanceService;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;
import com.douglei.tools.utils.StringUtil;

/**
 * 流程部署服务
 * @author DougLei
 */
@ProcessEngineTransactionBean
public class ProcessDefinitionService {
	
	@ProcessEngineField
	private RuntimeProcessInstanceService runtimeProcessInstanceService;
	
	@ProcessEngineField
	private HistoryProcessInstanceService historyProcessInstanceService;
	
	/**
	 * 保存流程定义信息
	 * @param processDefined 使用 {@link ProcessDefinitionBuilder} 构建
	 * @param strict 是否要强制保存流程定义; 针对保存的流程定义有实例的情况, 如果为true, 则会创建新的子版本信息保存, 否则会返回ExecutionResult
	 * @return
	 */
	@Transaction
	public ExecutionResult save(ProcessDefinition processDefined, boolean strict) {
		if(StringUtil.isEmpty(processDefined.getCode()))
			return new ExecutionResult("code", "流程定义中的编码值不能为空", "bpm.process.defined.save.code.notnull");
		if(StringUtil.isEmpty(processDefined.getVersion()))
			return new ExecutionResult("version", "流程定义中的版本值不能为空", "bpm.process.defined.save.version.notnull");
		
		ProcessDefinition pd = SessionContext.getTableSession().queryFirst(ProcessDefinition.class, "select id, subversion, signature, state from bpm_re_procdef where code=? and version=? order by subversion desc", Arrays.asList(processDefined.getCode(), processDefined.getVersion()));
		if(pd == null) {
			// 新的流程定义, 进行save
			SessionContext.getTableSession().save(processDefined); 
		}else {
			if(pd.getState() == ProcessDefinition.DELETE)
				return new ExecutionResult(null, "code=%s, version=%s的标识, 已被其他流程定义使用", "bpm.process.defined.save.code.version.exists", processDefined.getCode(), processDefined.getVersion());
			
			if(pd.getSignature().equals(processDefined.getSignature())){
				// 没有修改流程定义的内容, 进行update
				processDefined.setId(pd.getId());
				processDefined.setSubversion(pd.getSubversion());
				processDefined.setState(pd.getState());
				processDefined.setContent(null);
				processDefined.setSignature(null);
				SessionContext.getTableSession().update(processDefined); 
			}else if(!runtimeProcessInstanceService.existsInstance(pd.getId()) && !historyProcessInstanceService.existsInstance(pd.getId())) {
				// 修改了内容, 但旧的流程定义不存在实例, 进行update
				processDefined.setId(pd.getId());
				processDefined.setSubversion(pd.getSubversion());
				processDefined.setState(pd.getState());
				SessionContext.getTableSession().update(processDefined); 
				
				if(processDefined.getState() == ProcessDefinition.ENABLED) 
					enable(processDefined, false);
			}else {
				// 修改了内容, 且旧的流程定义存在实例, 根据参数strict的值, 进行save, 或提示修改失败
				if(strict) {
					processDefined.setSubversion(pd.getSubversion()+1);
					processDefined.setState(pd.getState());
					SessionContext.getTableSession().save(processDefined); 
					
					if(processDefined.getState() == ProcessDefinition.ENABLED) 
						enable(processDefined, false);
				}
				return new ExecutionResult(null, "修改失败, 流程[%s]已经被使用", "bpm.process.defined.save.fail", processDefined.getName());
			}
		}
		return null;
	}
	
	/**
	 * 删除流程定义信息
	 * @param processDefinitionId
	 * @param terminateAllRunProcessInstance 是否终止所有与当前流程定义相关的运行的流程实例
	 * @return
	 */
	@Transaction
	public ExecutionResult delete(int processDefinitionId, boolean terminateAllRunProcessInstance) {
		ProcessDefinition processDefined = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, state from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefined == null)
			return new ExecutionResult("id", "删除失败, 不存在id=%d的流程定义信息", "bpm.process.defined.delete.fail.unexists", processDefinitionId);
		if(processDefined.getState() == ProcessDefinition.DELETE)
			return new ExecutionResult("id", "删除失败, id=%d的流程定义已经被删除", "bpm.process.defined.delete.fail.already.done", processDefinitionId);
		
		// TODO 如果有实例, 则进行逻辑删除, 如果有运行实例, 根据terminateAllRunProcessInstance参数看是否要终止
		// 没有实例, 就直接进行删除
		if(runtimeProcessInstanceService.existsInstance(processDefinitionId) || historyProcessInstanceService.existsInstance(processDefinitionId)) {
			
		}
			
		return null;
	}
	
	
	
	
	
	
	/**
	 * 启用定义的流程
	 * @param processDefinitionId 
	 * @param activateAllRunProcessInstance 是否激活所有与当前流程定义相关的运行的流程实例
	 * @return
	 */
	@Transaction
	public ExecutionResult enable(int processDefinitionId, boolean activateAllRunProcessInstance) {
		ProcessDefinition processDefined = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, state, content_ from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefined == null)
			return new ExecutionResult("id", "启用失败, 不存在id=%d的流程定义信息", "bpm.process.defined.enable.fail.unexists", processDefinitionId);
		if(processDefined.getState() == ProcessDefinition.ENABLED)
			return new ExecutionResult("id", "启用失败, id=%d的流程定义已经启用", "bpm.process.defined.enable.fail.already.done", processDefinitionId);
		return enable(processDefined, activateAllRunProcessInstance);
	}
	
	// 启用定义的流程
	private ExecutionResult enable(ProcessDefinition processDefined, boolean activateAllRunProcessInstance) {
		SessionContext.getSqlSession().executeUpdate("update bpm_re_procdef set enabled=1 where id=?", Arrays.asList(processDefined.getId()));
		if(activateAllRunProcessInstance)
			return runtimeProcessInstanceService.activateAllProcessInstance(processDefined.getId());
		return null;
	}
	
	/**
	 * 禁用定义的流程
	 * @param processDefinitionId
	 * @param suspendAllRunProcessInstance 是否挂起所有与当前流程定义相关的运行的流程实例
	 * @return
	 */
	@Transaction
	public ExecutionResult disable(int processDefinitionId, boolean suspendAllRunProcessInstance) {
		ProcessDefinition processDefined = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, state from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefined == null)
			return new ExecutionResult("id", "禁用失败, 不存在id=%d的流程定义信息", "bpm.process.defined.disable.fail.unexists", processDefinitionId);
		if(processDefined.getState() == ProcessDefinition.DISABLED)
			return new ExecutionResult("id", "禁用失败, id=%d的流程定义已经被禁用", "bpm.process.defined.disable.fail.already.done", processDefinitionId);
		
		SessionContext.getSqlSession().executeUpdate("update bpm_re_procdef set enabled=0 where id=?", Arrays.asList(processDefinitionId));
		if(suspendAllRunProcessInstance)
			return runtimeProcessInstanceService.suspendAllProcessInstance(processDefinitionId);
		return null;
	}
}
