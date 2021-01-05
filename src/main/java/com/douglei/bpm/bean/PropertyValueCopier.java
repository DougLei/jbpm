package com.douglei.bpm.bean;

import java.lang.reflect.Field;

import com.douglei.bpm.bean.annotation.Property;

/**
 * 属性值复制器
 * @author DougLei
 */
public class PropertyValueCopier {
	
	/**
	 * 
	 * @param source
	 * @param target
	 */
	public static void copy(Object source, Object target) {
		try {
			for(Field field : source.getClass().getDeclaredFields()) {
				if(field.isAnnotationPresent(Property.class)) 
					field.set(target, field.get(source));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
