package com.douglei.bpm.module.repository.definition;

import java.util.Arrays;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.instance.HistoryProcessInstanceService;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.module.repository.definition.entity.State;
import com.douglei.bpm.module.runtime.instance.ProcessInstanceHandlePolicy;
import com.douglei.bpm.module.runtime.instance.ProcessInstanceService;
import com.douglei.bpm.process.container.ProcessContainerProxy;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 流程定义服务
 * @author DougLei
 */
@Bean(isTransaction = true)
public class ProcessDefinitionService {
	
	@Autowired
	private ProcessInstanceService processInstanceService;
	
	@Autowired
	private HistoryProcessInstanceService historyProcessInstanceService;
	
	@Autowired
	private ProcessContainerProxy processContainer;
	
	/**
	 * 保存流程定义
	 * @param builder
	 * @return
	 */
	@Transaction
	public ExecutionResult insert(ProcessDefinitionBuilder builder) {
		// TODO 增加处理最新版本的功能
		ProcessDefinition processDefinition = builder.getProcessDefinition();
		
		ProcessDefinition exProcessDefinition = SessionContext.getSQLSession().uniqueQuery(ProcessDefinition.class, "ProcessDefinition", "query4Save", processDefinition);
		if(exProcessDefinition == null) {
			// 新的流程定义, 进行save
			SessionContext.getTableSession().save(processDefinition); 
		}else {
			if(exProcessDefinition.getStateInstance() == State.DELETE)
				return new ExecutionResult("保存失败, 已存在code为["+processDefinition.getCode()+"], version为["+processDefinition.getVersion()+"]的流程");
			
			if(exProcessDefinition.getSignature().equals(processDefinition.getSignature())){ // 没有修改流程定义的内容, 进行update
				processDefinition.setId(exProcessDefinition.getId());
				processDefinition.setNewest(exProcessDefinition.getNewest());
				processDefinition.setSubversion(exProcessDefinition.getSubversion());
				processDefinition.setStateInstance(exProcessDefinition.getStateInstance());
				processDefinition.setContent(null);
				processDefinition.setSignature(null);
				SessionContext.getTableSession().update(processDefinition);
			}else if(exProcessDefinition.getStateInstance() == State.INITIAL 
					|| (!processInstanceService.exists(exProcessDefinition) && !historyProcessInstanceService.exists(exProcessDefinition))) { // 修改了内容, 但旧的流程不存在实例, 进行update
				processDefinition.setId(exProcessDefinition.getId());
				processDefinition.setNewest(exProcessDefinition.getNewest());
				processDefinition.setSubversion(exProcessDefinition.getSubversion());
				processDefinition.setStateInstance(exProcessDefinition.getStateInstance());
				SessionContext.getTableSession().update(processDefinition); 
			}else { // 修改了内容, 且旧的流程定义存在实例, 根据strict值, 进行升级save, 或提示操作失败
				if(!builder.isStrict()) 
					return new ExecutionResult("保存失败, ["+processDefinition.getName()+"]流程已存在实例");
				
				processDefinition.setSubversion(exProcessDefinition.getSubversion()+1);
				processDefinition.setStateInstance(exProcessDefinition.getStateInstance());
				SessionContext.getTableSession().save(processDefinition);
				
				// 修改旧的流程定义状态, 关联的流程类型id, 以及是否最新标识
				exProcessDefinition.setStateInstance(State.INVALID);
				SessionContext.getTableSession().update(exProcessDefinition);
			}
		}
		return new ExecutionResult(processDefinition);
	}
	
	/**
	 * 流程部署
	 * @param processDefinitionId 
	 * @param runtimeProcessInstanceHandlePolicy 对运行实例的处理策略, 如果传入null, 则不进行任何处理
	 * @return
	 */
	@Transaction
	public ExecutionResult deploy(int processDefinitionId, ProcessInstanceHandlePolicy runtimeProcessInstanceHandlePolicy) {
		ProcessDefinition processDefinition = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select name, code, version, subnewest, state, content_, tenant_id from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefinition == null)
			return new ExecutionResult("部署失败, 不存在id为["+processDefinitionId+"]的流程");
		if(!processDefinition.isSubnewest())
			return new ExecutionResult("部署失败, ["+processDefinition.getName()+"]流程非最新子版本");
		if(!processDefinition.getStateInstance().supportDeploy())
			return new ExecutionResult("部署失败, ["+processDefinition.getName()+"]流程处于["+processDefinition.getState()+"]状态");
		
		if(runtimeProcessInstanceHandlePolicy != null && processInstanceService.exists(processDefinition))
			processInstanceService.handle(processDefinition, runtimeProcessInstanceHandlePolicy);
		
