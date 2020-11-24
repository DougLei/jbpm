package com.douglei.bpm.module.repository.definition;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.CommandExecutor;
import com.douglei.bpm.module.components.instance.InstanceHandlePolicy;
import com.douglei.bpm.module.repository.definition.builder.ProcessDefinitionBuilder;
import com.douglei.bpm.module.repository.definition.command.DeleteProcessDefinitionCommand;
import com.douglei.bpm.module.repository.definition.command.DeployProcessCommand;
import com.douglei.bpm.module.repository.definition.command.InsertProcessDefinitionCommand;
import com.douglei.bpm.module.repository.definition.command.UnDeployProcessCommand;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;

/**
 * 流程定义服务
 * @author DougLei
 */
@Bean
public class ProcessDefinitionService {

	@Autowired
	private CommandExecutor commandExecutor;
	
	/**
	 * 保存流程定义
	 * @param builder
	 * @param strict 是否要强制保存; 针对有实例的流程定义, 如果为true, 则会创建新的子版本并保存, 否则返回错误信息
	 * @return
	 */
	public ExecutionResult<ProcessDefinition> insert(ProcessDefinitionBuilder builder, boolean strict) {
		return commandExecutor.execute(new InsertProcessDefinitionCommand(builder.build(), strict));
	}
	
	/**
	 * 流程部署
	 * @param processDefinitionId 
	 * @param runtimeInstancePolicy 对运行实例的处理策略, 如果传入null, 则不进行任何处理
	 * @return
	 */
	public ExecutionResult<Integer> deploy(int processDefinitionId, InstanceHandlePolicy runtimeInstancePolicy) {
		return commandExecutor.execute(new DeployProcessCommand(processDefinitionId, runtimeInstancePolicy));
	}
	
	/**
	 * 取消流程部署
	 * @param processDefinitionId
	 * @param runtimeInstancePolicy 对运行实例的处理策略, 如果传入null, 则不进行任何处理
	 * @param historyInstancePolicy 对历史实例的处理策略, 如果传入null, 则不进行任何处理
	 * @return
	 */
	public ExecutionResult<Integer> undeploy(int processDefinitionId, InstanceHandlePolicy runtimeInstancePolicy, InstanceHandlePolicy historyInstancePolicy) {
		return commandExecutor.execute(new UnDeployProcessCommand(processDefinitionId, runtimeInstancePolicy, historyInstancePolicy));
	}
	
	/**
	 * 删除流程定义
	 * @param processDefinitionId
	 * @param strict 是否强制删除; 针对有实例的流程定义, 如果为true, 则会修改流程定义的状态为删除(逻辑删除), 否则返回错误信息
	 * @return
	 */
	public ExecutionResult<Integer> delete(int processDefinitionId, boolean strict) {
		return commandExecutor.execute(new DeleteProcessDefinitionCommand(processDefinitionId, strict));
	}
}
