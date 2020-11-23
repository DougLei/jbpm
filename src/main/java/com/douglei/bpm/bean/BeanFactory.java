package com.douglei.bpm.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.entity.BeanEntity;
import com.douglei.bpm.bean.entity.CustomBeanEntity;
import com.douglei.bpm.bean.entity.GeneralBeanEntity;
import com.douglei.tools.instances.resource.scanner.impl.ClassScanner;
import com.douglei.tools.utils.reflect.ClassLoadUtil;

/**
 * 
 * @author DougLei
 */
public class BeanFactory {
	private Map<Class<?>, Object> beanContainer = new HashMap<Class<?>, Object>(128);
	
	public BeanFactory() {
		Class<?> clazz = null;
		Bean bean = null;
		for (String classpath : new ClassScanner().scan("com.douglei.bpm")) { // 扫描指定路径下的所有class
			clazz = ClassLoadUtil.loadClass(classpath);
			bean = clazz.getAnnotation(Bean.class);
			if(bean == null)
				continue;
			putInstance2BeanContainer(new GeneralBeanEntity(bean.clazz(), clazz));
		}
	}
	
	// 将bean实例put到Bean容器中
	@SuppressWarnings("unchecked")
	private void putInstance2BeanContainer(BeanEntity beanEntity) {
		if(beanEntity.supportMultiInstances()) {
			List<Object> list = (List<Object>) beanContainer.get(beanEntity.getKey());
			if(list == null) {
				list = new ArrayList<Object>();
				beanContainer.put(beanEntity.getKey(), list);
			}
			list.add(beanEntity.getInstance());
		}else {
			beanContainer.put(beanEntity.getKey(), beanEntity.getInstance());
		}
	}
	
	/**
	 * 执行自动装配
	 */
	public void executeAutowired() {
		try {
			for(Object object : beanContainer.values())
				setFields(object);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			beanContainer.clear();
		}
	}
	// 设置当前对象的属性
	private void setFields(Object object) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		if(object instanceof CustomAutowired) {
			((CustomAutowired)object).setFields(beanContainer);
		}else {
			Class<?> currentClass = object.getClass();
			do{
				for (Field field : currentClass.getDeclaredFields()) {
					if(field.getAnnotation(Autowired.class) != null)
						setField(object, field);
				}
				currentClass = currentClass.getSuperclass();
			} while (currentClass != Object.class);
		}
	}
	// 设置属性
	private void setField(Object object, Field field) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		Object value = beanContainer.get(field.getType());
		if(value == null) {
			DefaultInstance defaultInstance = field.getType().getAnnotation(DefaultInstance.class);
			if(defaultInstance == null)
				throw new NullPointerException("在Bean容器中, 未能找到[" + object.getClass().getName() + "]类中的[" + field.getName() + "]属性值");
			
			value = defaultInstance.clazz().newInstance();
			putInstance2BeanContainer(new CustomBeanEntity(field.getType(), value));
		}
		
		field.setAccessible(true);
		field.set(object, value);
		field.setAccessible(false);
	}
	
	/**
	 * 将自定义的实现Bean注册到BeanFactory的Bean容器中
	 * @param key bean在容器中的key
	 * @param instance bean的实例
	 */
	public void registerCustomBean(Class<?> key, Object instance) {
		putInstance2BeanContainer(new CustomBeanEntity(key, instance));
	}
}
