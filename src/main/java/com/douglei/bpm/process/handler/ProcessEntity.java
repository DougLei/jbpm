package com.douglei.bpm.process.handler;

import com.douglei.bpm.process.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
public class ProcessEntity {
	private String procinstId; // 流程实例id
	private ProcessMetadata processMetadata; // 流程元数据实例

	public ProcessEntity(String procinstId, ProcessMetadata processMetadata) {
		this.procinstId = procinstId;
		this.processMetadata = processMetadata;
	}
	
	public String getProcinstId() {
		return procinstId;
	}
	public ProcessMetadata getProcessMetadata() {
		return processMetadata;
	}
}
