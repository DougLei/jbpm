package com.douglei.bpm.module.runtime.variable;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author DougLei
 */
public abstract class ProcessVariableMapHolder {
	protected Map<String, Object> variableMap;
	protected Map<String, ProcessVariable> globalVariableMap;
	protected Map<String, ProcessVariable> localVariableMap;
	protected List<String> transientVariableNames;
	
	/**
	 * 获取流程变量map
	 * @return
	 */
	public Map<String, Object> getVariableMap(){
		return variableMap;
	}
	/**
	 * 获取GLOBAL范围的变量map
	 * @return
	 */
	public Map<String, ProcessVariable> getGlobalVariableMap() {
		return globalVariableMap;
	}
	/**
	 * 获取LOCAL范围的变量map
	 * @return
	 */
	public Map<String, ProcessVariable> getLocalVariableMap() {
		return localVariableMap;
	}
	
	/**
	 * 是否存在GLOBAL范围的变量
	 * @return
	 */
	public boolean existsGlobalVariableMap() {
		return globalVariableMap != null;
	}
	/**
	 * 是否存在LOCAL范围的变量
	 * @return
	 */
	public boolean existsLocalVariableMap() {
		return localVariableMap != null;
	}
	/**
	 * 是否存在TRANSIENT范围的变量
	 * @return
	 */
	public boolean existsTransientVariable() {
		return transientVariableNames != null;
	}
}
