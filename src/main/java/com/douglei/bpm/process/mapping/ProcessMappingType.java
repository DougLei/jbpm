package com.douglei.bpm.process.mapping;

import java.io.InputStream;

import com.douglei.bpm.process.mapping.parser.ProcessMappingParser;
import com.douglei.orm.mapping.MappingSubject;
import com.douglei.orm.mapping.MappingType;
import com.douglei.orm.mapping.handler.entity.AddOrCoverMappingEntity;

/**
 * 
 * @author DougLei
 */
public class ProcessMappingType extends MappingType {
	public static final String NAME ="process";
	public static final String FILE_SUFFIX =".bpm.xml";
	
	private ProcessMappingParser parser;
	public ProcessMappingType(ProcessMappingParser parser) {
		super(NAME, FILE_SUFFIX, 70, false);
		this.parser = parser;
	}

	@Override
	public MappingSubject parse(AddOrCoverMappingEntity entity, InputStream input) throws Exception {
		return parser.parse(entity, input);
	}
}
