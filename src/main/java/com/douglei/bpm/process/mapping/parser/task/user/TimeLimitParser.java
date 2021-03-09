package com.douglei.bpm.process.mapping.parser.task.user;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.mapping.metadata.TimeLimit;
import com.douglei.bpm.process.mapping.metadata.TimeLimitType;
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
	 * @param id
	 * @param name
	 * @param timeLimit
	 * @return
	 * @throws ProcessParseException
	 */
	TimeLimit parse(String id, String name, String timeLimit) throws ProcessParseException{
		if(StringUtil.isEmpty(timeLimit))
			return null;
		
		TimeLimit tl = parse_(id, name, timeLimit);
		if(tl == null)
			throw new ProcessParseException("<userTask id="+id+" name="+name+">的timeLimit属性值["+timeLimit+"]不合法");
		return tl;
	}
	// 返回null表示配置的值不合法
	private TimeLimit parse_(String id, String name, String timeLimit) {
		TimeLimit tl = new TimeLimit(); 
		
		StringBuilder sb = new StringBuilder(timeLimit.length());
		char c;
		int index = 0, limitTimeValue;
		loop:
		while(index < timeLimit.length()) {
			c = timeLimit.charAt(index++);
			switch(c) {
				case 'n':
				case 'N':
					tl.setType(TimeLimitType.NATURAL);
					break loop;
				case 'w':
				case 'W':
					tl.setType(TimeLimitType.WORKING);
					break loop;
				case 's':
				case 'S':
					tl.setType(TimeLimitType.SWORKING);
					break loop;
					
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
					
				default:
					sb.append(c);
					break;
			}
		}
		if(sb.length() > 0 || index < timeLimit.length() || tl.getTime() == 0)
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
