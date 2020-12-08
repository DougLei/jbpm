package com.douglei.bpm.process.scheduler.flow;

import java.util.Map;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.metadata.node.flow.FlowMetadata;
import com.douglei.bpm.process.scheduler.DispatchException;
import com.douglei.bpm.process.scheduler.ProcessScheduler;
import com.douglei.tools.instances.ognl.OgnlHandler;
import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
@Bean
public class FlowDispatcher {
	private ExecutionResult<Object> success = new ExecutionResult<Object>(new Object());
	private ExecutionResult<Object> fail = new ExecutionResult<Object>(null);
	
	@Autowired
	private ProcessScheduler processScheduler;
	
	/**
	 * 
	 * @param flow
	 * @param parameter
	 * @return 是否可以进入该Flow
	 */
	public ExecutionResult<Object> dispatch(FlowMetadata flow, FlowDispatchParameter parameter) {
		if(!matching(flow.getConditionExpr(), parameter.getVariableMap()))
			return fail;
		
		processScheduler.dispatchTask(flow.getTargetTask(), parameter);
		return success;
	}
	
	// 根据流程变量, 判断当前Flow的conditionExpr是否匹配
	private boolean matching(String conditionExpr, Map<String, Object> variableMap) {
		if(StringUtil.isEmpty(conditionExpr))
			return true;
		if(variableMap == null)
			throw new DispatchException("在匹配Flow时, 不存在流程变量");
		return OgnlHandler.getSingleton().getBooleanValue(conditionExpr, variableMap);
	}
}
