package com.douglei.bpm.process.handler;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.module.runtime.variable.DataType;
import com.douglei.bpm.module.runtime.variable.Scope;
import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.tools.utils.StringUtil;

/**
 * 流程变量(集合包装类)
 * @author DougLei
 */
public class VariableEntities {
	private Map<String, Object> variableMap;
	private Map<String, VariableEntity> globalVariableMap;
	private Map<String, VariableEntity> localVariableMap;
	private Map<String, VariableEntity> transientVariableMap;
	
	public VariableEntities() {}
	public VariableEntities(List<Variable> variables) {
		if(variables.isEmpty()) 
			return;
		for (Variable variable : variables) 
			addVariable(variable.getTaskinstId(), variable.getName(), variable.getScopeInstance(), variable.getDataTypeInstance(), variable.getValue());
	}
	
	/**
	 * 获取流程变量
	 * @return
	 */
	public Map<String, Object> getVariableMap(){
		return variableMap;
	}
	/**
	 * 获取global范围的变量
	 * @return
	 */
	public Map<String, VariableEntity> getGlobalVariableMap() {
		return globalVariableMap;
	}
	/**
	 * 获取local范围的变量
	 * @return
	 */
	public Map<String, VariableEntity> getLocalVariableMap() {
		return localVariableMap;
	}
	/**
	 * 获取transient范围的变量
	 * @return
	 */
	public Map<String, VariableEntity> getTransientVariableMap() {
		return transientVariableMap;
	}
	
	/**
	 * 是否存在变量
	 * @return
	 */
	public boolean existsVariable() {
		return variableMap != null; 
	}
	/**
	 * 是否存在global范围的变量
	 * @return
	 */
	public boolean existsGlobalVariable() {
		return globalVariableMap != null;
	}
	/**
	 * 是否存在local范围的变量
	 * @return
	 */
	public boolean existsLocalVariable() {
		return localVariableMap != null;
	}
	/**
	 * 是否存在transient范围的变量
	 * @return
	 */
	public boolean existsTransientVariable() {
		return transientVariableMap != null;
	}
	
	/**
	 * 添加变量
	 * @param name
	 * @param scope
	 * @param value
	 */
	public void addVariable(String name, Scope scope, Object value) {
		addVariable(null, name, scope, DataType.getByObject(value), value);
	}
	
	/**
	 * 添加变量
	 * @param name
	 * @param scope
	 * @param dataType
	 * @param value
	 */
	public void addVariable(String name, Scope scope, DataType dataType, Object value) {
		addVariable(null, name, scope, dataType, value);
	}
	
	/**
	 * 添加变量
	 * @param scope
	 * @param variables
	 */
	public void addVariables(Scope scope, Map<String, Object> variables) {
		variables.forEach((name, value) -> {
			addVariable(name, scope, value);
		});
	}
	
	/**
	 * 添加变量, 将对象中的某些属性作为流程变量添加进来
	 * @param object
	 */
	public void addVariables(Object object) {
		try {
			Class<?> clazz = object.getClass();
			if(clazz.isAnnotationPresent(VariableBean.class)) {
				VariableField variableField;
				do {
					for(Field field : clazz.getDeclaredFields()) {
						variableField = field.getAnnotation(VariableField.class);
						if(variableField != null) 
							addVariable(StringUtil.isEmpty(variableField.name())?field.getName():variableField.name(), variableField.scope(), field.get(object));
					}
					clazz = clazz.getSuperclass();
				}while(clazz != Object.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	// 添加变量
	private void addVariable(String taskinstId, String name, Scope scope, DataType dataType, Object value) {
		switch(scope) {
			case GLOBAL:
				if(globalVariableMap == null)
					globalVariableMap = new HashMap<String, VariableEntity>();
				globalVariableMap.put(name, new VariableEntity(taskinstId, name, scope, dataType, value));
				
				if(!existsInLocalVariable(name) && !existsInTransientVariable(name))
					addVariable_(name, value);
				break;
			case LOCAL:
				if(localVariableMap == null)
					localVariableMap = new HashMap<String, VariableEntity>();
				localVariableMap.put(name, new VariableEntity(taskinstId, name, scope, dataType, value));
				
				if(!existsInTransientVariable(name))
					addVariable_(name, value);
				break;
			case TRANSIENT:
				if(transientVariableMap == null)
					transientVariableMap = new HashMap<String, VariableEntity>();
				transientVariableMap.put(name, new VariableEntity(taskinstId, name, scope, dataType, value));
				
				addVariable_(name, value);
				break;
		}
	}
	// 判断指定的变量名, 是否存在于local变量集合中
	private boolean existsInLocalVariable(String name) {
		if(localVariableMap == null)
			return false;
		return localVariableMap.containsKey(name);
	}
	// 判断指定的变量名, 是否存在于transient变量名集合中
	private boolean existsInTransientVariable(String name) {
		if(transientVariableMap == null)
			return false;
		return transientVariableMap.containsKey(name);
	}
	// 给variables中添加数据
	private void addVariable_(String name, Object value) {
		if(variableMap == null)
			variableMap = new HashMap<String, Object>();
		variableMap.put(name, value);
	}
}
