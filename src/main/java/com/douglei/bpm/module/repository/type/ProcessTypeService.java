package com.douglei.bpm.module.repository.type;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.annotation.ProcessEngineTransactionBean;
import com.douglei.bpm.module.common.Service;
import com.douglei.bpm.module.common.Validator;
import com.douglei.i18n.I18nContext;
import com.douglei.i18n.Message;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 流程类型服务
 * @author DougLei
 */
@ProcessEngineTransactionBean
public class ProcessTypeService extends Service{
	
	/**
	 * 保存类型
	 * @param type
	 */
	@Transaction
	public Message save(ProcessType type) {
		Message result = validate(type, codeValidator);
		if(result == null)
			SessionContext.getTableSession().save(type);
		return result;
	}
	
	/**
	 * 修改类型
	 * @param type
	 */
	@Transaction
	public Message edit(ProcessType type) {
		Message result = validate(type, codeValidator);
		if(result == null)
			SessionContext.getTableSession().update(type);
		return result;
	}
	
	/**
	 * 删除类型
	 * @param type
	 */
	@Transaction
	public Message delete(ProcessType type) {
		Message result = validate(type, refProcessValidator);
		if(result == null)
			SessionContext.getTableSession().delete(type);
		return result;
	}
	
	/**
	 * 查询
	 * @param type
	 */
	@Transaction
	public List<ProcessType> query(ProcessType condition) {
		
		return null;
	}
	
	/**
	 * 查询
	 * @param type
	 */
	@Transaction
	public ProcessType uniqueQuery(ProcessType condition) {
		
		return null;
	}
	
	
	/**
	 * code验证器, 验证code是否重复
	 */
	private Validator<ProcessType> codeValidator = new Validator<ProcessType>() {
		@Override
		public Message validate(ProcessType type) {
			Object[] oa = SessionContext.getSqlSession().uniqueQuery_("select id from bpm_re_proctype where code = ?", Arrays.asList(type.getCode()));
			if(oa != null && Integer.parseInt(oa[0].toString()) != type.getId()) 
				return I18nContext.getMessage("流程类型的编码值[%s]已存在", "bpm.process.type.code.exists", type.getCode());
			return null;
		}
	};
	
	/**
	 * 关联的流程验证器, 验证指定的类型, 是否有关联流程
	 */
	private Validator<ProcessType> refProcessValidator = new Validator<ProcessType>() {
		@Override
		public Message validate(ProcessType type) {
			Object[] oa = SessionContext.getSqlSession().uniqueQuery_("select count(id) from bpm_re_procdef where ref_type_id = ?", Arrays.asList(type.getId()));
			int refProcessCount = Integer.parseInt(oa[0].toString());
			if(refProcessCount > 0) 
				return I18nContext.getMessage("该类型下关联了[%d]条流程, 无法删除", "bpm.process.type.delete.fail", refProcessCount);
			return null;
		}
	};
}
