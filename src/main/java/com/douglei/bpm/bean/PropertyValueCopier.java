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
			Class<?> clz = source.getClass();
			while(clz != Object.class) {
				for(Field field : clz.getDeclaredFields()) {
					if(field.isAnnotationPresent(Property.class)) {
						field.setAccessible(true);
						field.set(target, field.get(source));
						field.setAccessible(false);
					}
				}
				
				clz = clz.getSuperclass();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
