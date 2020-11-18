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
	private List<LazyBean> lazyBeanContainer = new ArrayList<LazyBean>(10);
	
	public BeanFactory() {
		Class<?> loadClass = null;
		Bean beanAnnotation = null;
		for (String clz : new ClassScanner().scan(true, "com.douglei.bpm")) { // 扫描指定路径下的所有class
			loadClass = ClassLoadUtil.loadClass(clz);
			if((beanAnnotation = loadClass.getAnnotation(Bean.class)) == null)
				continue;
			
			if(beanAnnotation.isLazy()) 
				lazyBeanContainer.add(new LazyBean(beanAnnotation, loadClass));
			else
				putInstance2BeanContainer(beanAnnotation.isTransaction(), (beanAnnotation.clazz()==Object.class)?loadClass:beanAnnotation.clazz(), loadClass);
		}
	}
	
	// 将bean实例put到Bean容器中
	private void putInstance2BeanContainer(boolean isTransactionBean, Class<?> key, Class<?> loadClass) {
		if(isTransactionBean) {
			TransactionComponentEntity entity = null;
			for (Method method : loadClass.getDeclaredMethods()) {
				if(method.getAnnotation(Transaction.class) != null) {
					if(entity == null)
						entity = new TransactionComponentEntity(loadClass);
					entity.addMethod(method);
				}
			}
			if(entity != null) {
				beanContainer.put(key, ProxyBeanContext.createProxy(loadClass, new TransactionProxyInterceptor(entity.getClazz(), entity.getMethods())));
				return;
			}
		}
		beanContainer.put(key, ConstructorUtil.newInstance(loadClass));
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
			lazyBeanContainer.forEach(lazyBean -> {
				if(!beanContainer.containsKey(lazyBean.getKey()))
					putInstance2BeanContainer(lazyBean.isTransactionBean(), lazyBean.getKey(), lazyBean.getLoadClass());
			});
			
			for(Object object : beanContainer.values())
				setAttribute(object);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			beanContainer.clear();
			lazyBeanContainer.clear();
		}
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
}
