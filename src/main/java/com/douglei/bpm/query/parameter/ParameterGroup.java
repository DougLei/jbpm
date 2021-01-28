package com.douglei.bpm.query.parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * 参数组, 即多参数
 * @author DougLei
 */
public class ParameterGroup extends AbstractParameter{
	private List<Parameter> parameters;

	/**
	 * 添加参数
	 * @param parameter
	 * @return
	 */
	public ParameterGroup addParameter(Parameter parameter) {
		if(parameters == null)
			parameters = new ArrayList<Parameter>();
		parameters.add(parameter);
		return this;
	}
}
