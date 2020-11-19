package com.douglei.bpm.module.runtime.instance.start;

/**
 * 启动方式
 * @author DougLei
 */
public enum StartingMode {
	
	/**
	 * 使用流程定义的id启动
	 */
	BY_PROCESS_DEFINITION_ID,
	
	/**
	 * 使用流程定义的code和version启动
	 */
	BY_PROCESS_DEFINITION_CODE_VERSION;
}
