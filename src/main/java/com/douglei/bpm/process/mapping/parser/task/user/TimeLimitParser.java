package com.douglei.bpm.process.mapping.parser.task.user;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.mapping.metadata.task.user.TimeLimit;
import com.douglei.bpm.process.mapping.metadata.task.user.TimeLimitType;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;
import com.douglei.tools.StringUtil;
import com.douglei.tools.datatype.DataTypeValidateUtil;

/**
 * 
 * @author DougLei
 */
@Bean
public class TimeLimitParser {
	
	/**
	 * 解析时限配置
	 * @param metadata
	 * @param timeLimit
	 * @return
	 * @throws ProcessParseException
	 */
	TimeLimit parse(UserTaskMetadata metadata, String timeLimit) throws ProcessParseException{
		if(StringUtil.isEmpty(timeLimit))
			return null;
		
		TimeLimit tl = parse_(timeLimit);
		if(tl == null)
			throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+">的timeLimit属性值["+timeLimit+"]不合法");
		return tl;
	}
	// 返回null表示配置的值不合法
	private TimeLimit parse_(String timeLimit) {
		TimeLimit tl = new TimeLimit(); 
		
		// 纯数字, 使用默认格式: dn(天/自然日)
		if(DataTypeValidateUtil.isInteger(timeLimit)) {
			tl.addDays(Integer.parseInt(timeLimit));
			tl.setType(TimeLimitType.NATURAL);
			return tl;
		}
		
		// 非纯数字, 按照标准格式去解析
		StringBuilder sb = new StringBuilder(timeLimit.length());
		char c;
		int index = 0, limitTimeValue;
		loop:
		while(index < timeLimit.length()) {
			c = timeLimit.charAt(index++);
			switch(c) {
				case 'd': // 天
				case 'D':
					limitTimeValue = getLimitTime(sb);
					if(limitTimeValue == -1)
						return null;
					tl.addDays(limitTimeValue);
					break;
				case 'h': // 小时
				case 'H':
					limitTimeValue = getLimitTime(sb);
					if(limitTimeValue == -1)
						return null;
					tl.addHours(limitTimeValue);
					break;
				case 'm': // 分钟
				case 'M':
					limitTimeValue = getLimitTime(sb);
					if(limitTimeValue == -1)
						return null;
					tl.addMinutes(limitTimeValue);
					break;
					
				case 'n': // 自然日
				case 'N':
					tl.setType(TimeLimitType.NATURAL);
					break loop;
				case 'w': // 工作日
				case 'W':
					tl.setType(TimeLimitType.WORKING);
					break loop;
				case 's': // 智能工作日
				case 'S':
					tl.setType(TimeLimitType.SWORKING);
					break loop;
					
				default:
					sb.append(c);
					break;
			}
		}
		if(sb.length() > 0 || index < timeLimit.length() || tl.getTimes() == 0)
			return null;
		return tl;
	}
	// 获取配置的限制时间数值, 返回-1表示数值不合法
	private int getLimitTime(StringBuilder sb) {
		if(sb.length() > 0 && sb.charAt(0) != '-') {
			String str = sb.toString();
			if(DataTypeValidateUtil.isInteger(str)) {
				int limitTime = Integer.parseInt(str);
				if(limitTime > 0) {
					sb.setLength(0);
					return limitTime;
				}
			}
		}
		return -1;
	}
}
