package com.douglei.bpm.process.mapping;

import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.orm.mapping.handler.entity.AddOrCoverMappingEntity;

/**
 * 
 * @author DougLei
 */
public class AddOrCoverMappingEntity4Process extends AddOrCoverMappingEntity {
	private int id;
	
	public AddOrCoverMappingEntity4Process(ProcessDefinition definition) {
		super(definition.getContent(), ProcessMappingType.NAME);
		this.id = definition.getId();
	}

	public int getId() {
		return id;
	}
}
