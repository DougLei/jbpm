package com.douglei.bpm.module.repository.type;

import java.util.Arrays;

import com.douglei.bpm.annotation.ProcessEngineTransactionBean;
import com.douglei.bpm.module.Service;
import com.douglei.bpm.module.Validator;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;
import com.douglei.orm.core.metadata.validator.ValidationResult;

/**
 * 流程类型服务
 * @author DougLei
 */
@ProcessEngineTransactionBean
public class ProcessTypeService extends Service{
	
	/**
	 * 保存类型
	 * @param type
	 * @return
	 */
	@Transaction
	public ValidationResult save(ProcessType type) {
		ValidationResult result = validate(type, codeValidator);
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
	public ValidationResult edit(ProcessType type) {
		ValidationResult result = validate(type, codeValidator);
		if(result == null)
			SessionContext.getTableSession().update(type);
		return result;
	}
	
	/**
	 * 删除类型
	 * @param type
	 * @return
	 */
	@Transaction
	public ValidationResult delete(ProcessType type) {
		ValidationResult result = validate(type, refProcessValidator);
		if(result == null)
			SessionContext.getTableSession().delete(type);
		return result;
	}
	
	/**
	 * code验证器, 验证code是否重复
	 */
	private Validator<ProcessType> codeValidator = new Validator<ProcessType>() {
		@Override
		public ValidationResult validate(ProcessType type) {
			Object[] oa = SessionContext.getSqlSession().uniqueQuery_("select id from bpm_re_proctype where code = ?", Arrays.asList(type.getCode()));
			if(oa != null && Integer.parseInt(oa[0].toString()) != type.getId()) 
				return new ValidationResult("code") {
					@Override
					public String getOriginMessage() {
						return "流程类型的编码值[%s]已存在";
					}
					@Override
					public String getCode() {
						return "bpm.process.type.code.exists";
					}
					@Override
					public Object[] getParams() {
						return new Object[] {type.getCode()};
					}
				};
			return null;
		}
	};
	
	/**
	 * 关联的流程验证器, 验证指定的类型, 是否有关联流程
	 */
	private Validator<ProcessType> refProcessValidator = new Validator<ProcessType>() {
		@Override
		public ValidationResult validate(ProcessType type) {
			Object[] oa = SessionContext.getSqlSession().uniqueQuery_("select count(id) from bpm_re_procdef where ref_type_id = ?", Arrays.asList(type.getId()));
			int refProcessCount = Integer.parseInt(oa[0].toString());
			if(refProcessCount > 0)
				return new ValidationResult(null) {
					@Override
					public String getOriginMessage() {
						return "该类型下关联了[%d]条流程, 无法删除";
					}
					@Override
					public String getCode() {
						return "bpm.process.type.delete.fail";
					}
					@Override
					public Object[] getParams() {
						return new Object[] {refProcessCount};
					}
				};
			return null;
		}
	};
}
