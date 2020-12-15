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
	
	private String name;
	private Class<?>[] supportClasses;
	private DataType(Class<?>... supportClasses) {
		this.name = name().toLowerCase();
		this.supportClasses = supportClasses;
	}
	public String getName() {
		return name;
	}
	
	/**
	 * 根据字符串值获取对应的类型枚举实例
	 * @param str
	 * @return
	 */
	public static DataType getByString(String str) {
		if(STRING.name.equals(str))
			return STRING;
		if(NUMBER.name.equals(str))
			return NUMBER;
		if(DATETIME.name.equals(str))
			return DATETIME;
		if(OBJECT.name.equals(str))
			return OBJECT;
		throw new NullPointerException("不存在值为["+str+"]的变量数据类型");
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
