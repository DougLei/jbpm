package com.douglei.bpm.process.metadata.entity;

import java.util.List;

import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.flow.FlowMetadata;

/**
 * 
 * @author DougLei
 */
public class StartEventMetadataEntity extends TaskMetadataEntity {

	public StartEventMetadataEntity(TaskMetadata taskMetadata, List<FlowMetadata> flows) {
		super(taskMetadata, flows, false, true, taskMetadata.getDefaultFlowId()==null?false:true);
	}
}
