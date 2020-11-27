package com.douglei.bpm.bean.entity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.douglei.aop.ProxyBeanContext;
import com.douglei.aop.ProxyMethod;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.orm.context.TransactionProxyInterceptor;
import com.douglei.orm.context.transaction.component.Transaction;
import com.douglei.tools.utils.reflect.ConstructorUtil;

/**
 * 
 * @author DougLei
 */
public class GeneralBeanEntity extends BeanEntity{

	public GeneralBeanEntity(Bean bean, Class<?> instanceClazz) {
		super(bean.clazz(), instanceClazz);
		
		if(bean.isTransaction()) {
			List<ProxyMethod> methods = null;
			for (Method method : instanceClazz.getDeclaredMethods()) {
				if(method.getAnnotation(Transaction.class) != null) {
					if(methods == null)
						methods = new ArrayList<ProxyMethod>();
					methods.add(new ProxyMethod(method));
				} 
			}
			
			if(methods == null)
				throw new NullPointerException("["+instanceClazz.getName()+"]类被注解为事物Bean, 但是类中却没有任何方法有["+Transaction.class.getName()+"]注解");
			super.instance = ProxyBeanContext.createProxy(instanceClazz, new TransactionProxyInterceptor(instanceClazz, methods));
		}else {
			super.instance = ConstructorUtil.newInstance(instanceClazz);
		}
	}
}
