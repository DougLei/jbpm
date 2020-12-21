package com.douglei.bpm.module.runtime.variable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;

/**
 * 变量的数据类型
 * @author DougLei
 */
public enum DataType {
	STRING(String.class, StringBuilder.class, char.class, StringBuffer.class, Character.class),
	NUMBER(byte.class, short.class, int.class, long.class, double.class, float.class, Byte.class, Short.class, Integer.class, Long.class, Double.class, Float.class, BigDecimal.class),
	DATETIME(Date.class, java.sql.Date.class, Timestamp.class),
	OBJECT;
	
	private Class<?>[] supportClasses;
	private DataType(Class<?>... supportClasses) {
		this.supportClasses = supportClasses;
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
