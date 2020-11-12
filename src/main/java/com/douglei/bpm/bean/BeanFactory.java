package com.douglei.bpm.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.douglei.aop.ProxyBean;
import com.douglei.aop.ProxyBeanContext;
import com.douglei.aop.ProxyMethod;
import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.orm.context.TransactionProxyInterceptor;
import com.douglei.orm.context.transaction.component.Transaction;
import com.douglei.orm.context.transaction.component.TransactionComponentEntity;
import com.douglei.tools.instances.resource.scanner.impl.ClassScanner;
import com.douglei.tools.utils.reflect.ClassLoadUtil;
import com.douglei.tools.utils.reflect.ConstructorUtil;

/**
 * 引擎中bean工厂, 类似于spring的bean工厂, 扫描类并实例化后, 将实例set到各个属性中
 * @author DougLei
 */
public class BeanFactory {
	private Map<Class<?>, Object> beanContainer = new HashMap<Class<?>, Object>(128);
	
	public BeanFactory() {
		initBeanContainer();
	}
	
	/**
	 * 初始化Bean容器
	 */
	private void initBeanContainer() {
		Class<?> loadClass = null;
		Bean beanAnno = null;
		Method[] declareMethods = null;
		for (String clz : new ClassScanner().scan("com.douglei.bpm")) { // 扫描指定路径下的所有class
			loadClass = ClassLoadUtil.loadClass(clz);
			beanAnno = loadClass.getAnnotation(Bean.class);
			
			if(beanAnno != null) {
				if(beanAnno.isTransaction()) {
					TransactionComponentEntity entity = null;
					
					declareMethods = loadClass.getDeclaredMethods();
					for (Method method : declareMethods) {
						if(method.getAnnotation(Transaction.class) != null) {
							if(entity == null)
								entity = new TransactionComponentEntity(loadClass, declareMethods.length);
							entity.addMethod(new ProxyMethod(method));
						}
					}
					if(entity != null) {
						beanContainer.put(beanAnno.clazz()==Object.class?loadClass:beanAnno.clazz(), ProxyBeanContext.createProxy(loadClass, new TransactionProxyInterceptor(entity.getClazz(), entity.getMethods())));
						continue;
					}
				}
				beanContainer.put(beanAnno.clazz()==Object.class?loadClass:beanAnno.clazz(), ConstructorUtil.newInstance(loadClass));
			}
		}
	}
	
	/**
	 * 将自定义的实现Bean注册到BeanFactory的Bean容器中, 用来覆盖引擎默认的实现Bean
	 * @param beanClass bean对应的class
	 * @param beanObject bean的实例
	 */
	public void registerCustomImplBean(Class<?> beanClass, Object beanObject) {
		beanContainer.put(beanClass, beanObject);
	}
	
	/**
	 * 设置Bean容器中每个Bean的属性
	 */
	public void setBeanAttributes() {
		try {
			for(Object object : beanContainer.values())
				setAttribute(object);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	// 给对象的属性赋值
	private void setAttribute(Object object) throws IllegalArgumentException, IllegalAccessException {
		Class<?> currentClass = (object instanceof ProxyBean)?((ProxyBean)object).getOriginObject().getClass():object.getClass();
		do{
			for (Field field : currentClass.getDeclaredFields()) {
				if(field.getAnnotation(Attribute.class) != null)
					setValue(object, field, beanContainer.get(field.getType()));
			}
			currentClass = currentClass.getSuperclass();
		} while (currentClass != Object.class);
	}
	
	// 给属性赋值
	private void setValue(Object object, Field field, Object value) throws IllegalArgumentException, IllegalAccessException {
		if(value == null)
			return;
		
		field.setAccessible(true);
		if(object instanceof ProxyBean) {
			if(value instanceof ProxyBean) {
				field.set(((ProxyBean)object).getOriginObject(), ((ProxyBean)value).getProxy());
			}else {
				field.set(((ProxyBean)object).getOriginObject(), value);
			}
		}else {
			if(value instanceof ProxyBean) {
				field.set(object, ((ProxyBean)value).getProxy());
			}else {
				field.set(object, value);
			}
		}
		field.setAccessible(false);
	}
}
