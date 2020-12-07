package com.douglei.bpm.process.executor.flow;

import java.util.Map;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.executor.Executors;
import com.douglei.bpm.process.metadata.node.flow.FlowMetadata;
import com.douglei.tools.instances.ognl.OgnlHandler;
import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
@Bean
public class FlowExecutor {
	private ExecutionResult<Object> success = new ExecutionResult<Object>(new Object());
	private ExecutionResult<Object> fail = new ExecutionResult<Object>(null);
	
	@Autowired
	private Executors executors;
	
	/**
	 * 执行Flow
	 * @param flow
	 * @param parameter
	 * @return 是否可以进入该Flow
	 */
	public ExecutionResult<Object> execute(FlowMetadata flow, FlowExecutionParameter parameter) {
		if(!matching(flow.getConditionExpr(), parameter.getVariableMap()))
			return fail;
		
		executors.executeTask(flow.getTargetTask(), parameter);
		return success;
	}
	
	// 根据流程变量, 判断当前Flow的conditionExpr是否匹配
	private boolean matching(String conditionExpr, Map<String, Object> variableMap) {
		if(StringUtil.isEmpty(conditionExpr))
			return true;
		return OgnlHandler.getSingleton().getBooleanValue(conditionExpr, variableMap);
	}
}
