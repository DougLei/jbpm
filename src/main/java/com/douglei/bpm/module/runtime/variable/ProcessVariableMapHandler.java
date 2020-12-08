package com.douglei.bpm.module.runtime.variable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.douglei.bpm.module.runtime.variable.entity.Variable;
import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
public class ProcessVariableMapHandler extends ProcessVariableMapHolder{
	
	/**
	 * 添加变量
	 * @param name
	 * @param scope
	 * @param value
	 */
	public void addVariable(String name, Scope scope, Object value) {
		addVariable(name, scope, DataType.getByObjectValue(value), value);
	}
	
	/**
	 * 添加变量
	 * @param name
	 * @param scope
	 * @param dataType
	 * @param value
	 */
	public void addVariable(String name, Scope scope, DataType dataType, Object value) {
		switch(scope) {
			case GLOBAL:
				if(globalVariableMap == null)
					globalVariableMap = new HashMap<String, ProcessVariable>();
				globalVariableMap.put(name, new ProcessVariable(name, scope, dataType, value));
				
				if(!existsInLocalVariableMap(name) && !existsInTransientVariableNames(name))
					put2VariableMap(name, value);
				break;
			case LOCAL:
				if(localVariableMap == null)
					localVariableMap = new HashMap<String, ProcessVariable>();
				localVariableMap.put(name, new ProcessVariable(name, scope, dataType, value));
				
				if(!existsInTransientVariableNames(name))
					put2VariableMap(name, value);
				break;
			case TRANSIENT:
				if(transientVariableNames == null)
					transientVariableNames = new ArrayList<String>();
				if(transientVariableNames.isEmpty() || !transientVariableNames.contains(name))
					transientVariableNames.add(name);
				
				put2VariableMap(name, value);
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
	// 给variableMap中添加数据
	private void put2VariableMap(String name, Object value) {
		if(variableMap == null)
			variableMap = new HashMap<String, Object>();
		variableMap.put(name, value);
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
			if(clazz.isAnnotationPresent(ProcessVariableBean.class)) {
				ProcessVariableField variableField;
				do {
					for(Field field : clazz.getDeclaredFields()) {
						variableField = field.getAnnotation(ProcessVariableField.class);
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
	
	/**
	 * 添加变量
	 * @param variable
	 */
	public void addVariable(Variable variable) {
		DataType dataType = DataType.getByString(variable.getDataType());
		Object value = null;
		switch(dataType) {
			case STRING:
				value = variable.getStringVal();
				break;
			case NUMBER:
				value = variable.getNumberVal();
				break;
			case DATETIME:
				value = variable.getDateVal();
				break;
			case OBJECT:
				value = variable.getObjectVal();
				break;
		}
		addVariable(variable.getName(), Scope.getByString(variable.getScope()), dataType, value);
	}
}
