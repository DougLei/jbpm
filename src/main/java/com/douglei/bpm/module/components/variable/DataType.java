package com.douglei.bpm.module.components.variable;

import java.math.BigDecimal;
import java.util.Date;

import com.douglei.orm.dialect.datatype.db.wrapper.ClobWrapper;

/**
 * 变量的数据类型
 * @author DougLei
 */
public enum DataType {
	STRING(5, String.class, StringBuilder.class, char.class, Character.class),
	NUMBER(10, byte.class, short.class, int.class, long.class, double.class, float.class, Byte.class, Short.class, Integer.class, Long.class, Double.class, Float.class, BigDecimal.class),
	DATETIME(15, Date.class),
	CLOB(20, ClobWrapper.class),
	BLOB(25, byte[].class, Byte[].class);
	
	private int value;
	private Class<?>[] supportClasses;
	private DataType(int value, Class<?>... supportClasses) {
		this.value = value;
		this.supportClasses = supportClasses;
	}

	public int getValue() {
		return value;
	}

	/**
	 * 根据值获取对应的数据类型
	 * @param value
	 * @return
	 */
	static DataType getValueByObject(Object value) {
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
