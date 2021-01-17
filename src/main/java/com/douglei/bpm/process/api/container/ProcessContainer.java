package com.douglei.bpm.process.api.container;

import com.douglei.bpm.bean.annotation.DefaultInstance;
import com.douglei.bpm.process.metadata.ProcessMetadata;

/**
 * 流程容器
 * @author DougLei
 */
@DefaultInstance(clazz=ApplicationProcessContainer.class)
public interface ProcessContainer {
	
	/**
	 * 清空存储容器
	 */
	void clear();
	
	/**
	 * 添加流程, 如果存在相同id的流程, 将其cover; 如果不存在相同id的流程, 将其add
	 * @param process
	 */
	void addProcess(ProcessMetadata process);
	
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
	ProcessMetadata getProcess(int id);
}