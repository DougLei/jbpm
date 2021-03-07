package com.douglei.bpm.module.runtime.instance;

import java.util.Arrays;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.CommandExecutor;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.instance.command.StartProcessCmd;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;

/**
 * 运行实例服务
 * @author DougLei
 */
@Bean(isTransaction=true)
public class ProcessInstanceService {
	
	@Autowired
	private CommandExecutor commandExecutor;
	
	/**
	 * 启动流程
	 * @param parameter
	 * @return 返回对象的success=true时, 其Object属性为 {@link ProcessInstance} 类型, 记录启动的流程实例
	 */
	@Transaction
	public ExecutionResult start(StartParameter parameter) {
		return commandExecutor.execute(new StartProcessCmd(parameter));
	}
	
	/**
	 * 激活指定id的流程实例
	 * @param instanceId
	 * @return
	 */
	public int activate(int instanceId) {
		// TODO 
		
		return 1;
	}
	
	/**
	 * 挂起指定id的流程实例
	 * @param instanceId
	 * @return
	 */
	public int suspend(int instanceId) {
		// TODO 
		
		return 1;
	}
	
	/**
	 * 终止指定id的流程实例
	 * @param instanceId
	 * @return
	 */
	public int terminate(int instanceId) {
		// TODO 
		
		return 1;
	}
	
	/**
	 * 删除指定id的流程实例
	 * @param instanceId
	 * @return
	 */
	public int delete(int instanceId) {
		// TODO 
		
		return 1;
	}
	
	/**
	 * 判断指定的流程定义, 是否存在运行实例
	 * @param processDefinitionId
	 * @return
	 */
	public boolean exists(int processDefinitionId) {
		return Integer.parseInt(
				SessionContext.getSqlSession().uniqueQuery_("select count(id) from bpm_ru_procinst where procdef_id=?", Arrays.asList(processDefinitionId))[0].toString()) > 0;
	}
	
	/**
	 * 处理指定的流程定义, 相关的所有运行实例
	 * @param processDefinitionId
	 * @param policy 对实例的处理策略
	 */
	public void handle(int processDefinitionId, ProcessInstanceHandlePolicy policy) {
		// TODO 处理指定id的流程定义, 相关的所有运行实例
	}
}
