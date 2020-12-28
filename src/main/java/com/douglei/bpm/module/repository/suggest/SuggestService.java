package com.douglei.bpm.module.repository.suggest;

import java.util.Arrays;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

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
	public ExecutionResult insert(Suggest suggest) {
		SessionContext.getTableSession().save(suggest);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 修改意见
	 * @param suggest
	 * @return
	 */
	@Transaction
	public ExecutionResult update(Suggest suggest) {
		SessionContext.getTableSession().update(suggest);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 删除意见
	 * @param suggestId
	 * @return
	 */
	@Transaction
	public ExecutionResult delete(int suggestId) {
		SessionContext.getSqlSession().executeUpdate("delete bpm_re_suggest where id=?", Arrays.asList(suggestId));
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 删除指定userId的意见
	 * @param userId
	 * @return
	 */
	@Transaction
	public ExecutionResult deleteByUserId(String userId) {
		SessionContext.getSqlSession().executeUpdate("delete bpm_re_suggest where user_id=?", Arrays.asList(userId));
		return ExecutionResult.getDefaultSuccessInstance();
	}
	

	/**
	 *  删除指定tenantId的意见
	 * @param tenantId
	 * @return
	 */
	@Transaction
	public ExecutionResult deleteByTenantId(String tenantId) {
		SessionContext.getSqlSession().executeUpdate("delete bpm_re_suggest where tenant_id=?", Arrays.asList(tenantId));
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
