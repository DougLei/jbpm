package com.douglei.bpm.module.runtime.instance;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author DougLei
 */
public class ProcessVariables {
	protected Map<String, Object> variableMap = new HashMap<String, Object>();
	protected Map<String, ProcessVariable> globalVariableMap;
	protected Map<String, ProcessVariable> localVariableMap;
	
	public Map<String, Object> getVariableMap(){
		return variableMap;
	}
	public Map<String, ProcessVariable> getGlobalVariableMap() {
		return globalVariableMap;
	}
	public Map<String, ProcessVariable> getLocalVariableMap() {
		return localVariableMap;
	}
}
