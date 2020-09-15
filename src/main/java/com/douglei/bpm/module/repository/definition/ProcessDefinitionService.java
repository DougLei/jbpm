package com.douglei.bpm.module.repository.definition;

import java.util.Arrays;

import com.douglei.bpm.annotation.ProcessEngineField;
import com.douglei.bpm.annotation.ProcessEngineTransactionBean;
import com.douglei.bpm.core.process.ProcessHandler;
import com.douglei.bpm.module.common.service.ExecutionResult;
import com.douglei.bpm.module.history.HistoryProcessInstanceService;
import com.douglei.bpm.module.runtime.InstanceProcessingPolicy;
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
	
	private ProcessHandler processHandler;
	
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
				return new ExecutionResult(null, "code=%s, version=%s的标识, 已被其他流程定义使用, 请更换", "bpm.process.defined.save.code.version.exists", processDefined.getCode(), processDefined.getVersion());
			
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
				
				if(processDefined.getState() == ProcessDefinition.PUBLISHED) 
					publishing(processDefined, null);
			}else {
				// 修改了内容, 且旧的流程定义存在实例, 根据参数strict的值, 进行save, 或提示修改失败
				if(strict) {
					processDefined.setSubversion(pd.getSubversion()+1);
					processDefined.setState(pd.getState());
					SessionContext.getTableSession().save(processDefined); 
					
					if(processDefined.getState() == ProcessDefinition.PUBLISHED) 
						publishing(processDefined, null);
				}
				return new ExecutionResult(null, "修改失败, 流程[%s]已经被使用", "bpm.process.defined.save.fail", processDefined.getName());
			}
		}
		return null;
	}
	
	/**
	 * 更新流程定义的状态
	 * @param processDefinitionId
	 * @param state {@link ProcessDefinition.UNPUBLISHED}, {@link ProcessDefinition.PUBLISHED}, {@link ProcessDefinition.DELETE}
	 */
	private void updateState(int processDefinitionId, int state) {
		SessionContext.getSqlSession().executeUpdate("update bpm_re_procdef set state=? where id=?", Arrays.asList(state, processDefinitionId));
	}
	
	/**
	 * 发布定义的流程
	 * @param processDefinitionId 
	 * @param policy 对运行实例的处理策略
	 * @return
	 */
	@Transaction
	public ExecutionResult publishing(int processDefinitionId, InstanceProcessingPolicy policy) {
		ProcessDefinition processDefined = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, state, content_ from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefined == null || processDefined.getState() == ProcessDefinition.DELETE)
			return new ExecutionResult("id", "发布失败, 不存在id=%d的流程定义信息", "bpm.process.defined.publishing.fail.unexists", processDefinitionId);
		if(processDefined.getState() == ProcessDefinition.PUBLISHED)
			return new ExecutionResult("id", "发布失败, id=%d的流程定义已经发布", "bpm.process.defined.publishing.fail.already.done", processDefinitionId);
		return publishing(processDefined, policy);
	}
	
	/**
	 * 发布定义的流程
	 * @param processDefined
	 * @param policy
	 * @return
	 */
	private ExecutionResult publishing(ProcessDefinition processDefined, InstanceProcessingPolicy policy) {
		processHandler.parse(processDefined.getContent(), processDefined.getSubversion());
		updateState(processDefined.getId(), ProcessDefinition.PUBLISHED);
		if(policy != null)
			return runtimeProcessInstanceService.processingAllInstance(processDefined.getId(), policy);
		return null;
	}
	
	/**
	 * 取消发布定义的流程
	 * @param processDefinitionId
	 * @param policy 对运行实例的处理策略
	 * @return
	 */
	@Transaction
	public ExecutionResult cancelPublishing(int processDefinitionId, InstanceProcessingPolicy policy) {
		ProcessDefinition processDefined = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, state from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefined == null || processDefined.getState() == ProcessDefinition.DELETE)
			return new ExecutionResult("id", "取消发布失败, 不存在id=%d的流程定义信息", "bpm.process.defined.cancel.publishing.fail.unexists", processDefinitionId);
		if(processDefined.getState() == ProcessDefinition.UNPUBLISHED)
			return new ExecutionResult("id", "取消发布失败, id=%d的流程定义已经处于未发布状态", "bpm.process.defined.cancel.publishing.fail.already.done", processDefinitionId);
		
		updateState(processDefined.getId(), ProcessDefinition.UNPUBLISHED);
		processHandler.delete(processDefined.getCode(), processDefined.getVersion(), processDefined.getSubversion());
		if(policy != null)
			return runtimeProcessInstanceService.processingAllInstance(processDefined.getId(), policy);
		return null;
	}
	
	/**
	 * 删除流程定义信息
	 * @param processDefinitionId
	 * @param policy 对运行实例的处理策略
	 * @return
	 */
	@Transaction
	public ExecutionResult delete(int processDefinitionId, InstanceProcessingPolicy policy) {
		ProcessDefinition processDefined = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, state from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefined == null || processDefined.getState() == ProcessDefinition.DELETE)
			return new ExecutionResult("id", "删除失败, 不存在id=%d的流程定义信息", "bpm.process.defined.delete.fail.unexists", processDefinitionId);
		
		ExecutionResult result = null;
		boolean existsInstance = runtimeProcessInstanceService.existsInstance(processDefinitionId);
		if(existsInstance)
			result = runtimeProcessInstanceService.processingAllInstance(processDefined.getId(), policy);
		
		if(result == null) {
			if(existsInstance || historyProcessInstanceService.existsInstance(processDefinitionId)) {
				updateState(processDefinitionId, ProcessDefinition.DELETE);
			} else {
				SessionContext.getSqlSession().executeUpdate("delete bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
				processHandler.delete(processDefined.getCode(), processDefined.getVersion(), processDefined.getSubversion());
			}
		}
		return result;
	}
	
	
	/**
	 * 获取指定id的流程实例
	 * @param code
	 * @return 
	 */
	@Transaction
	public ExecutionResult get(int processDefinitionId) {
		ProcessDefinition processDefined = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, code, version, subversion from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefined == null)
			return new ExecutionResult("id", "获取失败, 不存在id=%d的流程定义信息", "bpm.process.defined.get.fail.unexists", processDefinitionId);
		if(processDefined.getState() == ProcessDefinition.UNPUBLISHED)
			return new ExecutionResult("id", "获取失败, id=%d的流程定义信息还未发布", "bpm.process.defined.get.fail.unpublished", processDefinitionId);
		return get(processDefined.getCode(), processDefined.getVersion(), processDefined.getSubversion());
	}
	
	/**
	 * 获取指定code, 最新version和subversion的流程实例
	 * @param code
	 * @return 
	 */
	@Transaction
	public ExecutionResult get(String code) {
		return get(code, null, -1);
	}
	
	/**
	 * 获取指定code和version, 最新subversion的流程实例
	 * @param code
	 * @param version
	 * @return 
	 */
	@Transaction
	public ExecutionResult get(String code, String version) {
		return get(code, version, -1);
	}
	

	/**
	 * 获取指定code, version和subversion的流程实例
	 * @param code
	 * @param version
	 * @param subversion -1表示获取最新子版本的流程实例
	 * @return 
	 */
	@Transaction
	public ExecutionResult get(String code, String version, int subversion) {
		// TODO 如果没有查询到, 还要去查询再获取, 以及对定义状态的判断
		
		
		return null;
	}
}
