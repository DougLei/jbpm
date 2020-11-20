package com.douglei.bpm.module.components.command;

import java.util.Comparator;
import java.util.List;

import com.douglei.bpm.bean.Autowire;
import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.bean.BeanFactory;
import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.interceptor.Interceptor;

/**
 * 
 * @author DougLei
 */
@Bean(isTransaction = false)
public class CommandExecutor {

	private Interceptor first;
	
	@Autowire
	private BeanFactory beanFactory;
	
	public CommandExecutor() {
		List<Interceptor> list = beanFactory.getInstances(Interceptor.class);
		list.sort(new Comparator<Interceptor>() {
			@Override
			public int compare(Interceptor o1, Interceptor o2) {
				if(o1.getOrder() < o2.getOrder())
					return -1;
				if(o1.getOrder() > o2.getOrder())
					return 1;
				return 0;
			}
		});
		
		for(int i=0; i<list.size()-1;i++)
			list.get(i).setNext(list.get(i+1));
		this.first = list.get(0);
	}

	public <T> ExecutionResult<T> execute(Command<T> command){
		return first.execute(command);
	}
}
