package com.douglei.bpm.process.api.user.option;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.CustomAutowired;
import com.douglei.bpm.bean.annotation.Bean;

/**
 * 用户任务中的option容器
 * @author DougLei
 */
@Bean
public class OptionHandlerContainer implements CustomAutowired{
	private Map<String, OptionHandler> map = new HashMap<String, OptionHandler>(); 
	
	@SuppressWarnings("unchecked")
	@Override
	public void setFields(Map<Class<?>, Object> beanContainer) {
		((List<OptionHandler>)beanContainer.get(OptionHandler.class)).forEach(optionHandler -> {
			map.put(optionHandler.getType(), optionHandler);
		});
	}
	
	/**
	 * 获取指定type的OptionHandler
	 * @param type
	 * @return
	 */
	public OptionHandler get(String type) {
		return map.get(type);
	}
}
