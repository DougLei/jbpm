package com.douglei.bpm.process.container.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.bpm.process.container.ProcessContainer;
import com.douglei.bpm.process.executer.Process;

/**
 * 
 * @author DougLei
 */
public class ApplicationProcessContainer implements ProcessContainer {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationProcessContainer.class);
	private Map<String, Process> container = new HashMap<String, Process>(64);

	@Override
	public void clear() {
		if(!container.isEmpty())
			container.clear();
	}
	
	@Override
	public Process addProcess(Process process) {
		String id = process.getId();
		Process exProcess = container.get(id);
		if(logger.isDebugEnabled() && exProcess != null) 
			logger.debug("覆盖id为[{}]的流程: {}", id, exProcess);
		
		container.put(id, process);
		return exProcess;
	}

	@Override
	public Process deleteProcess(String id) {
		return container.remove(id);
	}

	@Override
	public Process getProcess(String id) {
		return container.get(id);
	}

	@Override
	public boolean exists(String id) {
		return container.containsKey(id);
	}
}
