package com.douglei.bpm.module.components.command;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.CustomAutowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.components.command.interceptor.Interceptor;

/**
 * 
 * @author DougLei
 */
@Bean
public class CommandExecutor implements CustomAutowired{

	private Interceptor first;
	
	@Override
	@SuppressWarnings("unchecked")
	public void setFields(Map<Class<?>, Object> beanContainer) {
		List<Interceptor> list = (List<Interceptor>) beanContainer.get(Interceptor.class);
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
	
	/**
	 * 执行命令
	 * @param command
	 * @return
	 */
	public <T> T execute(Command<T> command) {
		return first.execute(command);
	}
}
