package com.douglei.bpm.module.runtime.instance;

import java.util.Arrays;

import com.douglei.bpm.bean.Bean;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 运行实例查询服务
 * @author DougLei
 */
@Bean
public class RuntimeInstanceQueryService {
	
	/**
	 * 判断指定id的流程定义, 是否存在运行实例
	 * @param processDefinitionId
	 * @return
	 */
	@Transaction
	public boolean exists(int processDefinitionId) {
		return Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_("select count(id) from bpm_ru_procinst where procdef_id=?", Arrays.asList(processDefinitionId))[0].toString()) > 0;
	}
}
