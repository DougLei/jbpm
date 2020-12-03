package com.douglei.bpm.module.runtime.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.module.runtime.task.entity.variable.Scope;

/**
 * 
 * @author DougLei
 */
class ProcessVariablesSetter extends ProcessVariables{
	private List<String> transientVariableNames;
	
	public void addVariable(String name, Scope scope, Object value) {
		switch(scope) {
			case GLOBAL:
				if(globalVariableMap == null)
					globalVariableMap = new HashMap<String, ProcessVariable>();
				globalVariableMap.put(name, new ProcessVariable(name, scope, value));
				
				if(!existsInLocalVariableMap(name) && !existsInTransientVariableNames(name))
					variableMap.put(name, value);
				break;
			case LOCAL:
				if(localVariableMap == null)
					localVariableMap = new HashMap<String, ProcessVariable>();
				localVariableMap.put(name, new ProcessVariable(name, scope, value));
				
				if(!existsInTransientVariableNames(name))
					variableMap.put(name, value);
				break;
			case TRANSIENT:
				if(transientVariableNames == null)
					transientVariableNames = new ArrayList<String>();
				if(transientVariableNames.isEmpty() || !transientVariableNames.contains(name))
					transientVariableNames.add(name);
				
				variableMap.put(name, value);
				break;
		}
	}
	// 判断指定的变量名, 是否存在于local变量集合中
	private boolean existsInLocalVariableMap(String name) {
		if(localVariableMap == null)
			return false;
		return localVariableMap.containsKey(name);
	}
	// 判断指定的变量名, 是否存在于transient变量名集合中
	private boolean existsInTransientVariableNames(String name) {
		if(transientVariableNames == null)
			return false;
		return transientVariableNames.contains(name);
	}
	
	public void addVariables(Scope scope, Map<String, Object> variables) {
		variables.forEach((name, value) -> {
			addVariable(name, scope, value);
		});
	}
}
