package com.douglei.bpm.process.handler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.module.execution.variable.DataType;
import com.douglei.bpm.module.execution.variable.Scope;
import com.douglei.bpm.module.execution.variable.runtime.Variable;
import com.douglei.tools.StringUtil;

/**
 * 
 * @author DougLei
 */
public class VariableEntities {
	private String taskinstId;
	private Map<String, Object> variableMap;
	private Map<String, VariableEntity> globalVariableMap;
	private Map<String, VariableEntity> localVariableMap;
	private Map<String, VariableEntity> transientVariableMap;
	
	public VariableEntities() {}
	public VariableEntities(String taskinstId, List<Variable> variables) {
		this.taskinstId = taskinstId;
		if(variables.size() > 0) {
			for (Variable variable : variables) 
				addVariable(variable.getTaskinstId(), variable.getName(), variable.getScopeInstance(), variable.getDataTypeInstance(), variable.getValue());
		}
	}
	
	/**
	 * 设置关联的任务实例id
	 * @param taskinstId 
	 */
	public void setTaskinstId(String taskinstId) {
		this.taskinstId = taskinstId;
	}
	/**
	 * 获取关联的任务实例id
	 * @return
	 */
	public String getTaskinstId() {
		return taskinstId;
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
	// 给variables中添加数据
	private void addVariable_(String name, Object value) {
		if(variableMap == null)
			variableMap = new HashMap<String, Object>();
		variableMap.put(name, value);
	}
	
	/**
	 * 获取流程变量
	 * @return 返回非null集合
	 */
	public Map<String, Object> getVariableMap() {
		if(variableMap== null)
			return Collections.emptyMap();
		return variableMap;
	}
	/**
	 * 获取global范围的变量
	 * @return 返回非null集合
	 */
	public Map<String, VariableEntity> getGlobalVariableMap() {
		if(globalVariableMap== null)
			return Collections.emptyMap();
		return globalVariableMap;
	}
	/**
	 * 获取local范围的变量
	 * @return 返回非null集合
	 */
	public Map<String, VariableEntity> getLocalVariableMap() {
		if(localVariableMap== null)
			return Collections.emptyMap();
		return localVariableMap;
	}
	/**
	 * 获取transient范围的变量
	 * @return 返回非null集合
	 */
	public Map<String, VariableEntity> getTransientVariableMap() {
		if(transientVariableMap== null)
			return Collections.emptyMap();
		return transientVariableMap;
	}
	
	/**
	 * 是否存在变量
	 * @return
	 */
	public boolean existsVariable() {
		return variableMap != null && variableMap.size()>0; 
	}
	/**
	 * 是否存在global范围的变量
	 * @return
	 */
	public boolean existsGlobalVariable() {
		return globalVariableMap != null && globalVariableMap.size()>0;
	}
	/**
	 * 是否存在local范围的变量
	 * @return
	 */
	public boolean existsLocalVariable() {
		return localVariableMap != null && localVariableMap.size()>0;
	}
	/**
	 * 是否存在transient范围的变量
	 * @return
	 */
	public boolean existsTransientVariable() {
		return transientVariableMap != null && transientVariableMap.size()>0;
	}
	
	/**
	 * 判断指定name的变量, 是否存在于变量集合中
	 * @param name
	 * @return
	 */
	public boolean existsInVariable(String name) {
		if(variableMap == null)
			return false;
		return variableMap.containsKey(name);
	}
	/**
	 * 判断指定name的变量, 是否存在于global变量集合中
	 * @param name
	 * @return
	 */
	public boolean existsInGlobalVariable(String name) {
		if(globalVariableMap == null)
			return false;
		return globalVariableMap.containsKey(name);
	}
	/**
	 * 判断指定name的变量, 是否存在于local变量集合中
	 * @param name
	 * @return
	 */
	public boolean existsInLocalVariable(String name) {
		if(localVariableMap == null)
			return false;
		return localVariableMap.containsKey(name);
	}
	/**
	 * 判断指定name的变量, 是否存在于transient变量集合中
	 * @param name
	 * @return
	 */
	public boolean existsInTransientVariable(String name) {
		if(transientVariableMap == null)
			return false;
		return transientVariableMap.containsKey(name);
	}
	
	/**
	 * 移除指定name的流程变量
	 * @param name
	 */
	public void removeVariable(String name) {
		if(variableMap == null || variableMap.remove(name) == null)
			return;
		
		removeGlobalVariable(name);
		removeLocalVariable(name);
		removeTransientVariable(name);
	}
	/**
	 * 移除所有流程变量
	 */
	public void removeAllVariable() {
		if(variableMap != null) 
			variableMap.clear();
		
		if(globalVariableMap != null) 
			globalVariableMap.clear();
		
		if(localVariableMap != null) 
			localVariableMap.clear();
		
		if(transientVariableMap != null) 
			transientVariableMap.clear();
	}
	
	/**
	 * 移除global范围中, 指定name的流程变量
	 * @param name
	 */
	public void removeGlobalVariable(String name) {
		if(globalVariableMap == null || globalVariableMap.remove(name) == null || existsInLocalVariable(name) || existsInTransientVariable(name))
			return;
		
		variableMap.remove(name);
	}
	/**
	 * 移除global范围中所有流程变量
	 */
	public void removeAllGlobalVariable() {
		if(globalVariableMap == null || globalVariableMap.size()==0)
			return;
		
		List<String> names = new ArrayList<String>(globalVariableMap.size());
		globalVariableMap.keySet().forEach(key -> names.add(key));
		names.forEach(name -> removeGlobalVariable(name));
	}
	
	/**
	 * 移除local范围中, 指定name的流程变量
	 * @param name
	 */
	public void removeLocalVariable(String name) {
		if(localVariableMap == null || localVariableMap.remove(name) == null || existsInTransientVariable(name))
			return;
		
		// 补充或删除变量
		if(existsInGlobalVariable(name)) 
			addVariable_(name, globalVariableMap.get(name));
		else
			variableMap.remove(name);
	}
	/**
	 * 移除local范围中所有流程变量
	 */
	public void removeAllLocalVariable() {
		if(localVariableMap == null || localVariableMap.size()==0)
			return;
		
		List<String> names = new ArrayList<String>(localVariableMap.size());
		localVariableMap.keySet().forEach(key -> names.add(key));
		names.forEach(name -> removeLocalVariable(name));
	}
	
	/**
	 * 移除transient范围中, 指定name的流程变量
	 * @param name
	 */
	public void removeTransientVariable(String name) {
		if(transientVariableMap == null || transientVariableMap.remove(name) == null)
			return;
		
		// 补充或删除变量
		if(existsInLocalVariable(name)) {
			addVariable_(name, localVariableMap.get(name));
		}else if(existsInGlobalVariable(name)) {
			addVariable_(name, globalVariableMap.get(name));
		}else {
			variableMap.remove(name);
		}
	}
	/**
	 * 移除transient范围中所有流程变量
	 */
	public void removeAllTransientVariable() {
		if(transientVariableMap == null || transientVariableMap.size()==0)
			return;
		
		List<String> names = new ArrayList<String>(transientVariableMap.size());
		transientVariableMap.keySet().forEach(key -> names.add(key));
		names.forEach(name -> removeTransientVariable(name));
	}
}
