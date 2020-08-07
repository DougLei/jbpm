package com.douglei.bpm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.douglei.aop.ProxyBeanContext;
import com.douglei.aop.ProxyMethod;
import com.douglei.orm.context.TransactionProxyInterceptor;
import com.douglei.orm.context.transaction.component.Transaction;
import com.douglei.orm.context.transaction.component.TransactionComponent;
import com.douglei.orm.context.transaction.component.TransactionComponentEntity;
import com.douglei.tools.instances.scanner.ClassScanner;
import com.douglei.tools.utils.reflect.ClassLoadUtil;
import com.douglei.tools.utils.reflect.ConstructorUtil;

/**
 * 
 * @author DougLei
 */
class ProcessEngineBeanFactory {
	private final Map<Class<?>, Object> BEAN_CONTAINER = new HashMap<Class<?>, Object>(64);
	
	/**
	 * 创建所有流程引擎用到的bean集合
	 * @return
	 */
	public ProcessEngineBeanFactory createBeans() {
		initBeanContainer();
		setBeanFields();
		return this;
	}
	
	/**
	 * 初始化Bean容器
	 * 
	 * 扫描com.douglei.bpm包下的所有class
	 * 找到有 {@link TransactionComponent} 的类, 对其创建代理实例, 存到 BEAN_CONTAINER 容器中
	 * 找到有 {@link ProcessEngineBean} 的类, 将其实例化, 存到 BEAN_CONTAINER 容器中
	 */
	private void initBeanContainer() {
		List<String> classes = new ClassScanner().scan("com.douglei.bpm");
		Class<?> loadClass = null;
		Method[] declareMethods = null;
		for (String clz : classes) {
			loadClass = ClassLoadUtil.loadClass(clz);
			
			if(loadClass.getAnnotation(TransactionComponent.class) != null) {
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
	 * 设置Bean容器中每个Bean的属性实例, 完成依赖注入
	 */
	private void setBeanFields() {
		try {
			Class<?> currentClass;
			Field[] fields;
			for(Entry<Class<?>, Object> bean : BEAN_CONTAINER.entrySet()) {
				currentClass = bean.getKey();
				while(currentClass != Object.class) {
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
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 获取流程引擎实例
	 * @return
	 */
	public ProcessEngine getProcessEngine() {
		return (ProcessEngine) BEAN_CONTAINER.get(ProcessEngine.class);
	}
}
