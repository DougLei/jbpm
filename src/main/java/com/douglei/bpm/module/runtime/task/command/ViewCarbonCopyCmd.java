package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.HistoryCarbonCopy;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.orm.context.SessionContext;

/**
 * 查看抄送
 * @author DougLei
 */
public class ViewCarbonCopyCmd implements Command {
	private String taskinstId;
	private String userId; // 查看抄送的用户id
	
	public ViewCarbonCopyCmd(String taskinstId, String userId) {
		this.taskinstId = taskinstId;
		this.userId = userId;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		List<HistoryCarbonCopy> list = SessionContext.getSqlSession().query(
				HistoryCarbonCopy.class, 
				"select * from bpm_ru_cc where taskinst_id=? and user_id=?", 
				Arrays.asList(taskinstId, userId));
		if(list.isEmpty())
			throw new TaskHandleException("不存在可以查看的抄送信息");
		
		// 从运行表删除
		SessionContext.getSQLSession().executeUpdate("CarbonCopy", "deleteByIds", list);
		
		// 添加到历史表
		Date viewTime = new Date();
		list.forEach(cc -> cc.setViewTime(viewTime));
		SessionContext.getTableSession().save(list);
		
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
