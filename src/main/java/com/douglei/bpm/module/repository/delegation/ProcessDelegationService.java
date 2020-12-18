package com.douglei.bpm.module.repository.delegation;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 
 * @author DougLei
 */
@Bean(isTransaction=true)
public class ProcessDelegationService {
	
	/*
	 * 添加
	 * 修改
	 * 
	 * */
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 删除委托配置
	 * @param delegationId
	 * @return
	 */
	public ExecutionResult delete(int delegationId) {
		List<Object> paramList = Arrays.asList(delegationId);
		if(SessionContext.getSqlSession().uniqueQuery_("select id from bpm_re_delegation where id=?", paramList) == null)
			return new ExecutionResult("删除失败, 不存在id为["+delegationId+"]的委托配置");
		
		SessionContext.getSqlSession().executeUpdate("delete bpm_re_delegation where id=?", paramList);
		SessionContext.getSqlSession().executeUpdate("delete bpm_re_delegation_detail where delegation_id=?", paramList);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	
	/**
	 * 启用委托配置
	 * @param delegationId
	 * @return
	 */
	@Transaction
	public ExecutionResult enabled(int delegationId) {
		ProcessDelegation delegation = SessionContext.getSqlSession().uniqueQuery(ProcessDelegation.class, "select end_time, is_enabled from bpm_re_delegation where id=?", Arrays.asList(delegationId));
		if(delegation == null)
			return new ExecutionResult("启用失败, 不存在id为["+delegationId+"]的委托配置");
		if(delegation.isEnabled())
			return new ExecutionResult("启用失败, 委托配置已启用");
		if(delegation.getEndTime() < new Date().getTime())
			return new ExecutionResult("启用失败, 当前时间已晚于委托配置的结束时间");
		
		updateState(delegationId, 1);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 禁用委托配置
	 * @param delegationId
	 * @return
	 */
	@Transaction
	public ExecutionResult disabled(int delegationId) {
		Object[] obj = SessionContext.getSqlSession().uniqueQuery_("select is_enabled from bpm_re_delegation where id=?", Arrays.asList(delegationId));
		if(obj == null)
			return new ExecutionResult("禁用失败, 不存在id为["+delegationId+"]的委托配置");
		if("0".equals(obj[0].toString()))
			return new ExecutionResult("禁用失败, 委托配置已禁用");
		
		updateState(delegationId, 0);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	// 更新状态
	private void updateState(int delegationId, int isEnabled) {
		SessionContext.getSqlSession().executeUpdate("update bpm_re_delegation set is_enabled=? where id=?", Arrays.asList(isEnabled, delegationId));
	}
}
