package com.douglei.bpm.module.repository.delegation;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.repository.RepositoryException;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;

/**
 * 
 * @author DougLei
 */
@Bean(isTransaction=true)
public class DelegationService {
	
	/**
	 * 添加委托
	 * @param builder
	 * @return
	 */
	@Transaction
	public Result insert(DelegationBuilder builder) {
		Delegation delegation = builder.build();
		
		// 递归查询被委托人委托了哪些流程, 这些流程是否和当前委托的流程相同; 如果不相同, 则结束, 否则继续递归查询和判断
		
		
		
		
		
		
		
		
		
		SessionContext.getTableSession().save(delegation);
		if(delegation.getDetails() != null)
			SessionContext.getTableSession().save(delegation.getDetails());
		return Result.getDefaultSuccessInstance();
	}
	
	/**
	 * 修改委托
	 * @param builder
	 * @return
	 */
	@Transaction
	public Result update(DelegationBuilder builder) {
		Delegation delegation = builder.build();
		
		// 判断是否存在指定id的委托信息
		Delegation old = SessionContext.getSqlSession().uniqueQuery(Delegation.class, "select * from bpm_re_delegation where id=?", Arrays.asList(delegation.getId()));
		if(old == null)
			throw new RepositoryException("修改委托失败, 不存在id为["+delegation.getId()+"]的委托");
		
		
		
		
		// 如果已经接受, 然后其修改了明细, 要判断是否需要重新接受, 以及激活状态
		
		
		
		
		
		
		
		SessionContext.getTableSession().update(delegation);
		SessionContext.getSqlSession().executeUpdate("delete bpm_re_delegation_detail where delegation_id=?", Arrays.asList(delegation.getId()));
		if(delegation.getDetails() != null)
			SessionContext.getTableSession().save(delegation.getDetails());
		return Result.getDefaultSuccessInstance();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 接受委托
	 * @param delegationId
	 * @param assigneeUserId
	 * @return
	 */
	@Transaction
	public Result accept(int delegationId, String assigneeUserId) {
		Delegation delegation = SessionContext.getSqlSession().uniqueQuery(Delegation.class, "select assigned_user_id, end_time, accept_time from bpm_re_delegation where id=?", Arrays.asList(delegationId));
		if(delegation == null)
			throw new RepositoryException("接受委托失败, 不存在id为["+delegationId+"]的委托");
		if(!delegation.getAssignedUserId().equals(assigneeUserId))
			throw new RepositoryException("接受委托失败, 委托配置的接受人id与实际接受人id不一致");
		if(delegation.isAccept())
			throw new RepositoryException("接受委托失败, 委托已被接受");
		
		Date currentDate = new Date();
		if(delegation.getEndTime() < currentDate.getTime())
			return new Result("接受委托失败, 当前时间已晚于委托配置的结束时间", "jbpm.delegation.accept.fail.too.late");
		
		SessionContext.getSqlSession().executeUpdate("update bpm_re_delegation set is_enabled=1, accept_time=? where id=?", Arrays.asList(currentDate, delegationId));
		return Result.getDefaultSuccessInstance();
	}
	
	/**
	 * 启用委托
	 * @param delegationId
	 * @return
	 */
	@Transaction
	public Result enabled(int delegationId) {
		Delegation delegation = SessionContext.getSqlSession().uniqueQuery(Delegation.class, "select end_time, accept_time, is_enabled from bpm_re_delegation where id=?", Arrays.asList(delegationId));
		if(delegation == null)
			throw new RepositoryException("启用委托失败, 不存在id为["+delegationId+"]的委托");
		if(delegation.isEnabled())
			return new Result("启用委托失败, 委托已启用", "jbpm.delegation.enabled.fail.already.enabled");
		if(!delegation.isAccept())
			return new Result("启用委托失败, 委托还未被指派人接受", "jbpm.delegation.enabled.fail.unaccept");
		if(delegation.getEndTime() < new Date().getTime())
			return new Result("启用委托失败, 当前时间已晚于委托配置的结束时间", "jbpm.delegation.enabled.fail.too.late");
		
		updateState(delegationId, true);
		return Result.getDefaultSuccessInstance();
	}
	
	/**
	 * 禁用委托
	 * @param delegationId
	 * @return
	 */
	@Transaction
	public Result disabled(int delegationId) {
		Object[] obj = SessionContext.getSqlSession().uniqueQuery_("select is_enabled from bpm_re_delegation where id=?", Arrays.asList(delegationId));
		if(obj == null)
			throw new RepositoryException("禁用委托失败, 不存在id为["+delegationId+"]的委托");
		if("0".equals(obj[0].toString()))
			return new Result("禁用委托失败, 委托已禁用", "jbpm.delegation.disabled.fail.already.disabled");
		
		updateState(delegationId, false);
		return Result.getDefaultSuccessInstance();
	}
	
	// 更新状态
	private void updateState(int delegationId, boolean isEnabled) {
		SessionContext.getSqlSession().executeUpdate("update bpm_re_delegation set is_enabled=? where id=?", Arrays.asList(isEnabled?1:0, delegationId));
	}
	
	/**
	 * 删除委托
	 * @param delegationId
	 * @return
	 */
	public Result delete(int delegationId) {
		List<Object> params = Arrays.asList(delegationId);
		if(SessionContext.getSqlSession().executeUpdate("delete bpm_re_delegation where id=?", params) == 1)
			SessionContext.getSqlSession().executeUpdate("delete bpm_re_delegation_detail where delegation_id=?", params);
		return Result.getDefaultSuccessInstance();
	}
}
