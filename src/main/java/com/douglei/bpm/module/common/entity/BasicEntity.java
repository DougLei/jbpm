package com.douglei.bpm.module.common.entity;

import java.util.Date;

/**
 * 
 * @author DougLei
 */
public abstract class BasicEntity extends IdEntity{
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
		setUpdateUser(createUser);
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
		setUpdateDate(createDate);
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateDate() {
		if(updateDate == null)
			return new Date();
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
