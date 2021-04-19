package com.douglei.bpm.process.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.douglei.bpm.module.execution.variable.Scope;

/**
 * 
 * @author DougLei
 */
@Target(ElementType.FIELD) // 表示注解的作用对象，ElementType.TYPE表示类，ElementType.METHOD表示方法...
@Retention(RetentionPolicy.RUNTIME) // 注解的保留机制，表示是运行时注解
public @interface VariableField {
	
	/**
	 * 变量名, 默认即属性名
	 * @return
	 */
	String name() default "";
	
	/**
	 * 变量范围, 默认是global范围
	 * @return
	 */
	Scope scope() default Scope.GLOBAL;
}
