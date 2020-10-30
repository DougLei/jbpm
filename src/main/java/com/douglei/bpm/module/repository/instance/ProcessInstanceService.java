package com.douglei.bpm.module.repository.instance;

import java.util.Arrays;

import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.common.service.ExecutionResult;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.bpm.process.ProcessHandler;
import com.douglei.bpm.process.executer.Process;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 流程实例服务
 * @author DougLei
 */
@Bean
public class ProcessInstanceService {
	
	@Attribute
	private ProcessHandler processHandler;
	
	/**
	 * 获取指定id的流程
	 * @param code
	 * @return 
	 */
	@Transaction
	public ExecutionResult<Process> getProcessInstance(int processDefinitionId) {
		ProcessDefinition processDefined = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, code, version, subversion from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefined == null)
			return new ExecutionResult<Process>("id", "获取失败, 不存在id=%d的流程定义信息", "bpm.process.defined.get.fail.unexists", processDefinitionId);
		if(processDefined.getState() == ProcessDefinition.UNPUBLISHED)
			return new ExecutionResult<Process>("id", "获取失败, id=%d的流程定义信息还未发布", "bpm.process.defined.get.fail.unpublished", processDefinitionId);
		return get(processDefined.getCode(), processDefined.getVersion(), processDefined.getSubversion());
	}
	
	/**
	 * 获取指定code, version和subversion的流程实例
	 * @param code
	 * @param version
	 * @param subversion -1表示获取最新子版本的流程实例
	 * @return 
	 */
	@Transaction
	public ExecutionResult<Process> get(String code, String version, int subversion) {
		// TODO 如果没有查询到, 还要去查询再获取, 以及对定义状态的判断
		
		
		return null;
	}
}
