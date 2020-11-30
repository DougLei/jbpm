package com.douglei.bpm.module.runtime.instance.command.start.parameter;

/**
 * 启动方式
 * @author DougLei
 */
public enum StartingMode {
	
	/**
	 * 使用流程定义的id启动流程
	 */
	BY_PROCESS_DEFINITION_ID,
	
	/**
	 * 使用流程定义的code和version启动最新版本的流程
	 */
	BY_PROCESS_DEFINITION_CODE_VERSION;
}
