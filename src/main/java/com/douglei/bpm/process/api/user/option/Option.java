package com.douglei.bpm.process.api.user.option;

import com.douglei.bpm.bean.annotation.MultiInstance;

/**
 * 
 * @author DougLei
 */
@MultiInstance
public interface Option {
	
	/**
	 * 获取option的类型, 必须唯一; 默认值为类名全路径
	 * @return
	 */
	default String getType() {
		return getClass().getName();
	}
}
