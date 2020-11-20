package com.douglei.bpm.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.aop.ProxyBean;
import com.douglei.aop.ProxyBeanContext;
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
		Class<?> clazz = null;
		Bean bean = null;
		for (String classpath : new ClassScanner().scan(true, "com.douglei.bpm")) { // 扫描指定路径下的所有class
			clazz = ClassLoadUtil.loadClass(classpath);
			bean = clazz.getAnnotation(Bean.class);
			if(bean == null)
				continue;
			
			if(bean.isLazy()) 
				lazyBeanContainer.add(new LazyBean(bean, clazz));
			else
				putInstance2BeanContainer(bean, clazz);
		}
	}
	
	// 将bean实例put到Bean容器中
	private void putInstance2BeanContainer(Bean bean, Class<?> clazz) {
		Class<?> key = (bean.clazz()==Object.class)?clazz:bean.clazz();
		
		if(bean.isTransaction()) {
			TransactionComponentEntity entity = null;
			for (Method method : clazz.getDeclaredMethods()) {
				if(method.getAnnotation(Transaction.class) != null) {
					if(entity == null)
						entity = new TransactionComponentEntity(clazz);
					entity.addMethod(method);
				}
			}
			if(entity != null) {
				beanContainer.put(key, ProxyBeanContext.createProxy(clazz, new TransactionProxyInterceptor(entity.getClazz(), entity.getMethods())));
				return;
			}
		}
		beanContainer.put(key, ConstructorUtil.newInstance(clazz));
	}
	
	// 给对象的属性赋值
	private void setAttribute(Object object) throws IllegalArgumentException, IllegalAccessException {
		Class<?> currentClass = (object instanceof ProxyBean)?((ProxyBean)object).getOriginObject().getClass():object.getClass();
		do{
			for (Field field : currentClass.getDeclaredFields()) {
				if(field.getAnnotation(Autowire.class) != null)
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

	/**
	 * 将自定义的实现Bean注册到BeanFactory的Bean容器中, 用来覆盖引擎默认的实现Bean
	 * @param beanClass bean对应的class
	 * @param beanObject bean的实例
	 */
	public void registerCustomImplBean(Class<?> beanClass, Object beanObject) {
		beanContainer.put(beanClass, beanObject);
	}
	
	/**
	 * 执行自动装配
	 */
	public void executeAutowire() {
		try {
			beanContainer.put(BeanFactory.class, this);
			for(Object object : beanContainer.values())
				setAttribute(object);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			beanContainer.clear();
		}
	}
	
	/**
	 * 获取指定类型的实例集合
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getInstances(Class<T> clazz) {
		Object list = beanContainer.get(clazz);
		if(list == null)
			throw new NullPointerException("Bean容器中不存在class为["+clazz+"]的实例集合");
		return (List<T>) list;
	}
}
