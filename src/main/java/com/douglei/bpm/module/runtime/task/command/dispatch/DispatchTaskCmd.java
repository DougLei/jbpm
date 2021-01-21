package com.douglei.bpm.module.runtime.task.command.dispatch;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.ProcessEngineBugException;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.HistoryDispatch;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.bpm.module.runtime.task.command.parameter.DispatchTaskParameter;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.GeneralTaskHandler;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.metadata.TaskMetadataEntity;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.orm.context.SessionContext;
import com.douglei.tools.utils.StringUtil;

/**
 * 调度任务
 * @author DougLei
 */
public class DispatchTaskCmd extends GeneralTaskHandler implements Command {
	private TaskInstance taskInstance;
	private DispatchTaskParameter parameter;
	public DispatchTaskCmd(TaskInstance taskInstance, DispatchTaskParameter parameter) {
		this.taskInstance = taskInstance;
		this.parameter = parameter;
	}

	private GeneralHandleParameter handleParameter;
	
	@SuppressWarnings("unchecked")
	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(taskInstance.isAuto())
			throw new TaskHandleException("调度失败, ["+taskInstance.getName()+"]任务不支持用户调度");
		
		if(StringUtil.isEmpty(parameter.getUserId()))
			throw new TaskHandleException("调度失败, 调度["+taskInstance.getName()+"]任务, 需要提供userId");

		// 判断指定的userId能否调度当前任务
		int count = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_("select count(id) from bpm_ru_dispatch where taskinst_id=? and user_id=? and is_enabled=1", Arrays.asList(taskInstance.getTask().getTaskinstId(), parameter.getUserId()))[0].toString());
		if(count == 0)
			throw new TaskHandleException("调度失败, 指定的userId无权调度["+taskInstance.getName()+"]任务");
		if(count > 1)
			throw new ProcessEngineBugException("同一个任务的调度信息存在"+count+"条有效数据");
		
		
		// 创建调度用办理参数实例
		this.handleParameter = new GeneralHandleParameter(
				taskInstance, 
				processEngineBeans.getUserBeanFactory().create(parameter.getUserId()), 
				null, null, null, null, 
				processEngineBeans.getUserBeanFactory().create(parameter.getAssignUserIds()));
		
		// 完成用户任务
		finishUserTask();
		
		// 进行调度
		parameter.getDispatchExecutor()
			.setParameters((TaskMetadataEntity<UserTaskMetadata>)taskInstance.getTaskMetadataEntity(), handleParameter, processEngineBeans)
			.execute();
		return ExecutionResult.getDefaultSuccessInstance();
	}

	// 结束用户任务
	private void finishUserTask() {
		completeTask(taskInstance.getTask(), handleParameter.getCurrentDate());
		handleUserInfo();
		followTaskCompleted4Variable(taskInstance.getTask(), handleParameter.getVariableEntities());
	}
	
	// 处理相关的用户信息
	private void handleUserInfo() {
		// 移除不用的指派信息
		SessionContext.getSqlSession().executeUpdate(
				"delete bpm_ru_assignee where taskinst_id=?", 
				Arrays.asList(taskInstance.getTask().getTaskinstId()));
		
		// 转移调度信息(从运行表到历史表)
		List<HistoryDispatch> historyDispatchs = SessionContext.getTableSession().query(HistoryDispatch.class, "select * from bpm_ru_dispatch where taskinst_id=? order by is_enabled desc", Arrays.asList(taskInstance.getTask().getTaskinstId()));
		historyDispatchs.get(0).setDispatchTime(handleParameter.getCurrentDate());
		SessionContext.getTableSession().save(historyDispatchs);
		SessionContext.getSqlSession().executeUpdate("delete from bpm_ru_dispatch where taskinst_id=?", Arrays.asList(taskInstance.getTask().getTaskinstId()));
	}
}
