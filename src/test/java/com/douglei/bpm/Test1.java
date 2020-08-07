package com.douglei.bpm;

import org.junit.Test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;

public class Test1 {
	
	@Test
	public void init() {
		ProcessEngine engine = new ProcessEngineBuilder().build();
		System.out.println(engine);
	}
}
