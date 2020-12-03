package com.douglei.bpm.module.runtime.task.entity.variable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 变量的数据类型
 * @author DougLei
 */
public enum DataType {
	STRING(String.class, StringBuilder.class, char.class, StringBuffer.class, Character.class),
	NUMBER(byte.class, short.class, int.class, long.class, double.class, float.class, Byte.class, Short.class, Integer.class, Long.class, Double.class, Float.class, BigDecimal.class),
	DATETIME(Date.class),
	OBJECT;
	
	private String value;
	private Class<?>[] supportClasses;
	private DataType(Class<?>... supportClasses) {
		this.value = name().toLowerCase();
		this.supportClasses = supportClasses;
	}
	public String getValue() {
		return value;
	}
	
	/**
	 * 根据值获取对应的数据类型; 没有匹配到合适的, 就默认为 OBJECT
	 * @param value
	 * @return
	 */
	public static DataType getByObjectValue(Object value) {
		if(value != null) {
			Class<?> clazz = value.getClass();
			for(DataType dataType : DataType.values()) {
				for(Class<?> cls : dataType.supportClasses) {
					if(cls == clazz)
						return dataType;
				}
			}
			
			if(!(value instanceof Serializable))
				throw new IllegalArgumentException("["+clazz.getName()+"]的数据类型为OBJECT, 但未实现["+Serializable.class.getName()+"]接口");
		}
		return OBJECT;
	}
}
