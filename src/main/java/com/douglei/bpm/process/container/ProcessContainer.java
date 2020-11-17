package com.douglei.bpm.process.container;

import com.douglei.bpm.process.executor.Process;

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
	 */
	void addProcess(Process process);
	
	/**
	 * 删除流程
	 * @param id
	 */
	void deleteProcess(int id);
	
	/**
	 * 获取流程
	 * @param id
	 * @return 如果没有查询到, 返回null
	 */
	Process getProcess(int id);
	
	/**
	 * 指定id的流程是否存在
	 * @param id
	 * @return
	 */
	boolean exists(int id);
}
