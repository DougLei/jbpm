package com.douglei.bpm.module.history.instance;

import java.util.Arrays;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.runtime.instance.ProcessInstanceHandlePolicy;
import com.douglei.orm.context.SessionContext;

/**
 * 历史实例服务
 * @author DougLei
 */
@Bean
public class HistoryProcessInstanceService {
	
	/**
	 * 判断指定的流程定义, 是否存在历史实例
	 * @param processDefinitionId
	 * @return
	 */
	public boolean exists(int processDefinitionId) {
		return Integer.parseInt(
				SessionContext.getSqlSession().uniqueQuery_("select count(id) from bpm_hi_procinst where procdef_id=?", Arrays.asList(processDefinitionId))[0].toString()) > 0;
	}
	
	/**
	 * 处理指定的流程定义, 相关的所有历史实例
	 * @param processDefinitionId
	 * @param policy 对实例的处理策略
	 */
	public void handle(int processDefinitionId, ProcessInstanceHandlePolicy policy) {
		// TODO 处理指定id的流程定义, 相关的所有运行实例
	}
}
