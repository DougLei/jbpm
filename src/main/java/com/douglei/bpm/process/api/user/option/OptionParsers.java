package com.douglei.bpm.process.api.user.option;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.CustomAutowired;
import com.douglei.bpm.bean.annotation.Bean;

/**
 * 用户任务中的option解析器容器
 * @author DougLei
 */
@Bean
public class OptionParsers implements CustomAutowired{
	private Map<String, AbstractOptionParser> map = new HashMap<String, AbstractOptionParser>(); 
	
	@SuppressWarnings("unchecked")
	@Override
	public void setFields(Map<Class<?>, Object> beanContainer) {
		((List<AbstractOptionParser>)beanContainer.get(AbstractOptionParser.class)).forEach(optionHandler -> {
			map.put(optionHandler.getType(), optionHandler);
		});
	}
	
	public AbstractOptionParser get(String type) {
		return map.get(type);
	}
}
