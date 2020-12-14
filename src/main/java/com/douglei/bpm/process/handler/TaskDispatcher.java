package com.douglei.bpm.process.handler;

import java.util.Map;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.metadata.node.TaskMetadata;
import com.douglei.bpm.process.metadata.node.flow.FlowMetadata;
import com.douglei.tools.instances.ognl.OgnlHandler;
import com.douglei.tools.utils.StringUtil;

/**
 * 任务调度器
 * @author DougLei
 */
@Bean
public class TaskDispatcher {
	
	@Autowired
	private ProcessHandlers processHandlers;
	
	/**
	 * 对task中的flows进行调度, 选择第一条匹配的flow执行
	 * @param flows
	 * @param variableMap
	 */
	public void dispatch(TaskMetadata task, Map<String, Object> variableMap) {
		for(FlowMetadata flow : task.getFlows()) {
			if(matching(flow, variableMap)) {
				processHandlers.startup(flow.getTargetTask(), null);
				return;
			}
		}
		throw new TaskDispatchException("执行["+task.getName()+"]任务后, 未能匹配到合适的Flow, 使流程无法正常流转");
	}
	
	/**
	 * 判断指定的flow是否匹配
	 * @param flow
	 * @param variableMap
	 * @return 
	 */
	public boolean matching(FlowMetadata flow, Map<String, Object> variableMap) {
		String conditionExpr = flow.getConditionExpr();
		if(StringUtil.isEmpty(conditionExpr))
			return true;
		if(variableMap == null)
			throw new TaskDispatchException("在匹配Flow时, 未找到流程变量");
		return OgnlHandler.getSingleton().getBooleanValue(conditionExpr, variableMap);
	}
}
