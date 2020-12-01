package com.douglei.bpm.module.repository.definition;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ProcessObjectException;
import com.douglei.bpm.module.components.instance.InstanceHandlePolicy;
import com.douglei.bpm.module.history.HistoryModule;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.module.repository.definition.entity.builder.ProcessDefinitionBuilder;
import com.douglei.bpm.module.runtime.RuntimeModule;
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
	private RuntimeModule runtimeModule;
	
	@Autowired
	private HistoryModule historyModule;
	
	@Autowired
	private ProcessContainerProxy processContainer;
	
	/**
	 * 更新流程定义的状态
	 * @param processDefinitionId
	 * @param state {@link ProcessDefinitionStateConstants}
	 */
	private void updateState(int processDefinitionId, byte state) {
		SessionContext.getSqlSession().executeUpdate("update bpm_re_procdef set state=? where id=?", Arrays.asList(state, processDefinitionId));
	}
	
	/**
	 * 保存流程定义
	 * @param builder
	 * @param strict 是否要强制保存; 针对有实例的流程定义, 如果为true, 则会创建新的子版本并保存, 否则返回错误信息
	 * @return
	 */
	@Transaction
	public ProcessDefinition insert(ProcessDefinitionBuilder builder, boolean strict) {
		ProcessDefinition processDefinition = builder.build();
		ProcessDefinition exProcessDefinition = SessionContext.getSQLSession().queryFirst(ProcessDefinition.class, "ProcessDefinition", "query4Save", processDefinition);
		if(exProcessDefinition == null) {
			// 新的流程定义, 进行save
			SessionContext.getTableSession().save(processDefinition); 
		}else {
			if(exProcessDefinition.getState() == ProcessDefinition.DELETE)
				throw new ProcessObjectException("保存失败, 已存在code为["+processDefinition.getCode()+"], version为["+processDefinition.getVersion()+"]的流程");
			
			if(exProcessDefinition.getSignature().equals(processDefinition.getSignature())){ // 没有修改流程定义的内容, 进行update
				processDefinition.setId(exProcessDefinition.getId());
				processDefinition.setSubversion(exProcessDefinition.getSubversion());
				processDefinition.setState(exProcessDefinition.getState());
				processDefinition.setContent(null);
				processDefinition.setSignature(null);
				SessionContext.getTableSession().update(processDefinition);
			}else if(!runtimeModule.getInstanceService().exists(exProcessDefinition.getId()) && !historyModule.getInstanceService().exists(exProcessDefinition.getId())) { // 修改了内容, 但旧的流程定义不存在实例, 进行update
				processDefinition.setId(exProcessDefinition.getId());
				processDefinition.setSubversion(exProcessDefinition.getSubversion());
				processDefinition.setState(exProcessDefinition.getState());
				SessionContext.getTableSession().update(processDefinition); 
			}else { // 修改了内容, 且旧的流程定义存在实例, 根据参数strict的值, 进行save, 或提示操作失败
				if(!strict) 
					throw new ProcessObjectException("保存失败, ["+processDefinition.getName()+"]流程已存在实例");
				
				processDefinition.setSubversion(exProcessDefinition.getSubversion()+1);
				processDefinition.setState(exProcessDefinition.getState());
				SessionContext.getTableSession().save(processDefinition); 
			}
		}
		
		if(processDefinition.getState() == ProcessDefinition.DEPLOY) 
			processContainer.addProcess(processDefinition);
		return processDefinition;
	}
	
	/**
	 * 流程部署
	 * @param processDefinitionId 
	 * @param runtimeInstancePolicy 对运行实例的处理策略, 如果传入null, 则不进行任何处理
	 * @return
	 */
	@Transaction
	public int deploy(int processDefinitionId, InstanceHandlePolicy runtimeInstancePolicy) {
		ProcessDefinition processDefinition = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, state, content_ from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefinition == null || processDefinition.getState() == ProcessDefinition.DELETE)
			throw new ProcessObjectException("部署失败, 不存在id为["+processDefinitionId+"]的流程");
		
		if(processDefinition.getState() != ProcessDefinition.DEPLOY) {
			if(runtimeInstancePolicy != null && runtimeModule.getInstanceService().exists(processDefinitionId))
				runtimeModule.getInstanceService().handle(processDefinitionId, runtimeInstancePolicy);
			
			updateState(processDefinitionId, ProcessDefinition.DEPLOY);
			processContainer.addProcess(processDefinition);
		}
		return processDefinitionId;
	}
	
	/**
	 * 取消流程部署
	 * @param processDefinitionId
	 * @param runtimeInstancePolicy 对运行实例的处理策略, 如果传入null, 则不进行任何处理
	 * @param historyInstancePolicy 对历史实例的处理策略, 如果传入null, 则不进行任何处理
	 * @return
	 */
	@Transaction
	public int undeploy(int processDefinitionId, InstanceHandlePolicy runtimeInstancePolicy, InstanceHandlePolicy historyInstancePolicy) {
		ProcessDefinition processDefinition = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, code, version, subversion, state from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefinition == null || processDefinition.getState() == ProcessDefinition.DELETE)
			throw new ProcessObjectException("取消部署失败, 不存在id为["+processDefinitionId+"]的流程");
		
		if(processDefinition.getState() != ProcessDefinition.UNDEPLOY) {
			if(runtimeInstancePolicy != null && runtimeModule.getInstanceService().exists(processDefinitionId)) 
				runtimeModule.getInstanceService().handle(processDefinitionId, runtimeInstancePolicy);
			if(historyInstancePolicy != null && historyModule.getInstanceService().exists(processDefinitionId)) 
				historyModule.getInstanceService().handle(processDefinitionId, historyInstancePolicy);
			
			updateState(processDefinitionId, ProcessDefinition.UNDEPLOY);
			processContainer.deleteProcess(processDefinitionId);
		}
		return processDefinitionId;
	}
	
	/**
	 * 删除流程定义
	 * @param processDefinitionId
	 * @param strict 是否强制删除; 针对有实例的流程定义, 如果为true, 则会修改流程定义的状态为删除(逻辑删除), 否则返回错误信息
	 * @return
	 */
	@Transaction
	public int delete(int processDefinitionId, boolean strict) {
		List<Object> paramList = Arrays.asList(processDefinitionId);
		
		ProcessDefinition processDefinition = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select name, state from bpm_re_procdef where id=?", paramList);
		if(processDefinition == null || processDefinition.getState() == ProcessDefinition.DELETE)
			throw new ProcessObjectException("删除失败, 不存在id为["+processDefinitionId+"]的流程");
		if(processDefinition.getState() == ProcessDefinition.DEPLOY)
			throw new ProcessObjectException("删除失败, ["+processDefinition.getName()+"]流程已部署, 请先取消部署");
		
		if(runtimeModule.getInstanceService().exists(processDefinitionId) || historyModule.getInstanceService().exists(processDefinitionId)) {
			if(!strict)
				throw new ProcessObjectException("删除失败, ["+processDefinition.getName()+"]流程已存在实例");
			updateState(processDefinitionId, ProcessDefinition.DELETE);
		} else {
			SessionContext.getSqlSession().executeUpdate("delete bpm_re_procdef where id=?", paramList);
		}
		return processDefinitionId;
	}
}
