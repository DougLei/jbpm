package com.douglei.bpm.bean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author DougLei
 */
@Target(ElementType.TYPE) // 表示注解的作用对象，ElementType.TYPE表示类，ElementType.METHOD表示方法...
@Retention(RetentionPolicy.RUNTIME) // 注解的保留机制，表示是运行时注解
public @interface Bean {
	
	/**
	 * bean对应的class, 默认Object.class
	 * @return
	 */
	Class<?> clazz() default Object.class;
	
	/**
	 * 是否是事物Bean, 默认是true
	 * @return
	 */
	boolean isTransaction() default true;
}