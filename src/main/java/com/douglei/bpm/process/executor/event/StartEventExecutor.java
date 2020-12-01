package com.douglei.bpm.process.executor.event;

import com.douglei.bpm.process.executor.AbstractExecutor;
import com.douglei.bpm.process.metadata.node.event.StartEventMetadata;

/**
 * 
 * @author DougLei
 */
public class StartEventExecutor extends AbstractExecutor<StartEventMetadata> {

	public StartEventExecutor(StartEventMetadata startEvent) {
		super(startEvent);
	}
	
	
	
}
