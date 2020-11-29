package com.douglei.bpm.module.components.expression;

import java.util.regex.Pattern;

/**
 * 
 * @author DougLei
 */
public class ExpressionConstants {
	
	/**
	 * 表达式前缀
	 */
	public static final String PREFIX = "#{";
	/**
	 * 表达式前缀 for 正则表达式, 针对{符号进行了转义
	 */
	public static final String PREFIX_4_REGEX = "#\\{";
	/**
	 * 表达式后缀
	 */
	public static final String SUFFIX = "}";
	
	/**
	 * 表达式前缀正则表达式实例
	 */
	static final Pattern PREFIX_REGEX_PATTERN = Pattern.compile(PREFIX_4_REGEX);
	
	/**
	 * 表达式后缀正则表达式实例
	 */
	static final Pattern SUFFIX_REGEX_PATTERN = Pattern.compile(SUFFIX);
}
