package com.douglei.bpm.module.repository.type;

import java.util.Arrays;

import com.douglei.bpm.bean.annotation.ProcessEngineBean;
import com.douglei.bpm.module.common.service.ExecutionResult;
import com.douglei.bpm.module.common.service.Service;
import com.douglei.bpm.module.common.service.Validator;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 流程类型服务
 * @author DougLei
 */
@ProcessEngineBean
public class ProcessTypeService extends Service{
	// code验证器, 验证code是否重复
	private Validator<ProcessType> codeValidator = new Validator<ProcessType>() { 
		@Override
		public ExecutionResult<ProcessType> validate(ProcessType type) {
			Object[] oa = SessionContext.getSqlSession().uniqueQuery_("select id from bpm_re_proctype where code = ?", Arrays.asList(type.getCode()));
			if(oa != null && Integer.parseInt(oa[0].toString()) != type.getId())
				return new ExecutionResult<ProcessType>("code", "流程类型的编码值[%s]已存在", "bpm.process.type.code.exists", type.getCode());
			return null;
		}
	};
	// 关联的流程验证器, 验证指定的类型, 是否有关联流程
	private Validator<ProcessType> refProcessValidator = new Validator<ProcessType>() { 
		@Override
		public ExecutionResult<ProcessType> validate(ProcessType type) {
			Object[] oa = SessionContext.getSqlSession().uniqueQuery_("select count(id) from bpm_re_procdef where ref_type_id = ?", Arrays.asList(type.getId()));
			int refProcessCount = Integer.parseInt(oa[0].toString());
			if(refProcessCount > 0)
				return new ExecutionResult<ProcessType>(null, "该类型下关联了[%d]条流程, 无法删除", "bpm.process.type.delete.fail", refProcessCount);
			return null;
		}
	};
	
	/**
	 * 保存类型
	 * @param type
	 * @return
	 */
	@Transaction
	public ExecutionResult<Object> save(ProcessType type) {
		ExecutionResult<Object> result = validate(type, codeValidator);
		if(result == null)
			SessionContext.getTableSession().save(type);
		return result;
	}
	
	/**
	 * 修改类型
	 * @param type
	 * @return
	 */
	@Transaction
	public ExecutionResult<Object> edit(ProcessType type) {
		ExecutionResult<Object> result = validate(type, codeValidator);
		if(result == null)
			SessionContext.getTableSession().update(type);
		return result;
	}
	
	/**
	 * 删除类型
	 * @param type
	 * @param strict 是否进行强制删除; 强制删除时, 如果被删除的类型下存在流程定义, 则将这些流程定义的类型值改为0(默认类型)
	 * @return
	 */
	@Transaction
	public ExecutionResult<Object> delete(ProcessType type, boolean strict) {
		ExecutionResult<Object> result = validate(type, refProcessValidator);
		if(result != null && !strict)
			return result;

		SessionContext.getTableSession().delete(type);
		if(result != null)
			SessionContext.getSqlSession().executeUpdate("update bpm_re_procdef set ref_type_id=0 where ref_type_id=?", Arrays.asList(type.getId()));
		return result;
	}
}
