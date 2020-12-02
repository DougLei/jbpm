package com.douglei.bpm.component.variable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 变量的数据类型
 * @author DougLei
 */
public enum DataType {
	STRING(String.class, StringBuilder.class, char.class, Character.class),
	NUMBER(byte.class, short.class, int.class, long.class, double.class, float.class, Byte.class, Short.class, Integer.class, Long.class, Double.class, Float.class, BigDecimal.class),
	DATETIME(Date.class);
	
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
	 * 根据值获取对应的数据类型
	 * @param value
	 * @return
	 */
	static DataType getByObjectValue(Object value) {
		Class<?> clazz = value.getClass();
		for(DataType dataType : DataType.values()) {
			for(Class<?> cls : dataType.supportClasses) {
				if(cls == clazz)
					return dataType;
			}
		}
		throw new IllegalArgumentException("流程引擎目前不支持值为["+value+"]的类型["+value.getClass().getName()+"]");
	}
}
