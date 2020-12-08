package com.douglei.bpm.process.scheduler.event.start;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.douglei.tools.instances.ognl.OgnlHandler;

/**
 * 
 * @author DougLei
 */
class TitleExpression {
	private static final String PREFIX = "#{";
	private static final String PREFIX_4_REGEX = "#\\{";
	private static final String SUFFIX = "}";
	private static final Pattern PREFIX_REGEX_PATTERN = Pattern.compile(PREFIX_4_REGEX);
	private static final Pattern SUFFIX_REGEX_PATTERN = Pattern.compile(SUFFIX);
	
	private String source; // 源字符串; 表达式就是从源字符串中提取出来的
	private List<String> expressions; // 表达式集合
	
	public TitleExpression(String source) {
		this.source = source;
		if(source.indexOf(PREFIX) != -1) {
			Matcher prefixMatcher = PREFIX_REGEX_PATTERN.matcher(source);
			Matcher suffixMatcher = SUFFIX_REGEX_PATTERN.matcher(source);
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
				source = source.replaceAll(PREFIX_4_REGEX + expression + SUFFIX, value.toString());
		}
		return source;
	}
}
