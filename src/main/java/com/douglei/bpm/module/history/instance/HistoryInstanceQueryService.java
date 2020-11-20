package com.douglei.bpm.module.history.instance;

import com.douglei.bpm.bean.Bean;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 历史实例查询服务
 * @author DougLei
 */
@Bean
public class HistoryInstanceQueryService {
	
	/**
	 * 判断指定id的流程定义, 是否存在历史实例
	 * @param processDefinitionId
	 * @return
	 */
	@Transaction
	public boolean exists(int processDefinitionId) {
		// TODO 判断指定的流程定义id, 是否存在历史实例
		return true;
	}
}
