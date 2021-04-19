package com.douglei.bpm.module.repository.suggest;

import java.util.Arrays;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.Result;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;

/**
 * 
 * @author DougLei
 */
@Bean(isTransaction=true)
public class SuggestService {
	
	/**
	 * 保存意见
	 * @param suggest
	 * @return
	 */
	@Transaction
	public Result insert(Suggest suggest) {
		SessionContext.getTableSession().save(suggest);
		return Result.getDefaultSuccessInstance();
	}
	
	/**
	 * 修改意见
	 * @param suggest
	 * @return
	 */
	@Transaction
	public Result update(Suggest suggest) {
		SessionContext.getTableSession().update(suggest);
		return Result.getDefaultSuccessInstance();
	}
	
	/**
	 * 删除意见
	 * @param suggestId
	 * @return
	 */
	@Transaction
	public Result delete(int suggestId) {
		SessionContext.getSqlSession().executeUpdate("delete bpm_re_suggest where id=?", Arrays.asList(suggestId));
		return Result.getDefaultSuccessInstance();
	}
	
	/**
	 * 删除指定userId的意见
	 * @param userId
	 * @param tenantId 租户id, 可为null
	 * @return
	 */
	@Transaction
	public Result deleteByUserId(String userId, String tenantId) {
		if(tenantId == null)
			SessionContext.getSqlSession().executeUpdate("delete bpm_re_suggest where user_id=?", Arrays.asList(userId));
		else
			SessionContext.getSqlSession().executeUpdate("delete bpm_re_suggest where user_id=? and tenant_id=?", Arrays.asList(userId, tenantId));
		
		return Result.getDefaultSuccessInstance();
	}
	

	/**
	 *  删除指定tenantId的意见
	 * @param tenantId
	 * @return
	 */
	@Transaction
	public Result deleteByTenantId(String tenantId) {
		SessionContext.getSqlSession().executeUpdate("delete bpm_re_suggest where tenant_id=?", Arrays.asList(tenantId));
		return Result.getDefaultSuccessInstance();
	}
}
