package com.douglei.test;

import com.douglei.bpm.ProcessEngine;
import com.douglei.bpm.ProcessEngineBuilder;

public class Test {
	public static void main(String[] args) {
		ProcessEngine engine = new ProcessEngineBuilder().build("test.jbpm.conf.xml");
		System.out.println(engine);
		engine.getRuntime().test();
	}
}
