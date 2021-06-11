package com.douglei.bpm.module.repository.delegation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.module.repository.RepositoryException;

/**
 * 
 * @author DougLei
 */
public class DelegationBuilder {
	private int id;
	private String userId;
	private String assignedUserId;
	private long startTime;
	private long endTime;
	private String reason;
	private List<DelegationDetail> details;
	private boolean strict4Update;
	private boolean acceptNotDelegate;
	
	public void setId(int id) {
		this.id = id;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setAssignedUserId(String assignedUserId) {
		this.assignedUserId = assignedUserId;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime.getTime();
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime.getTime();
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public boolean isStrict4Update() {
		return strict4Update;
	}
	public boolean isAcceptNotDelegate() {
		return acceptNotDelegate;
	}
	
	/**
	 * 委托已被接受时, 是否进行强制修改; 强制修改会取消接受状态, 需要重新进行接受操作; 默认值为false
	 * @param strict4Update
	 */
	public void setStrict4Update(boolean strict4Update) {
		this.strict4Update = strict4Update;
	}
	
	/**
	 * 当可能出现无限循环委托的情况时, 是否接受不委托操作, 即在流程流转中不进行委托操作; 如果值为false, 则无法增加或修改委托信息; 默认值为false
	 * @param acceptNotDelegate
	 */
	public void setAcceptNotDelegate(boolean acceptNotDelegate) {
		this.acceptNotDelegate = acceptNotDelegate;
	}
	
	/**
	 * 添加具体要委托的流程
	 * @param code
	 * @param version 可为null
	 */
	public void addDetail(String code, String version) {
		if(code == null)
			throw new RepositoryException("委托明细指定的流程定义code不能为空");
		
		if(details== null) {
			details= new ArrayList<DelegationDetail>();
		}else {
			if(version == null) {
				for(int i=0;i<details.size();i++) {
					if(details.get(i).getProcdefCode().equals(code))
						details.remove(i--);
				}
			}else {
				for (DelegationDetail detail : details) {
					if(detail.getProcdefCode().equals(code) && detail.getProcdefVersion().equals(version)) 
						return;
				}
			}
		}
		details.add(new DelegationDetail(code, version));
	}
	
	/**
	 * 构建Delegation实例
	 * @return
	 */
	public Delegation build() {
		if(userId.equals(assignedUserId))
			throw new RepositoryException("创建委托失败, 不能委托给自己");
		if(new Date().getTime() > startTime)
			throw new RepositoryException("创建委托失败, 委托的开始时间不能早于当前时间");
		if(startTime >= endTime)
			throw new RepositoryException("创建委托失败, 委托的结束时间不能早于开始时间");
			
		Delegation delegation = new Delegation();
		delegation.setId(id);
		delegation.setUserId(userId);
		delegation.setAssignedUserId(assignedUserId);
		delegation.setStartTime(startTime);
		delegation.setEndTime(endTime);
		delegation.setReason(reason);
		delegation.setDetails(details);
		return delegation;
	}
}