		updateState_(processDefinitionId, State.DEPLOY);
		processContainer.addProcess(processDefinition);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 取消流程部署
	 * @param processDefinitionId
	 * @param runtimeProcessInstanceHandlePolicy 对运行实例的处理策略, 如果传入null, 则不进行任何处理
	 * @param historyProcessInstanceHandlePolicy 对历史实例的处理策略, 如果传入null, 则不进行任何处理
	 * @return
	 */
	@Transaction
	public ExecutionResult undeploy(int processDefinitionId, ProcessInstanceHandlePolicy runtimeProcessInstanceHandlePolicy, ProcessInstanceHandlePolicy historyProcessInstanceHandlePolicy) {
		ProcessDefinition processDefinition = SessionContext.getSqlSession().uniqueQuery(ProcessDefinition.class, "select name, code, version, subnewest, state, tenant_id from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefinition == null)
			return new ExecutionResult("取消部署失败, 不存在id为["+processDefinitionId+"]的流程");
		if(!processDefinition.isSubnewest())
			return new ExecutionResult("取消部署失败, ["+processDefinition.getName()+"]流程非最新子版本");
		if(!processDefinition.getStateInstance().supportUnDeploy())
			return new ExecutionResult("取消部署失败, ["+processDefinition.getName()+"]流程处于["+processDefinition.getState()+"]状态");
		
		if(runtimeProcessInstanceHandlePolicy != null && processInstanceService.exists(processDefinition)) 
			processInstanceService.handle(processDefinition, runtimeProcessInstanceHandlePolicy);
		if(historyProcessInstanceHandlePolicy != null && historyProcessInstanceService.exists(processDefinition)) 
			historyProcessInstanceService.handle(processDefinition, historyProcessInstanceHandlePolicy);
		
		updateState_(processDefinitionId, State.UNDEPLOY);
		processContainer.deleteProcess(processDefinitionId);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 删除流程定义
	 * @param processDefinitionId
	 * @param strict 是否强制删除; 针对有实例的流程定义, 如果为true, 则会修改流程定义的状态为删除(逻辑删除), 否则返回错误信息
	 * @return
	 */
	@Transaction
	public ExecutionResult delete(int processDefinitionId, boolean strict) {
		// TODO 增加处理最新版本的功能
		ProcessDefinition processDefinition = SessionContext.getSqlSession().uniqueQuery(ProcessDefinition.class, "select name, code, version, subnewest, state, tenant_id from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefinition == null)
			return new ExecutionResult("删除失败, 不存在id为["+processDefinitionId+"]的流程");
		if(!processDefinition.isSubnewest())
			return new ExecutionResult("删除失败, ["+processDefinition.getName()+"]流程非最新子版本");
		if(!processDefinition.getStateInstance().supportDelete())
			return new ExecutionResult("删除失败, ["+processDefinition.getName()+"]流程处于["+processDefinition.getState()+"]状态");
		
		if(processInstanceService.exists(processDefinition) || historyProcessInstanceService.exists(processDefinition)) {
			if(!strict)
				return new ExecutionResult("删除失败, ["+processDefinition.getName()+"]流程已存在实例");
			updateState_(processDefinitionId, State.DELETE);
		} else {
			SessionContext.getSQLSession().executeUpdate("ProcessDefinition", "delete", processDefinition);
		}
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 物理删除流程定义; 如果存在实例, 会将所有实例都删除
	 * @param processDefinitionId
	 * @return
	 */
	@Transaction
	public ExecutionResult delete4Physical(int processDefinitionId) {
		// TODO 增加处理最新版本的功能
		ProcessDefinition processDefinition = SessionContext.getSqlSession().uniqueQuery(ProcessDefinition.class, "select id, type_id, name, code, version, subversion, subnewest, state, tenant_id from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefinition == null)
			return new ExecutionResult("删除失败, 不存在id为["+processDefinitionId+"]的流程");
		if(!processDefinition.getStateInstance().supportPhysicalDelete())
			return new ExecutionResult("删除失败, ["+processDefinition.getName()+"]流程处于["+processDefinition.getState()+"]状态");
		
		if(processInstanceService.exists(processDefinition))
			processInstanceService.handle(processDefinition, ProcessInstanceHandlePolicy.DELETE);
		if(historyProcessInstanceService.exists(processDefinition))
			historyProcessInstanceService.handle(processDefinition, ProcessInstanceHandlePolicy.DELETE);
		SessionContext.getSQLSession().executeUpdate("ProcessDefinition", "delete", processDefinition);
		
		if(processDefinition.isSubnewest() && processDefinition.getSubversion() > 0) { // 如果被删除的流程是最新子版本, 且子版本值不为0, 需要将上一个子版本的流程自动设置为最新子版本
			ProcessDefinition beforeProcessDefinition = SessionContext.getSQLSession().queryFirst(ProcessDefinition.class, "ProcessDefinition", "querySubversions", processDefinition);
			
			if(beforeProcessDefinition != null) {
				beforeProcessDefinition.setTypeId(processDefinition.getTypeId());
				beforeProcessDefinition.setSubnewest(1);
				beforeProcessDefinition.setStateInstance(processDefinition.getStateInstance());
				SessionContext.getTableSession().update(beforeProcessDefinition);
			}
		}
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 设置流程定义的最新版本
	 * @param processDefinitionId
	 * @return
	 */
	public ExecutionResult setNewest(int processDefinitionId) {
		// TODO 增加处理最新版本的功能
		
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 设置流程定义的最新子版本
	 * @param processDefinitionId
	 * @return
	 */
	@Transaction
	public ExecutionResult setSubnewest(int processDefinitionId) {
		ProcessDefinition targetProcessDefinition = SessionContext.getSqlSession().uniqueQuery(ProcessDefinition.class, "select id, name, code, version, subversion, subnewest, state, tenant_id from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(targetProcessDefinition == null)
			return new ExecutionResult("设置最新子版本失败, 不存在id为["+processDefinitionId+"]的流程");
		if(targetProcessDefinition.isSubnewest())
			return new ExecutionResult("设置最新子版本失败, ["+targetProcessDefinition.getName()+"]流程就是最新子版本");
		
		// 进行必要的数据交换后, 执行update
		ProcessDefinition subnewestProcessDefinition = SessionContext.getSQLSession().uniqueQuery(ProcessDefinition.class, "ProcessDefinition", "querySubnewest", targetProcessDefinition);
		int typeId = subnewestProcessDefinition.getTypeId();
		int newest = subnewestProcessDefinition.getNewest();
		State state = subnewestProcessDefinition.getStateInstance();
		
		subnewestProcessDefinition.setTypeId(0);
		subnewestProcessDefinition.setNewest(0);
		subnewestProcessDefinition.setSubnewest(0);
		subnewestProcessDefinition.setStateInstance(targetProcessDefinition.getStateInstance()); // targetProcessDefinition的state, 逻辑上只能是State.INVALID
		SessionContext.getTableSession().update(subnewestProcessDefinition);
		
		targetProcessDefinition.setTypeId(typeId);
		targetProcessDefinition.setNewest(newest);
		targetProcessDefinition.setSubnewest(1);
		targetProcessDefinition.setSubversion(subnewestProcessDefinition.getSubversion()+1);
		targetProcessDefinition.setStateInstance(state);
		SessionContext.getTableSession().update(targetProcessDefinition);
		
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 还原流程的状态, 从{@link State.DELETE}还原为{@link State.UNDEPLOY}
	 * @param processDefinitionId
	 * @param targetState
	 * @return
	 */
	@Transaction
	public ExecutionResult restoreStateDelete2UnDeploy(int processDefinitionId) {
		ProcessDefinition processDefinition = SessionContext.getSqlSession().uniqueQuery(ProcessDefinition.class, "select name, subnewest, state from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefinition == null)
			return new ExecutionResult("还原流程状态失败, 不存在id为["+processDefinitionId+"]的流程");
		if(!processDefinition.isSubnewest())
			return new ExecutionResult("还原流程状态失败, ["+processDefinition.getName()+"]流程非最新子版本");
		if(processDefinition.getStateInstance() != State.DELETE)
			return new ExecutionResult("还原流程状态失败, 不能将["+processDefinition.getState()+"]状态还原为["+State.UNDEPLOY+"]状态");

		updateState_(processDefinitionId, State.UNDEPLOY);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 修改流程的状态
	 * @param processDefinitionId
	 * @param targetState
	 * @return
	 */
	@Transaction
	public ExecutionResult updateState(int processDefinitionId, State targetState) {
		ProcessDefinition processDefinition = SessionContext.getSqlSession().uniqueQuery(ProcessDefinition.class, "select name, subnewest, state from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefinition == null)
			return new ExecutionResult("修改流程状态失败, 不存在id为["+processDefinitionId+"]的流程");
		if(!processDefinition.isSubnewest())
			return new ExecutionResult("修改流程状态失败, ["+processDefinition.getName()+"]流程非最新子版本");
		if(!processDefinition.getStateInstance().supportConvert(targetState))
			return new ExecutionResult("修改流程状态失败, 不能将["+processDefinition.getState()+"]状态更新为["+targetState+"]状态");

		updateState_(processDefinitionId, targetState);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	private void updateState_(int processDefinitionId, State state) {
		SessionContext.getSqlSession().executeUpdate("update bpm_re_procdef set state=? where id=?", Arrays.asList(state.name(), processDefinitionId));
	}
}
