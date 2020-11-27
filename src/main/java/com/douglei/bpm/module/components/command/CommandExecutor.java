package com.douglei.bpm.module.components.command;

import com.douglei.bpm.bean.BeanFactory;
import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;

/**
 * 
 * @author DougLei
 */
@Bean
public class CommandExecutor {
	
	@Autowired
	private BeanFactory beanFactory;
	
	/**
	 * 执行
	 * @param command
	 * @return
	 */
	public <T> T execute(Command<T> command) {
		if(command.autowired())
			beanFactory.executeAutowired(command);
		return command.execute();
	}
}
