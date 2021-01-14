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
public class OptionContainer implements CustomAutowired{
	private Map<String, Option> map = new HashMap<String, Option>(); 
	
	@SuppressWarnings("unchecked")
	@Override
	public void setFields(Map<Class<?>, Object> beanContainer) {
		((List<Option>)beanContainer.get(Option.class)).forEach(option -> {
			map.put(option.getType(), option);
		});
	}
	
	/**
	 * 获取指定type的option
	 * @param type
	 * @return
	 */
	public Option get(String type) {
		return map.get(type);
	}
}
