package com.douglei.bpm.process.container.impl;

import java.util.HashMap;
import java.util.Map;

import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.process.container.ProcessContainer;
import com.douglei.bpm.process.node.Process;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=ProcessContainer.class, isTransaction=false, isLazy=true)
public class ApplicationProcessContainer implements ProcessContainer {
	private final Map<Integer, Process> container = new HashMap<Integer, Process>(64);

	@Override
	public void clear() {
		if(!container.isEmpty())
			container.clear();
	}
	
	@Override
	public void addProcess(Process process) {
		container.put(process.getId(), process);
	}

	@Override
	public void deleteProcess(int id) {
		container.remove(id);
	}

	@Override
	public Process getProcess(int id) {
		return container.get(id);
	}

	@Override
	public boolean exists(int id) {
		return container.containsKey(id);
	}
}
