package com.douglei.bpm.module.runtime.instance;

import java.util.Arrays;

import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.common.InstanceHandlePolicy;
import com.douglei.bpm.module.common.service.ExecutionResult;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.bpm.module.repository.definition.ProcessDefinitionStateConstants;
import com.douglei.bpm.process.ProcessHandler;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 运行实例服务
 * @author DougLei
 */
@Bean
public class RuntimeInstanceService {
	
	@Attribute
	private ProcessHandler processHandler;
	
	/**
	 * 判断指定id的流程定义, 是否存在运行实例
	 * @param processDefinitionId
	 * @return
	 */
	@Transaction
	public boolean exists(int processDefinitionId) {
		// TODO 判断指定的流程定义id, 是否存在运行实例
		
		return true;
	}

	/**
	 * 处理指定id的流程定义, 相关的所有运行实例
	 * @param processDefinitionId
	 * @param policy 对实例的处理策略
	 */
	@Transaction
	public void process(int processDefinitionId, InstanceHandlePolicy policy) {
		// TODO 处理指定id的流程定义, 相关的所有运行实例
		
	}
	
	/**
	 * 启动指定id的流程
	 * @param id
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult start(int processDefinitionId) {
		ProcessDefinition processDefined = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select code, version, subversion from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefined == null)
			throw new NullPointerException("操作失败, 不存在id为["+processDefinitionId+"]的流程定义");
		if(processDefined.getState() == ProcessDefinitionStateConstants.UNPUBLISHED)
			throw new IllegalArgumentException("操作失败, id为["+processDefinitionId+"]的流程定义还未发布");
		
		// TODO
		return null;
	}
	
	/**
	 * 启动指定code, version的流程实例
	 * @param code
	 * @param version
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult start(String code, String version) {
		ProcessDefinition processDefined = SessionContext.getTableSession().queryFirst(ProcessDefinition.class, "select code, version, subversion from bpm_re_procdef where code=? and version=? order by subversion desc", Arrays.asList(code, version));
		if(processDefined == null)
			throw new NullPointerException("操作失败, 不存在code为["+code+"], version为["+version+"]的流程定义");
		if(processDefined.getState() == ProcessDefinitionStateConstants.UNPUBLISHED)
			throw new IllegalArgumentException("操作失败, code为["+code+"], version为["+version+"]的流程定义还未发布");
		
		// TODO
		return null;
	}
	
	/**
	 * 激活指定id的流程实例
	 * @param instanceId
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult activate(int instanceId) {
		// TODO 
		
		return null;
	}
	
	/**
	 * 挂起指定id的流程实例
	 * @param instanceId
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult suspend(int instanceId) {
		// TODO 
		
		return null;
	}
	
	/**
	 * 终止指定id的流程实例
	 * @param instanceId
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult terminate(int instanceId) {
		// TODO 
		
		return null;
	}
	
	/**
	 * 删除指定id的流程实例
	 * @param instanceId
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult delete(int instanceId) {
		// TODO 
		
		return null;
	}
}
