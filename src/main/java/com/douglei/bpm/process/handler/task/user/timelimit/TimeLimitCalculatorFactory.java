package com.douglei.bpm.process.handler.task.user.timelimit;

import com.douglei.bpm.ProcessEngineBugException;
import com.douglei.bpm.process.handler.task.user.timelimit.impl.NaturalTimeLimitCalculator;
import com.douglei.bpm.process.handler.task.user.timelimit.impl.SWorkingTimeLimitCalculator;
import com.douglei.bpm.process.handler.task.user.timelimit.impl.WorkingTimeLimitCalculator;
import com.douglei.bpm.process.mapping.metadata.task.user.TimeLimitType;

/**
 * 
 * @author DougLei
 */
public class TimeLimitCalculatorFactory {
	
	/**
	 * 构建时限计算器实例
	 * @param type
	 * @return
	 */
	public static TimeLimitCalculator build(TimeLimitType type) {
		switch(type) {
			case NATURAL: 
				return new NaturalTimeLimitCalculator();
			case WORKING: 
				return new WorkingTimeLimitCalculator();
			case SWORKING: 
				return new SWorkingTimeLimitCalculator();
		}
		throw new ProcessEngineBugException();
	}
}
