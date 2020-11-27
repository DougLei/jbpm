package com.douglei.bpm.process.container.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.douglei.bpm.process.container.ProcessContainer;
import com.douglei.bpm.process.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
public class ApplicationProcessContainer implements ProcessContainer {
	private final Map<Integer, ProcessMetadata> container = new ConcurrentHashMap<Integer, ProcessMetadata>(64);

	@Override
	public void clear() {
		if(!container.isEmpty())
			container.clear();
	}
	
	@Override
	public void addProcess(ProcessMetadata process) {
		container.put(process.getId(), process);
	}

	@Override
	public void deleteProcess(int id) {
		container.remove(id);
	}

	@Override
	public ProcessMetadata getProcess(int id) {
		return container.get(id);
	}
}
