package com.douglei.bpm.module.repository.deploy;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.annotation.ProcessEngineField;
import com.douglei.bpm.annotation.ProcessEngineTransactionBean;
import com.douglei.bpm.module.common.service.ExecutionResult;
import com.douglei.bpm.module.history.HistoryInstanceService;
import com.douglei.bpm.module.runtime.RuntimeInstanceService;
import com.douglei.orm.configuration.impl.util.XmlReaderContext;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;
import com.douglei.orm.sessionfactory.sessions.session.table.TableSession;
import com.douglei.tools.utils.serialize.JdkSerializeProcessor;

/**
 * 流程部署服务
 * @author DougLei
 */
@ProcessEngineTransactionBean
public class ProcessDeployService {
	
	@ProcessEngineField
	private RuntimeInstanceService runtimeInstance;
	
	@ProcessEngineField
	private HistoryInstanceService historyInstance;
	
	/**
	 * 部署定义的流程
	 * @param processDefinition
	 * @return
	 */
	@Transaction
	public ExecutionResult deploy(ProcessDefinition processDefinition) {
		List<ProcessDefinition> list = SessionContext.getTableSession().query(ProcessDefinition.class, "select id, builtin_version from bpm_re_procdef where code=? and version_=? order by builtin_version desc", Arrays.asList(processDefinition.getCode(), processDefinition.getVersion()));
		ProcessDefinition pd = list.isEmpty()?null:list.get(0);
		
		/**
		 * 进行保存或更新的逻辑判断如下
		 * 
		 * 不存在同code和version的流程定义, 直接保存
		 * 否则判断是否修改了content
		 * 	如果没有修改content, 则就是基础信息的修改, 
		 * 
		 * */
		
		if(pd == null) {
			SessionContext.getTableSession().save(processDefined); 
		} else {
			// 判断是否修改了content, 修改了
			
			
			
			
			
		}
		
		
		
		if(runtimeInstance.existsInstance(pd.getId()) || historyInstance.existsInstance(pd.getId())){ // 旧的流程定义信息, 存在实例, 则要更新内置版本后, 保存新的流程定义信息
			processDefined.setBuiltinVersion(pd.getBuiltinVersion()+1);
			SessionContext.getTableSession().save(processDefined); 
		}else { // 更新流程定义信息
			processDefined.setId(pd.getId());
			processDefined.setBuiltinVersion(pd.getBuiltinVersion());
			SessionContext.getTableSession().update(processDefined); 
		}
		
		// 解析流程配置文件
		
		
		if(processDefined.isEnabled()) 
			return enable(processDefined, false); // 启用流程定义
		return null;
	}
	
	
	// 获取指定id的流程定义信息
	private ProcessDefinition getProcessDefinedById(int processDefinitionId) {
		TableSession tableSession = SessionContext.getTableSession();
		return tableSession.uniqueQuery(ProcessDefinition.class, "select " + tableSession.getColumnNames(ProcessDefinition.class) + " from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
	}
	
	/**
	 * 启用定义的流程
	 * @param processDefinitionId 
	 * @param activateAllRunProcessInstance 是否激活所有与当前流程定义相关的运行的流程实例
	 * @return
	 */
	@Transaction
	public ExecutionResult enable(int processDefinitionId, boolean activateAllRunProcessInstance) {
		ProcessDefinition processDefined = getProcessDefinedById(processDefinitionId);
		if(processDefined == null)
			return new ExecutionResult("id", "启用失败, 不存在id=%d的流程定义信息", "bpm.process.defined.enable.fail.unexists", processDefinitionId);
		if(processDefined.isEnabled())
			return new ExecutionResult("id", "启用失败, id=%d的流程定义已经启用", "bpm.process.defined.enable.fail.already.done", processDefinitionId);
		return enable(processDefined, activateAllRunProcessInstance);
	}
	
	// 启用定义的流程
	private ExecutionResult enable(ProcessDefinition processDefined, boolean activateAllRunProcessInstance) {
		SessionContext.getSqlSession().executeUpdate("update bpm_re_procdef set enabled=1 where id=?", Arrays.asList(processDefined.getId()));
		if(activateAllRunProcessInstance)
			return runtimeInstance.activateAllProcessInstance(processDefined.getId());
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
		ProcessDefinition processDefined = getProcessDefinedById(processDefinitionId);
		if(processDefined == null)
			return new ExecutionResult("id", "禁用失败, 不存在id=%d的流程定义信息", "bpm.process.defined.disable.fail.unexists", processDefinitionId);
		if(!processDefined.isEnabled())
			return new ExecutionResult("id", "禁用失败, id=%d的流程定义已经被禁用", "bpm.process.defined.disable.fail.already.done", processDefinitionId);
		
		SessionContext.getSqlSession().executeUpdate("update bpm_re_procdef set enabled=0 where id=?", Arrays.asList(processDefinitionId));
		if(suspendAllRunProcessInstance)
			return runtimeInstance.suspendAllProcessInstance(processDefinitionId);
		return null;
	}
}
