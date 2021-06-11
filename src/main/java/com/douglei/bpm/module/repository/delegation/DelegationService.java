package com.douglei.bpm.module.repository.delegation;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
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
	
	// 保存委托明细
	private void saveDetails(int delegationId, List<DelegationDetail> details) {
		if(details == null)
			return;
		
		details.forEach(detail -> detail.setDelegationId(delegationId));
		SessionContext.getTableSession().save(details);
	}
	
	/**
	 * 递归验证器, 防止出现委托无限循环的情况
	 * @author DougLei
	 */
	private class RecursiveValidator {
		private Delegation origin;
		private boolean acceptNotDelegate;
		private HashSet<String> userIds; // 二次委托的userId集合
		public RecursiveValidator(Delegation origin, boolean acceptNotDelegate) {
			this.origin = origin;
			this.acceptNotDelegate = acceptNotDelegate;
		}
		
		// 执行验证
		public Result execute() {
			DelegationSqlCondition condition = new DelegationSqlCondition(origin.getStartTime(), origin.getEndTime(), origin.getAssignedUserId());
			return execute_(condition, SessionContext.getSQLSession().query(DelegationInfo.class, "Delegation", "queryDelegations4Op", condition));
		}
		
		// 执行验证(内部方法)
		private Result execute_(DelegationSqlCondition condition, List<DelegationInfo> delegations) {
			if(delegations.isEmpty())
				return null;
			
			HashSet<String> userIds = getUserIds();
			if(origin.getDetails() == null) { // 委托了所有流程
				delegations.forEach(delegation -> userIds.add(delegation.getAssignedUserId()));
			}else { // 委托了部分流程
				for (DelegationInfo delegation : delegations) {
					if(delegation.getProcdefCode() == null) {
						userIds.add(delegation.getAssignedUserId());
						continue;
					}
					
					for(DelegationDetail detail: origin.getDetails()) {
						if(delegation.getProcdefCode().equals(detail.getProcdefCode()) 
								&& (detail.getProcdefVersion()== null || delegation.getProcdefVersion()==null || detail.getProcdefVersion().equals(delegation.getProcdefVersion()))) {
							userIds.add(delegation.getAssignedUserId());
							break;
						}
					}
				}
			}
			
			if(userIds.isEmpty())
				return null;
			if(userIds.contains(origin.getUserId()))
				return new Result("委托失败, 可能会出现无限循环委托的情况", "jbpm.delegation.op.fail.maybe.infinite.cycle");

//				jbpm.delegation.op.fail.willbe.infinite.cycle=委托失败, 会出现无限循环委托的情况
			
			condition.resetUserIds(userIds);
			return execute_(condition, SessionContext.getSQLSession().query(DelegationInfo.class, "Delegation", "queryDelegations4Op", condition));
		}
		
		// 获取二次委托的userId集合
		private HashSet<String> getUserIds() {
			if(userIds == null)
				userIds = new HashSet<String>(16);
			else
				userIds.clear();
			return userIds;
		}
	}
	
	/**
	 * 添加委托
	 * @param builder
	 * @return
	 */
	@Transaction
	public Result insert(DelegationBuilder builder) {
		Delegation delegation = builder.build();

		// 递归验证, 防止出现委托无限循环的情况
		Result result = new RecursiveValidator(delegation, builder.isAcceptNotDelegate()).execute();
		if(result != null)
			return result;
		
		SessionContext.getTableSession().save(delegation);
		saveDetails(delegation.getId(), delegation.getDetails());
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
		Delegation old = SessionContext.getSqlSession().uniqueQuery(Delegation.class, "select * from bpm_re_delegation where id=?", Arrays.asList(delegation.getId()));
		if(old == null)
			throw new RepositoryException("修改委托失败, 不存在id为["+delegation.getId()+"]的委托");
		if(old.isAccept() && !builder.isStrict4Update())
			return new Result("修改委托失败, 委托已被接受", "jbpm.delegation.update.fail.accepted");
		
		// 递归验证, 防止出现委托无限循环的情况
		Result result = new RecursiveValidator(delegation, builder.isAcceptNotDelegate()).execute();
		if(result != null)
			return result;
		
		SessionContext.getTableSession().update(delegation, true);
		SessionContext.getSqlSession().executeUpdate("delete bpm_re_delegation_detail where delegation_id=?", Arrays.asList(delegation.getId()));
		saveDetails(delegation.getId(), delegation.getDetails());
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
