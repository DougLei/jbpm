package com.douglei.bpm.module.repository.type;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.CommandExecutor;
import com.douglei.bpm.module.repository.type.command.DeleteProcessTypeCommand;
import com.douglei.bpm.module.repository.type.command.InsertProcessTypeCommand;
import com.douglei.bpm.module.repository.type.command.UpdateProcessTypeCommand;
import com.douglei.bpm.module.repository.type.entity.ProcessType;

/**
 * 流程类型服务
 * @author DougLei
 */
@Bean
public class ProcessTypeService {
	
	@Autowired
	private CommandExecutor commandExecutor;
	
	/**
	 * 保存类型
	 * @param type
	 * @return
	 */
	public ExecutionResult<ProcessType> save(ProcessType type) {
		return commandExecutor.execute(new InsertProcessTypeCommand(type));
	}
	
	/**
	 * 修改类型
	 * @param type
	 * @return
	 */
	public ExecutionResult<ProcessType> update(ProcessType type) {
		return commandExecutor.execute(new UpdateProcessTypeCommand(type));
	}
	
	/**
	 * 删除类型
	 * @param processTypeId
	 * @param strict 是否进行强制删除; 强制删除时, 如果被删除的类型下存在流程定义, 则将这些流程定义的类型值改为0(默认类型)
	 * @return
	 */
	public ExecutionResult<Integer> delete(int processTypeId, boolean strict) {
		return commandExecutor.execute(new DeleteProcessTypeCommand(processTypeId, strict));
	}
}
