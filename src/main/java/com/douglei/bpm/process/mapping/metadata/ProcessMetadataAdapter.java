package com.douglei.bpm.process.mapping.metadata;

import com.douglei.orm.mapping.metadata.AbstractMetadata;

/**
 * 
 * @author DougLei
 */
public class ProcessMetadataAdapter extends AbstractMetadata {
	private String code;
	private ProcessMetadata processMetadata;
	
	public ProcessMetadataAdapter(ProcessMetadata processMetadata) {
		this.code = processMetadata.getId()+"";
		this.name = processMetadata.getName();
		this.processMetadata = processMetadata;
	}

	@Override
	public String getCode() {
		return code;
	}

	public ProcessMetadata getProcessMetadata() {
		return processMetadata;
	}
}
