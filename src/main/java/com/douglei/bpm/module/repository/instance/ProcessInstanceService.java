package com.douglei.bpm.module.repository.instance;

import java.util.Arrays;

import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.bpm.module.repository.definition.StateConstants;
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
	 * @param id
	 * @return 
	 */
	@Transaction
	public Process getProcessInstance(int processDefinitionId) {
		ProcessDefinition processDefined = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select code, version, subversion from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefined == null)
			throw new NullPointerException("操作失败, 不存在id为["+processDefinitionId+"]的流程定义");
		if(processDefined.getState() == StateConstants.UNPUBLISHED)
			throw new IllegalArgumentException("操作失败, id为["+processDefinitionId+"]的流程定义还未发布");
		return get(processDefined);
	}
	
	/**
	 * 获取指定code, version和subversion的流程
	 * @param code
	 * @param version
	 * @return 
	 */
	@Transaction
	public Process get(String code, String version) {
		ProcessDefinition processDefined = SessionContext.getTableSession().queryFirst(ProcessDefinition.class, "select code, version, subversion from bpm_re_procdef where code=? and version=? order by subversion desc", Arrays.asList(code, version));
		if(processDefined == null)
			throw new NullPointerException("操作失败, 不存在code为["+code+"], version为["+version+"]的流程定义");
		if(processDefined.getState() == StateConstants.UNPUBLISHED)
			throw new IllegalArgumentException("操作失败, code为["+code+"], version为["+version+"]的流程定义还未发布");
		return get(processDefined);
	}
	
	/**
	 * 获取流程
	 * @param processDefined
	 * @return
	 */
	private Process get(ProcessDefinition processDefined) {
		Process process = processHandler.get(processDefined.getId());
		if(process == null) {
			processDefined.setContent(SessionContext.getSqlSession().uniqueQuery_("select content_ from bpm_re_procdef where id=?", Arrays.asList(processDefined.getId()))[0].toString());
			process = processHandler.put(processDefined);
		}
		return process;
	}
}
