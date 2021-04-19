package com.douglei.bpm.module;

import com.douglei.bpm.ProcessEngineBeans;

/**
 * 
 * @author DougLei
 */
public interface Command {

	/**
	 * 
	 * @param processEngineBeans
	 * @return
	 */
	Result execute(ProcessEngineBeans processEngineBeans);
}
