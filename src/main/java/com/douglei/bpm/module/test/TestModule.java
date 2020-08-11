package com.douglei.bpm.module.test;

import com.douglei.bpm.ProcessEngineBean;
import com.douglei.bpm.ProcessEngineField;

/**
 * 
 * @author DougLei
 */
@ProcessEngineBean
public class TestModule{
	
	@ProcessEngineField
	private TestService testService;
	
	public void countQueryTest() {
		System.out.println("查询结果数量为：" + testService.countQuery());
	}
}
