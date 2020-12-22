package com.douglei.bpm.module;

import com.douglei.bpm.bean.BeanFactoryProxy;
import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;

/**
 * 
 * @author DougLei
 */
@Bean
public class CommandExecutor {
	
	@Autowired
	private BeanFactoryProxy beanFactory;
	
	/**
	 * 执行命令
	 * @param command
	 * @return
	 */
	public ExecutionResult execute(Command command) {
		if(command.autowiredRequired())
			beanFactory.autowireBean(command);
		return command.execute();
	}
}
