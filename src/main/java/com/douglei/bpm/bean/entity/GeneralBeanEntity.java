package com.douglei.bpm.bean.entity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.douglei.aop.ProxyBeanContainer;
import com.douglei.aop.ProxyMethod;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.context.TransactionProxyInterceptor;
import com.douglei.tools.reflect.ClassUtil;

/**
 * 
 * @author DougLei
 */
public class GeneralBeanEntity extends BeanEntity{

	public GeneralBeanEntity(Bean bean, Class<?> instanceClazz) {
		super(bean.clazz(), instanceClazz);
		
		if(bean.isTransaction()) {
			Class<?> copy = instanceClazz;
			List<ProxyMethod> methods = null;
			do {
				for (Method method : copy.getDeclaredMethods()) {
					if(method.isAnnotationPresent(Transaction.class)) {
						if(methods == null)
							methods = new ArrayList<ProxyMethod>();
						methods.add(new ProxyMethod(method));
					} 
				}
				copy = copy.getSuperclass();
			}while(copy != Object.class);
			
			if(methods == null)
				throw new NullPointerException("["+instanceClazz.getName()+"]类被注解为事物Bean, 但是类中却没有任何方法有["+Transaction.class.getName()+"]注解");
			super.instance = ProxyBeanContainer.createProxy(instanceClazz, new TransactionProxyInterceptor(methods));
		}else {
			super.instance = ClassUtil.newInstance(instanceClazz);
		}
	}
}
