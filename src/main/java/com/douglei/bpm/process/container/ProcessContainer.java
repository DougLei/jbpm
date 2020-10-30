package com.douglei.bpm.process.container;

import com.douglei.bpm.process.executer.Process;

/**
 * 流程容器
 * @author DougLei
 */
public interface ProcessContainer {
	
	/**
	 * 清空存储容器
	 */
	void clear();
	
	/**
	 * 添加流程, 如果存在相同id的流程, 将其cover; 如果不存在相同id的流程, 将其add
	 * @param process
	 * @return 如果存在相同id的流程, 返回被覆盖的流程实例, 否则返回null
	 */
	Process addProcess(Process process);
	
	/**
	 * 删除流程
	 * @param id
	 * @return 返回被删除的流程实例, 如果没有流程, 返回null
	 */
	Process deleteProcess(String id);
	
	/**
	 * 获取流程
	 * @param id
	 * @return 如果没有查询到, 返回null
	 */
	Process getProcess(String id);
	
	/**
	 * 指定id的流程是否存在
	 * @param id
	 * @return
	 */
	boolean exists(String id);
}
