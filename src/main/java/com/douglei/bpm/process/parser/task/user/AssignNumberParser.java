package com.douglei.bpm.process.parser.task.user;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignNumber;
import com.douglei.tools.utils.datatype.VerifyTypeMatchUtil;

/**
 * 
 * @author DougLei
 */
@Bean
public class AssignNumberParser {
	
	/**
	 * 解析指派人数
	 * @param value
	 * @return 返回null表示value不合法
	 */
	public AssignNumber parse(String value) {
		if(value.charAt(0) == '-')
			return null;
		
		int percentSignIndex = 0; // 百分号的下标
		while(percentSignIndex < value.length()) {
			if(value.charAt(percentSignIndex) == '%') 
				break;
			percentSignIndex++;
		}
		
		if(percentSignIndex == 0)
			return null;
		
		String str = value.substring(0, percentSignIndex);
		if(!VerifyTypeMatchUtil.isInteger(str))
			return null;
		
		
		int number = Integer.parseInt(str);
		if(number > 0) {
			if(percentSignIndex == value.length()) // 证明没有%号 
				return new AssignNumber(number, false, false);
			
			if(number < 101 && (value.length()-percentSignIndex) < 3) { // 大于100%的属于不合法的值; 总长度-百分号下标, 最大不能超过2, 给+/-预留一位
				if(value.length() == percentSignIndex+1) // 没有配置+/-, 所以总长度=百分号下标+1
					return new AssignNumber(number, true, false); 
				
				char c = value.charAt(percentSignIndex+1); // 否则证明配置了+/-, 百分号下标+1取对应的字符进行处理
				if(c == '+')
					return new AssignNumber(number, true, true);
				if(c == '-')
					return new AssignNumber(number, true, false); 
			}
		}
		return null;
	}
}
