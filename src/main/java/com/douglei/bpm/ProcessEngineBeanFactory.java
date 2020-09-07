package com.douglei.bpm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.douglei.aop.ProxyBeanContext;
import com.douglei.aop.ProxyMethod;
import com.douglei.bpm.annotation.ProcessEngineBean;
import com.douglei.bpm.annotation.ProcessEngineField;
import com.douglei.bpm.annotation.ProcessEngineTransactionBean;
import com.douglei.bpm.module.history.HistoryModule;
import com.douglei.bpm.module.repository.RepositoryModule;
import com.douglei.bpm.module.runtime.RuntimeModule;
import com.douglei.orm.context.TransactionProxyInterceptor;
import com.douglei.orm.context.transaction.component.Transaction;
import com.douglei.orm.context.transaction.component.TransactionComponent;
import com.douglei.orm.context.transaction.component.TransactionComponentEntity;
import com.douglei.tools.instances.scanner.ClassScanner;
import com.douglei.tools.utils.reflect.ClassLoadUtil;
import com.douglei.tools.utils.reflect.ConstructorUtil;

/**
 * 引擎中bean工厂, 类似于spring的bean工厂, 扫描类并实例化后, 将实例set到各个属性中
 * @author DougLei
 */
class ProcessEngineBeanFactory {
	private Map<Class<?>, Object> BEAN_CONTAINER = new HashMap<Class<?>, Object>(64);
	
	ProcessEngineBeanFactory() {
		initBeanContainer();
		setBeanField();
	}
	
	/**
	 * 初始化Bean容器
	 * 
	 * 扫描com.douglei.bpm包下的所有class
	 * 找到有 {@link TransactionComponent} 的类, 对其创建代理实例, 存到 BEAN_CONTAINER 容器中
	 * 找到有 {@link ProcessEngineBean} 的类, 将其实例化, 存到 BEAN_CONTAINER 容器中
	 */
	private void initBeanContainer() {
		List<String> classes = new ClassScanner().scan("com.douglei.bpm"); // 扫描指定路径下的所有class
		Class<?> loadClass = null;
		Method[] declareMethods = null;
		for (String clz : classes) {
			loadClass = ClassLoadUtil.loadClass(clz);
			
			if(loadClass.getAnnotation(ProcessEngineTransactionBean.class) != null) {
				declareMethods = loadClass.getDeclaredMethods();
				
				TransactionComponentEntity transactionComponentEntity = null;
				for (Method dm : declareMethods) {
					if(dm.getAnnotation(Transaction.class) != null) {
						if(transactionComponentEntity == null) {
							transactionComponentEntity = new TransactionComponentEntity(loadClass, declareMethods.length);
						}
						transactionComponentEntity.addMethod(new ProxyMethod(dm));
					}
				}
				BEAN_CONTAINER.put(loadClass, ProxyBeanContext.createProxy(loadClass, new TransactionProxyInterceptor(transactionComponentEntity.getTransactionComponentClass(), transactionComponentEntity.getTransactionMethods())).getProxy());
			}else if(loadClass.getAnnotation(ProcessEngineBean.class) != null) {
				BEAN_CONTAINER.put(loadClass, ConstructorUtil.newInstance(loadClass));
			}
		}
	}
	
	/**
	 * 设置Bean容器中每个Bean的属性实例
	 */
	private void setBeanField() {
		try {
			Class<?> currentClass;
			Field[] fields;
			for(Entry<Class<?>, Object> bean : BEAN_CONTAINER.entrySet()) {
				currentClass = bean.getKey();
				do{
					fields = currentClass.getDeclaredFields();
					if(fields.length > 0) {
						for (Field field : fields) {
							if(field.getAnnotation(ProcessEngineField.class) != null) {
								field.setAccessible(true);
								field.set(bean.getValue(), BEAN_CONTAINER.get(field.getType()));
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
	ProcessEngine initProcessEngineFields(ProcessEngine engine) {
		engine.setRepository((RepositoryModule)BEAN_CONTAINER.get(RepositoryModule.class));
		engine.setRuntime((RuntimeModule)BEAN_CONTAINER.get(RuntimeModule.class));
		engine.setHistory((HistoryModule)BEAN_CONTAINER.get(HistoryModule.class));
		
		return engine;
	}
}
