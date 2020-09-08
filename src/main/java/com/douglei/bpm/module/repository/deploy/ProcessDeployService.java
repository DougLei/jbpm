package com.douglei.bpm.module.repository.deploy;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.annotation.ProcessEngineField;
import com.douglei.bpm.annotation.ProcessEngineTransactionBean;
import com.douglei.bpm.module.common.service.ExecutionResult;
import com.douglei.bpm.module.history.HistoryInstanceService;
import com.douglei.bpm.module.runtime.RuntimeInstanceService;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;
import com.douglei.orm.sessionfactory.sessions.session.table.TableSession;

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
	 * @param processDefined
	 * @return
	 */
	@Transaction
	public ExecutionResult deploy(ProcessDefined processDefined) {
		List<ProcessDefined> list = SessionContext.getTableSession().query(ProcessDefined.class, "select id, builtin_version from bpm_re_procdef where code=? and version_=? order by builtin_version desc", Arrays.asList(processDefined.getCode(), processDefined.getVersion()));
		ProcessDefined pd = list.isEmpty()?null:list.get(0);
		
		if(pd == null) { // 一条新的流程定义信息
			SessionContext.getTableSession().save(processDefined); 
		}else if(runtimeInstance.existsInstance(pd.getId()) || historyInstance.existsInstance(pd.getId())){ // 旧的流程定义信息, 存在实例, 则要更新内置版本后, 保存新的流程定义信息
			processDefined.setBuiltinVersion(pd.getBuiltinVersion()+1);
			SessionContext.getTableSession().save(processDefined); 
		}else { // 更新流程定义信息
			processDefined.setId(pd.getId());
			processDefined.setBuiltinVersion(pd.getBuiltinVersion());
			SessionContext.getTableSession().update(processDefined); 
		}
		
		if(processDefined.isEnabled()) 
			return enable(processDefined, false); // 启用流程定义
		return null;
	}
	
	
	// 获取指定id的流程定义信息
	private ProcessDefined getProcessDefinedById(int processDefinedId) {
		TableSession tableSession = SessionContext.getTableSession();
		return tableSession.uniqueQuery(ProcessDefined.class, "select " + tableSession.getColumnNames(ProcessDefined.class) + " from bpm_re_procdef where id=?", Arrays.asList(processDefinedId));
	}
	
	// 启用定义的流程
	private ExecutionResult enable(ProcessDefined processDefined, boolean activateAllRunProcessInstance) {
		// 1. 将状态改为启用
		// 2. 解析流程的配置文件
		// 3. 如果需要激活流程实例, 则要激活
		return null;
	}
	
	/**
	 * 启用定义的流程
	 * @param processDefinedId 
	 * @param activateAllRunProcessInstance 是否激活所有运行的流程实例
	 * @return
	 */
	@Transaction
	public ExecutionResult enable(int processDefinedId, boolean activateAllRunProcessInstance) {
		ProcessDefined processDefined = getProcessDefinedById(processDefinedId);
		if(processDefined == null)
			return new ExecutionResult("id", "启用失败, 不存在id=%d的流程定义信息", "bpm.process.defined.enable.fail", processDefinedId);
		return enable(processDefined, activateAllRunProcessInstance);
	}
	
	/**
	 * 禁用定义的流程
	 * @param processDefinedId
	 * @param suspendAllRunProcessInstance 是否挂起所有运行的流程实例
	 * @return
	 */
	@Transaction
	public ExecutionResult disable(int processDefinedId, boolean suspendAllRunProcessInstance) {
		ProcessDefined processDefined = getProcessDefinedById(processDefinedId);
		if(processDefined == null)
			return new ExecutionResult("id", "禁用失败, 不存在id=%d的流程定义信息", "bpm.process.defined.disable.fail", processDefinedId);
		
		// 1.将状态改为启用
		// 2. 如果需要挂起所有运行实例, 则要执行
		return null;
	}
}
