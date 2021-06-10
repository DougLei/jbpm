package bpm.test;

import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;

import com.douglei.bpm.ProcessEngineBuilder;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.repository.delegation.DelegationBuilder;
import com.douglei.bpm.module.repository.delegation.DelegationService;

public class DelegationTest {
	private DelegationService service;
	
	@Before
	public void init() {
		service = new ProcessEngineBuilder().build().getRepositoryModule().getDelegationService();
	}
	
	@Test
	public void insert() throws Exception {
		DelegationBuilder builder = new DelegationBuilder();
		builder.setUserId("C");
		builder.setAssignedUserId("B");
		builder.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-6-12 18:00:01"));
		builder.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-6-18 18:00:00"));
		builder.setReason("R");
		builder.addDetail("报销", null);
		
		
		Result result = service.insert(builder);
		if(result.isSuccess())
			System.out.println("成功");
		else
			System.out.println("失败: "+ result.getMessage());
		
	}
	
	@Test
	public void update() throws Exception {
		DelegationBuilder builder = new DelegationBuilder();
		builder.setId(4);
		builder.setUserId("C");
		builder.setAssignedUserId("A");
		builder.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-6-14 18:00:01"));
		builder.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-6-18 18:00:00"));
		builder.setReason("R");
		
		
		Result result = service.update(builder);
		if(result.isSuccess())
			System.out.println("成功");
		else
			System.out.println("失败: "+ result.getMessage());
		
	}
}
