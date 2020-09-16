package com.douglei.bpm.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.douglei.aop.ProxyBeanContext;
import com.douglei.aop.ProxyMethod;
import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.orm.context.TransactionProxyInterceptor;
import com.douglei.orm.context.transaction.component.Transaction;
import com.douglei.orm.context.transaction.component.TransactionComponentEntity;
import com.douglei.tools.instances.scanner.ClassScanner;
import com.douglei.tools.utils.reflect.ClassLoadUtil;
import com.douglei.tools.utils.reflect.ConstructorUtil;

/**
 * 引擎中bean工厂, 类似于spring的bean工厂, 扫描类并实例化后, 将实例set到各个属性中
 * @author DougLei
 */
public class BeanFactory {
	private Map<Class<?>, Object> BEAN_CONTAINER = new HashMap<Class<?>, Object>(64);
	
	private BeanFactory() {
		initBeanContainer();
		setBeanAttributes();
	}
	
	private static BeanFactory singleton;
	public static synchronized BeanFactory getSingleton() {
		if(singleton == null)
			singleton = new BeanFactory();
		return singleton;
	}
	
	
	/**
	 * 初始化Bean容器
	 */
	private void initBeanContainer() {
		List<String> classes = new ClassScanner().scan("com.douglei.bpm"); // 扫描指定路径下的所有class
		Class<?> loadClass = null;
		Bean beanAnno = null;
		Method[] declareMethods = null;
		for (String clz : classes) {
			loadClass = ClassLoadUtil.loadClass(clz);
			beanAnno = loadClass.getAnnotation(Bean.class);
			
			if(beanAnno != null) {
				if(beanAnno.transaction()) {
					TransactionComponentEntity transactionComponentEntity = null;
					
					declareMethods = loadClass.getDeclaredMethods();
					for (Method method : declareMethods) {
						if(method.getAnnotation(Transaction.class) != null) {
							if(transactionComponentEntity == null)
								transactionComponentEntity = new TransactionComponentEntity(loadClass, declareMethods.length);
							transactionComponentEntity.addMethod(new ProxyMethod(method));
						}
					}
					if(transactionComponentEntity != null) {
						BEAN_CONTAINER.put(loadClass, ProxyBeanContext.createProxy(loadClass, new TransactionProxyInterceptor(transactionComponentEntity.getTransactionComponentClass(), transactionComponentEntity.getTransactionMethods())).getProxy());
						continue;
					}
				}
				BEAN_CONTAINER.put(loadClass, ConstructorUtil.newInstance(loadClass));
			}
		}
	}
	
	/**
	 * 设置Bean容器中每个Bean的属性
	 */
	private void setBeanAttributes() {
		try {
			Class<?> currentClass;
			Field[] fields;
			Object instance = null;
			for(Entry<Class<?>, Object> bean : BEAN_CONTAINER.entrySet()) {
				currentClass = bean.getKey();
				do{
					fields = currentClass.getDeclaredFields();
					if(fields.length > 0) {
						for (Field field : fields) {
							if(field.getAnnotation(Attribute.class) != null && (instance = BEAN_CONTAINER.get(field.getType())) != null) {
								field.setAccessible(true);
								field.set(bean.getValue(), instance);
								field.setAccessible(false);
							}
						}
					}
					currentClass = currentClass.getSuperclass();
				} while (currentClass != Object.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 初始化引擎中的属性
	 * @param engine
	 * @return 
	 */
	public ProcessEngine initEngineAttributes(ProcessEngine engine) {
		try {
			Field[] fields = engine.getClass().getSuperclass().getDeclaredFields();
			for (Field field : fields) {
				if(field.getAnnotation(Attribute.class) != null) {
					field.setAccessible(true);
					field.set(engine, BEAN_CONTAINER.get(field.getType()));
					field.setAccessible(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return engine;
	}
}
