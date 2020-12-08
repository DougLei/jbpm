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
	 */
	public <T> T execute(Command<T> command) {
		if(command.autowireField())
			beanFactory.autowireField(command);
		return command.execute();
	}
}
