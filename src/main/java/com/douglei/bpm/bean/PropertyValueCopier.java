package com.douglei.bpm.bean;

import java.lang.reflect.Field;

import com.douglei.bpm.bean.annotation.Property;

/**
 * 属性值复制器
 * @author DougLei
 */
public class PropertyValueCopier {
	
	public static void copy(Object source, Object target) {
		try {
			for(Field field : source.getClass().getDeclaredFields()) {
				if(field.isAnnotationPresent(Property.class)) {
					field.setAccessible(true);
					field.set(target, field.get(source));
					field.setAccessible(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
