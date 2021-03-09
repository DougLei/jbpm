package com.douglei.bpm.process.mapping;

import com.douglei.bpm.process.mapping.metadata.ProcessMetadataAdapter;
import com.douglei.orm.mapping.Mapping;

/**
 * 
 * @author DougLei
 */
public class ProcessMapping extends Mapping {

	public ProcessMapping(ProcessMetadataAdapter metadata) {
		super(ProcessMappingType.NAME, metadata);
	}
}
