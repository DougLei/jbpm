package com.douglei.bpm.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.aop.ProxyBean;
import com.douglei.aop.ProxyBeanContext;
import com.douglei.aop.ProxyMethod;
import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.container.ProcessContainer;
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
	private Map<Class<?>, Object> beans = new HashMap<Class<?>, Object>(64);
	
	public BeanFactory(ProcessContainer processContainerBean) {
		initBeanContainer(processContainerBean);
		setBeanAttributes();
	}
	
	/**
	 * 初始化Bean容器
	 * @param processContainerBean
	 */
	private void initBeanContainer(ProcessContainer processContainerBean) {
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
						beans.put(loadClass, ProxyBeanContext.createProxy(loadClass, new TransactionProxyInterceptor(transactionComponentEntity.getTransactionComponentClass(), transactionComponentEntity.getTransactionMethods())));
						continue;
					}
				}
				beans.put(loadClass, ConstructorUtil.newInstance(loadClass));
			}
		}
		
		// 将流程容器Bean也放到Bean容器中
		this.beans.put(ProcessContainer.class, processContainerBean);
	}
	
	// 给对象的属性赋值
	private void setAttribute(Object object) throws IllegalArgumentException, IllegalAccessException {
		Class<?> currentClass = (object instanceof ProxyBean)?((ProxyBean)object).getOriginObject().getClass():object.getClass();
		do{
			for (Field field : currentClass.getDeclaredFields()) {
				if(field.getAnnotation(Attribute.class) != null)
					setValue(object, field, beans.get(field.getType()));
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
	
	/**
	 * 设置Bean容器中每个Bean的属性
	 */
	private void setBeanAttributes() {
		try {
			for(Object object : beans.values())
				setAttribute(object);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 设置引擎中的属性
	 * @param engine
	 */
	public void setEngineAttributes(ProcessEngine engine) {
		try {
			setAttribute(engine);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
