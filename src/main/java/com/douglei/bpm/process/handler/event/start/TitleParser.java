package com.douglei.bpm.process.handler.event.start;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import com.douglei.tools.instances.ognl.OgnlHandler;

/**
 * 标题解析器
 * @author DougLei
 */
class TitleParser {
	private String title; // 源字符串; 表达式就是从源字符串中提取出来的
	private List<String> expressions; // 表达式集合
	
	public TitleParser(String title, Map<String, Object> variableMap) {
		this.title = title;
		if(variableMap == null)
			return;
		
		if(title.indexOf(VariableConstant.PREFIX) != -1) {
			Matcher prefixMatcher = VariableConstant.PREFIX_REGEX_PATTERN.matcher(title);
			Matcher suffixMatcher = VariableConstant.SUFFIX_REGEX_PATTERN.matcher(title);
			while(prefixMatcher.find()) {
				if(!suffixMatcher.find())
					return;
				addExpression(title.substring(prefixMatcher.start()+2, suffixMatcher.start()));
			}
		}
		if(expressions == null)
			return;
		
		Object value = null;
		for (String expression : expressions) {
			value = OgnlHandler.getSingleton().getObjectValue(expression, variableMap);
			if(value != null)
				this.title = this.title.replaceAll(VariableConstant.PREFIX_4_REGEX + expression + VariableConstant.SUFFIX, value.toString());
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
	 * 获取(解析后的)标题
	 * @return
	 */
	public String getTitle() {
		return title;
	}
}
