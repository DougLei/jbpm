package com.douglei.bpm.bean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.douglei.bpm.module.repository.RepositoryModule;

/**
 * 需要被BeanFactory扫描的类
 * @author DougLei
 */
@Target(ElementType.TYPE) // 表示注解的作用对象，ElementType.TYPE表示类，ElementType.METHOD表示方法...
@Retention(RetentionPolicy.RUNTIME) // 注解的保留机制，表示是运行时注解
public @interface Bean {
	
	/**
	 * <pre>
	 * 对应实际的class
	 * 例如 {@link ApplicationProcessContainer}的class为 {@link ProcesContainer}, 因为在其它类中, 如果需要依赖{@link ApplicationProcessContainer}, 肯定使用的是{@link ProcesContainer}类型作为属性; 
	 * 而一般类, 例如 {@link RepositoryModule}, class与自身的类相同, 因为在其它类中, 如果需要依赖{@link RepositoryModule}, 肯定使用的是{@link RepositoryModule}类型作为属性;
	 * </pre>
	 * @return
	 */
	Class<?> clazz() default Object.class;
	
	/**
	 * 是否是事物Bean
	 * @return
	 */
	boolean isTransaction() default false;
}