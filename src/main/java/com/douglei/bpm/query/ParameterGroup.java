package com.douglei.bpm.query;

import java.util.ArrayList;
import java.util.List;

/**
 *参数组
 * @author DougLei
 */
public class ParameterGroup extends AbstractParameter{
	private List<AbstractParameter> parameters;

	/**
	 * 添加参数
	 * @param parameter
	 * @return
	 */
	public ParameterGroup addParameter(Parameter parameter) {
		if(parameters == null)
			parameters = new ArrayList<AbstractParameter>();
		parameters.add(parameter);
		return this;
	}
}
