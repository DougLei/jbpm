package com.douglei.bpm.module;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;

/**
 * 
 * @author DougLei
 */
@Bean
public class CommandExecutor {
	
	@Autowired
	private ProcessEngineBeans processEngineBeans;
	
	/**
	 * 执行命令
	 * @param command
	 * @return
	 */
	public ExecutionResult execute(Command command) {
		return command.execute(processEngineBeans);
	}
}
