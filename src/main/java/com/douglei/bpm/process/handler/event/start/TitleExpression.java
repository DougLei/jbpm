package com.douglei.bpm.process.handler.event.start;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import com.douglei.bpm.module.runtime.variable.VariableConstant;
import com.douglei.tools.instances.ognl.OgnlHandler;

/**
 * 
 * @author DougLei
 */
class TitleExpression {
	private String source; // 源字符串; 表达式就是从源字符串中提取出来的
	private List<String> expressions; // 表达式集合
	
	public TitleExpression(String source) {
		this.source = source;
		if(source.indexOf(VariableConstant.PREFIX) != -1) {
			Matcher prefixMatcher = VariableConstant.PREFIX_REGEX_PATTERN.matcher(source);
			Matcher suffixMatcher = VariableConstant.SUFFIX_REGEX_PATTERN.matcher(source);
			while(prefixMatcher.find()) {
				if(!suffixMatcher.find())
					return;
				addExpression(source.substring(prefixMatcher.start()+2, suffixMatcher.start()));
			}
		}
	}
	private void addExpression(String expression) {
		if(this.expressions == null) {
			this.expressions = new ArrayList<String>();
		}else if(this.expressions.contains(expression)) {
			return;
		}
		this.expressions.add(expression);
	}
	
	/**
	 * 获取标题
	 * @param variables
	 * @return
	 */
	public String getTitle(Map<String, Object> variables) {
		if(variables == null || expressions == null)
			return source;
		
		Object value = null;
		for (String expression : expressions) {
			value = OgnlHandler.getSingleton().getObjectValue(expression, variables);
			if(value != null)
				source = source.replaceAll(VariableConstant.PREFIX_4_REGEX + expression + VariableConstant.SUFFIX, value.toString());
		}
		return source;
	}
}
