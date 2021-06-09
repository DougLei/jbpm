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
	
	/**
	 * 添加具体要委托的流程
	 * @param code
	 * @param version 可为null
	 */
	public void addDetail(String code, String version) {
		if(code == null)
			throw new RepositoryException("委托明细指定的流程code不能为空");
		
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
