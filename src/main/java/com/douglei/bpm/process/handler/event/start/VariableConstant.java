package com.douglei.bpm.process.handler.event.start;

import java.util.regex.Pattern;

/**
 * 
 * @author DougLei
 */
public class VariableConstant {
	
	/**
	 * 变量前缀
	 */
	public static final String PREFIX = "#{";
	/**
	 * 变量前缀 4 正则表达式使用, 因为{是正则表达式关键字, 所以需要转义
	 */
	public static final String PREFIX_4_REGEX = "#\\{";
	/**
	 * 变量后缀
	 */
	public static final String SUFFIX = "}";
	
	/**
	 * 变量前缀的正则模式实例
	 */
	public static final Pattern PREFIX_REGEX_PATTERN = Pattern.compile(PREFIX_4_REGEX);
	/**
	 * 变量后缀的正则模式实例
	 */
	public static final Pattern SUFFIX_REGEX_PATTERN = Pattern.compile(SUFFIX);
}
