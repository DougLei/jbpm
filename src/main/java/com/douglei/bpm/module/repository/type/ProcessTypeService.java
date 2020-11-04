package com.douglei.bpm.module.repository.type;

import java.util.Arrays;
import java.util.Date;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.common.service.ExecutionResult;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 流程类型服务
 * @author DougLei
 */
@Bean
public class ProcessTypeService {
	
	// 根据流程类型code, 获取对应的id
	private Object[] getIdByCode(String code) {
		return SessionContext.getSqlSession().uniqueQuery_("select id from bpm_re_proctype where code = ?", Arrays.asList(code));
	}
	
	/**
	 * 保存类型
	 * @param type
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult save(ProcessType type) {
		if(getIdByCode(type.getCode()) != null)
			return new ExecutionResult("code", "已存在编码为[%s]的流程类型", "bpm.process.type.code.exists", type.getCode());
		
		SessionContext.getTableSession().save(type);
		return null;
	}
	
	/**
	 * 修改类型
	 * @param type
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult update(ProcessType type) {
		Object[] obj = getIdByCode(type.getCode());
		if(obj != null && type.getId() != Integer.parseInt(obj[0].toString())) 
			return new ExecutionResult("code", "已存在编码为[%s]的流程类型", "bpm.process.type.code.exists", type.getCode());
		
		SessionContext.getTableSession().update(type);
		return null;
	}
	
	/**
	 * 删除类型
	 * @param type
	 * @param strict 是否进行强制删除; 强制删除时, 如果被删除的类型下存在流程定义, 则将这些流程定义的类型值改为0(默认类型)
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult delete(ProcessType type, boolean strict) {
		int count = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_("select count(id) from bpm_re_procdef where ref_type_id = ?", Arrays.asList(type.getId()))[0].toString());
		if(count > 0 && !strict)
			return new ExecutionResult(null, "该流程类型关联了[%d]条流程, 无法删除", "bpm.process.type.delete.fail", count);

		SessionContext.getTableSession().delete(type);
		if(count > 0) 
			SessionContext.getSqlSession().executeUpdate("update bpm_re_procdef set ref_type_id=0 where ref_type_id=?", Arrays.asList(type.getId()));
		return null;
	}
}
