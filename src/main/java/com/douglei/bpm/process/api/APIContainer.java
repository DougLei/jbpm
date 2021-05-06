package com.douglei.bpm.process.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.bpm.bean.CustomAutowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.listener.ListenParser;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpression;
import com.douglei.bpm.process.api.user.option.AbstractOptionParser;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimPolicy;
import com.douglei.bpm.process.api.user.task.handle.policy.DispatchPolicy;
import com.douglei.bpm.process.api.user.task.handle.policy.SerialHandlePolicy;
import com.douglei.tools.ExceptionUtil;

/**
 * 
 * @author DougLei
 */
@Bean
public class APIContainer implements CustomAutowired {
	private static final Logger logger= LoggerFactory.getLogger(APIContainer.class);
	private Map<String, AssignableUserExpression> assignableUserExpressionMap = new HashMap<String, AssignableUserExpression>();
	private Map<String, ClaimPolicy> claimPolicyMap = new HashMap<String, ClaimPolicy>();
	private Map<String, SerialHandlePolicy> serialHandlePolicyMap = new HashMap<String, SerialHandlePolicy>();
	private Map<String, DispatchPolicy> dispatchPolicyMap = new HashMap<String, DispatchPolicy>();
	private Map<String, AbstractOptionParser> optionParserMap = new HashMap<String, AbstractOptionParser>(); 
	private Map<String, ListenParser> listenParserMap = new HashMap<String, ListenParser>();
	
	@SuppressWarnings("unchecked")
	@Override
	public void setFields(Map<Class<?>, Object> beanContainer) {
		((List<AssignableUserExpression>)beanContainer.get(AssignableUserExpression.class)).forEach(expression -> {
			assignableUserExpressionMap.put(expression.getName(), expression);
		});
		((List<ClaimPolicy>)beanContainer.get(ClaimPolicy.class)).forEach(policy -> {
			claimPolicyMap.put(policy.getName(), policy);
		});
		((List<SerialHandlePolicy>)beanContainer.get(SerialHandlePolicy.class)).forEach(policy -> {
			serialHandlePolicyMap.put(policy.getName(), policy);
		});
		((List<DispatchPolicy>)beanContainer.get(DispatchPolicy.class)).forEach(policy -> {
			dispatchPolicyMap.put(policy.getName(), policy);
		});
		((List<AbstractOptionParser>)beanContainer.get(AbstractOptionParser.class)).forEach(optionHandler -> {
			optionParserMap.put(optionHandler.getType(), optionHandler);
		});
	}
	
	/**
	 * 
	 * @param name
	 * @param map
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object get(String name, Map map) {
		Object instance = map.get(name);
		if(instance == null) {
			try {
				instance = Class.forName(name).newInstance();
				map.put(name, instance);
			} catch (Exception e) {
				logger.error("获取name={}的实例时出现异常: {}", name, ExceptionUtil.getStackTrace(e));
			}
		}
		return instance;
	}
	
	/**
	 * 获取指定name的指派人表达式
	 * @param name
	 * @return
	 */
	public AssignableUserExpression getAssignableUserExpression(String name) {
		return (AssignableUserExpression) get(name, assignableUserExpressionMap);
	}
	
	/**
	 * 获取指定name的任务认领策略
	 * @param name
	 * @return
	 */
	public ClaimPolicy getClaimPolicy(String name) {
		return (ClaimPolicy) get(name, claimPolicyMap);
	}
	
	/**
	 * 获取指定name的串行办理策略
	 * @param name
	 * @return
	 */
	public SerialHandlePolicy getSerialHandlePolicy(String name) {
		return (SerialHandlePolicy) get(name, serialHandlePolicyMap);
	}
	
	/**
	 * 获取指定name的调度策略
	 * @param name
	 * @return
	 */
	public DispatchPolicy getDispatchPolicy(String name) {
		return (DispatchPolicy) get(name, dispatchPolicyMap);
	}
	
	/**
	 * 获取指定type的Option解析器
	 * @param type
	 * @return
	 */
	public AbstractOptionParser getOptionParser(String type) {
		return (AbstractOptionParser) get(type, optionParserMap);
	}
	
	/**
	 * 获取指定class的监听解析器
	 * @param clazz
	 * @return
	 */
	public ListenParser getListenParser(String clazz) {
		return (ListenParser) get(clazz, listenParserMap);
	}
}
