package com.douglei.bpm.module.repository.type;

import java.util.Arrays;

import com.douglei.bpm.ProcessEngineTransactionBean;
import com.douglei.i18n.I18nContext;
import com.douglei.i18n.Message;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 流程类型服务
 * @author DougLei
 */
@ProcessEngineTransactionBean
public class ProcessTypeService {
	
	/**
	 * 验证流程类型的code是否已经存在
	 * @param type
	 * @return
	 */
	private Message validateCodeExists(ProcessTypeEntity type) {
		Object[] oa = SessionContext.getSqlSession().uniqueQuery_("select id from bpm_re_proctype where code = ?", Arrays.asList(type.getCode()));
		if(oa != null) {
			int id = Integer.parseInt(oa[0].toString());
			if(id != type.getId()) {
				return I18nContext.getMessage("流程类型的编码值[%s]已存在", "bpm.process.type.code.exists", type.getCode());
			}
		}
		return null;
	}
	
	/**
	 * 保存类型
	 * @param type
	 */
	@Transaction
	public Message save(ProcessTypeEntity type) {
		Message result = validateCodeExists(type);
		if(result == null)
			SessionContext.getTableSession().save(type);
		return result;
	}
}
