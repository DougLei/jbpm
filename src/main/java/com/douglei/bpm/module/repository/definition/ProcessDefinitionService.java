package com.douglei.bpm.module.repository.definition;

import java.util.Arrays;

import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.core.process.ProcessHandler;
import com.douglei.bpm.module.common.service.ExecutionResult;
import com.douglei.bpm.module.history.HistoryProcessInstanceService;
import com.douglei.bpm.module.runtime.InstanceProcessingPolicy;
import com.douglei.bpm.module.runtime.RuntimeProcessInstanceService;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;
import com.douglei.tools.utils.StringUtil;

/**
 * 流程定义服务
 * @author DougLei
 */
@Bean()
public class ProcessDefinitionService {

	@Attribute
	private RuntimeProcessInstanceService runtimeProcessInstanceService;
	
	@Attribute
	private HistoryProcessInstanceService historyProcessInstanceService;
	
	@Attribute
	private ProcessHandler processHandler;
	
	/**
	 * 保存流程定义信息
	 * @param processDefined 推荐使用 {@link ProcessDefinitionBuilder} 来构建 {@link ProcessDefinition} 实例
	 * @param strict 是否要强制保存流程定义; 针对保存的流程定义有实例的情况, 如果为true, 则会创建新的子版本信息保存, 否则会返回错误信息
	 * @return
	 */
	@Transaction
	public ExecutionResult<Object> save(ProcessDefinition processDefined, boolean strict) {
		if(StringUtil.isEmpty(processDefined.getCode()))
			return new ExecutionResult<Object>("code", "流程定义中的编码值不能为空", "bpm.process.defined.save.code.notnull");
		if(StringUtil.isEmpty(processDefined.getVersion()))
			return new ExecutionResult<Object>("version", "流程定义中的版本值不能为空", "bpm.process.defined.save.version.notnull");

		ProcessDefinition pd = SessionContext.getTableSession().queryFirst(ProcessDefinition.class, "select id, subversion, signature, state from bpm_re_procdef where code=? and version=? order by subversion desc", Arrays.asList(processDefined.getCode(), processDefined.getVersion()));
		if(pd == null) {
			// 新的流程定义, 进行save
			SessionContext.getTableSession().save(processDefined); 
		}else {
			if(pd.getState() == ProcessDefinition.DELETE)
				return new ExecutionResult<Object>(null, "code=%s, version=%s的标识, 已被其他流程定义使用, 请更换", "bpm.process.defined.save.code.version.exists", processDefined.getCode(), processDefined.getVersion());
			
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
				return new ExecutionResult<Object>(null, "修改失败, 流程[%s]已经被使用", "bpm.process.defined.save.fail", processDefined.getName());
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
	public ExecutionResult<Object> publishing(int processDefinitionId, InstanceProcessingPolicy policy) {
		ProcessDefinition processDefined = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, state, content_ from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefined == null || processDefined.getState() == ProcessDefinition.DELETE)
			return new ExecutionResult<Object>("id", "发布失败, 不存在id=%d的流程定义信息", "bpm.process.defined.publishing.fail.unexists", processDefinitionId);
		if(processDefined.getState() == ProcessDefinition.PUBLISHED)
			return new ExecutionResult<Object>("id", "发布失败, id=%d的流程定义已经发布", "bpm.process.defined.publishing.fail.already.done", processDefinitionId);
		return publishing(processDefined, policy);
	}
	
	/**
	 * 发布定义的流程
	 * @param processDefined
	 * @param policy 对运行实例的处理策略
	 * @return
	 */
	private ExecutionResult<Object> publishing(ProcessDefinition processDefined, InstanceProcessingPolicy policy) {
		if(runtimeProcessInstanceService.existsInstance(processDefined.getId())) {
			if(policy == null)
				return new ExecutionResult<Object>("id", "发布失败, id=%d的流程定义存在运行实例, 必须指定如何处理这些实例", "bpm.process.defined.publishing.fail.policy.isnull", processDefined.getId());
			runtimeProcessInstanceService.processingAllInstance(processDefined.getId(), policy);
		}
		
		processHandler.parse(processDefined.getContent(), processDefined.getSubversion());
		updateState(processDefined.getId(), ProcessDefinition.PUBLISHED);
		return null;
	}
	
	/**
	 * 取消发布定义的流程
	 * @param processDefinitionId
	 * @param policy 对运行实例的处理策略
	 * @return
	 */
	@Transaction
	public ExecutionResult<Object> cancelPublishing(int processDefinitionId, InstanceProcessingPolicy policy) {
		ProcessDefinition processDefined = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, code, version, subversion, state from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefined == null || processDefined.getState() == ProcessDefinition.DELETE)
			return new ExecutionResult<Object>("id", "取消发布失败, 不存在id=%d的流程定义信息", "bpm.process.defined.cancel.publishing.fail.unexists", processDefinitionId);
		if(processDefined.getState() == ProcessDefinition.UNPUBLISHED)
			return new ExecutionResult<Object>("id", "取消发布失败, id=%d的流程定义已经处于未发布状态", "bpm.process.defined.cancel.publishing.fail.already.done", processDefinitionId);
		
		if(runtimeProcessInstanceService.existsInstance(processDefined.getId())) {
			if(policy == null)
				return new ExecutionResult<Object>("id", "取消发布失败, id=%d的流程定义存在运行实例, 必须指定如何处理这些实例", "bpm.process.defined.cancel.publishing.fail.policy.isnull", processDefined.getId());
			runtimeProcessInstanceService.processingAllInstance(processDefined.getId(), policy);
		}
		
		processHandler.delete(processDefined);
		updateState(processDefined.getId(), ProcessDefinition.UNPUBLISHED);
		return null;
	}
	
	/**
	 * 删除流程定义信息
	 * @param processDefinitionId
	 * @param policy 对运行实例的处理策略
	 * @return
	 */
	@Transaction
	public ExecutionResult<Object> delete(int processDefinitionId, InstanceProcessingPolicy policy) {
		ProcessDefinition processDefined = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, code, version, subversion, state from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefined == null || processDefined.getState() == ProcessDefinition.DELETE)
			return new ExecutionResult<Object>("id", "删除失败, 不存在id=%d的流程定义信息", "bpm.process.defined.delete.fail.unexists", processDefinitionId);
		
		boolean existsInstance = runtimeProcessInstanceService.existsInstance(processDefinitionId);
		if(existsInstance) {
			if(policy == null)
				return new ExecutionResult<Object>("id", "删除失败, id=%d的流程定义存在运行实例, 必须指定如何处理这些实例", "bpm.process.defined.delete.fail.policy.isnull", processDefined.getId());
			runtimeProcessInstanceService.processingAllInstance(processDefined.getId(), policy);
		}
		
		if(existsInstance || historyProcessInstanceService.existsInstance(processDefinitionId)) {
			updateState(processDefinitionId, ProcessDefinition.DELETE);
		} else {
			SessionContext.getSqlSession().executeUpdate("delete bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
			processHandler.delete(processDefined);
		}
		return null;
	}
}
