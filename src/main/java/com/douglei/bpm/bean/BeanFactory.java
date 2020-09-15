package com.douglei.bpm.bean;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.bean.annotation.ProcessEngineBean;
import com.douglei.bpm.bean.annotation.ProcessEngineField;
import com.douglei.tools.instances.scanner.ClassScanner;
import com.douglei.tools.utils.reflect.ClassLoadUtil;

/**
 * 引擎中bean工厂, 类似于spring的bean工厂, 扫描类并实例化后, 将实例set到各个属性中
 * @author DougLei
 */
public class BeanFactory {
	private Map<Class<?>, Bean> beanContainer = new HashMap<Class<?>, Bean>(64);
	
	private BeanFactory() {
		initBeanContainer();
		setBeanField();
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
		ProcessEngineBean bean;
		for (String clz : classes) {
			loadClass = ClassLoadUtil.loadClass(clz);
			if((bean = loadClass.getAnnotation(ProcessEngineBean.class)) != null) {
				
				if(bean.singleton()) {
					
					
					
				}
				
				
				
			}
			
			
			
			
			
//				declareMethods = loadClass.getDeclaredMethods();
//				
//				TransactionComponentEntity transactionComponentEntity = null;
//				for (Method dm : declareMethods) {
//					if(dm.getAnnotation(Transaction.class) != null) {
//						if(transactionComponentEntity == null) {
//							transactionComponentEntity = new TransactionComponentEntity(loadClass, declareMethods.length);
//						}
//						transactionComponentEntity.addMethod(new ProxyMethod(dm));
//					}
//				}
//				CONTAINER.put(loadClass, ProxyBeanContext.createProxy(loadClass, new TransactionProxyInterceptor(transactionComponentEntity.getTransactionComponentClass(), transactionComponentEntity.getTransactionMethods())).getProxy());
//			}else if(loadClass.getAnnotation(ProcessEngineBean.class) != null) {
//				CONTAINER.put(loadClass, ConstructorUtil.newInstance(loadClass));
//			}
		}
	}
	
	/**
	 * 设置Bean容器中每个Bean的属性实例
	 */
	private void setBeanField() {
		try {
			Class<?> currentClass;
			Field[] fields;
			for(Entry<Class<?>, Bean> bean : beanContainer.entrySet()) {
				currentClass = bean.getKey();
				do{
					fields = currentClass.getDeclaredFields();
					if(fields.length > 0) {
						for (Field field : fields) {
							if(field.getAnnotation(ProcessEngineField.class) != null) {
								field.setAccessible(true);
								field.set(bean.getValue(), beanContainer.get(field.getType()));
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
	 * 初始化引擎中的属性, 并返回
	 * @param engine
	 * @return 
	 */
	public ProcessEngine initEngineFields(ProcessEngine engine) {
//		engine.setRepository((RepositoryModule)BEAN_CONTAINER.get(RepositoryModule.class));
//		engine.setRuntime((RuntimeModule)BEAN_CONTAINER.get(RuntimeModule.class));
//		engine.setHistory((HistoryModule)BEAN_CONTAINER.get(HistoryModule.class));
		
		return engine;
	}
}
