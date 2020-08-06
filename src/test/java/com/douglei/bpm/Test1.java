package com.douglei.bpm;

import org.junit.Test;

public class Test1 {
	
	@Test
	public void init() {
		ProcessEngine engine = new ProcessEngineBuilder().build();
		engine.getRepository().getProcessTypeService().add();
	}
}
