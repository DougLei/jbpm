package com.douglei.bpm.module.components.expression;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.douglei.tools.instances.ognl.OgnlHandler;

/**
 * 
 * @author DougLei
 */
public class Expression implements Serializable {
	private String source; // 源字符串; 表达式就是从源字符串中提取出来的
	private List<String> expressions; // 表达式集合
	
	public Expression(String source) {
		this.source = source;
		if(source.indexOf(ExpressionConstants.PREFIX) != -1) {
			Matcher prefixMatcher = ExpressionConstants.PREFIX_REGEX_PATTERN.matcher(source);
			Matcher suffixMatcher = ExpressionConstants.SUFFIX_REGEX_PATTERN.matcher(source);
			while(prefixMatcher.find()) {
				if(suffixMatcher.find()) {
					addExpression(source.substring(prefixMatcher.start()+2, suffixMatcher.start()));
				}else {
					throw new IllegalArgumentException("表达式配置异常, ["+ExpressionConstants.SUFFIX+"]标识符不匹配(少一个), 请检查");
				}
			}
			if(suffixMatcher.find())
				throw new IllegalArgumentException("表达式配置异常, ["+ExpressionConstants.PREFIX+"]标识符不匹配(至少少一个), 请检查");
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
	
	public String getSource() {
		return source;
	}
	public boolean exists() {
		return expressions != null;
	}
	
	private int pointer;
	public boolean hasNext() {
		return pointer+1 < expressions.size();
	}
	public boolean next() {
		pointer++;
		return pointer < expressions.size();
	}
	public String getExpression() {
		return expressions.get(pointer);
	}
	public Object getValue(Object obj) {
		return OgnlHandler.getSingleton().getObjectValue(expressions.get(pointer), obj);
	}
	public void reset() {
		pointer=0;
	}
	
	@Override
	public String toString() {
		return "Expression [source=" + source + ", expressions=" + expressions + "]";
	}
}
