package com.douglei.bpm.module.execution.variable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 变量的数据类型
 * @author DougLei
 */
public enum DataType {
	STRING(1, String.class, StringBuilder.class, char.class, StringBuffer.class, Character.class),
	NUMBER(2, byte.class, short.class, int.class, long.class, double.class, float.class, Byte.class, Short.class, Integer.class, Long.class, Double.class, Float.class, BigDecimal.class),
	DATETIME(3, Date.class, java.sql.Date.class, Timestamp.class),
	OBJECT(4);
	
	// ---------------------------------------------------------------
	private int value;
	private Class<?>[] supportClasses;
	private DataType(int value, Class<?>... supportClasses) {
		this.value = value;
		this.supportClasses = supportClasses;
	}
	
	/**
	 * 获取标识值
	 * @return
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * 根据标识值获取DataType实例
	 * @param value
	 * @return
	 */
	public static DataType valueOf(int value) {
		for (DataType dt : DataType.values()) {
			if(dt.value == value)
				return dt;
		}
		throw new IllegalArgumentException("不存在value为["+value+"]的DataType Enum");
	}
	
	/**
	 * 根据obj获取对应的数据类型; 没有匹配到合适的, 就默认为 OBJECT
	 * @param obj
	 * @return
	 */
	public static DataType getByObject(Object obj) {
		if(obj != null) {
			Class<?> clazz = obj.getClass();
			for(DataType dataType : DataType.values()) {
				for(Class<?> cls : dataType.supportClasses) {
					if(cls == clazz)
						return dataType;
				}
			}
			
			if(!(obj instanceof Serializable))
				throw new IllegalArgumentException("["+clazz.getName()+"]的数据类型为OBJECT, 但未实现["+Serializable.class.getName()+"]接口");
		}
		return OBJECT;
	}
}
