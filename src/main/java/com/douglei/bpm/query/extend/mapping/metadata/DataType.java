package com.douglei.bpm.query.extend.mapping.metadata;

/**
 * 
 * @author DougLei
 */
public enum DataType {
	
	/**
	 * 字符串
	 */
	STRING {
		@Override
		public boolean support(Operator operator) {
			switch (operator) {
				case EQ:
				case NE:
					
				case LIKE:
				case NLIKE:
					
				case IN:
				case NIN:
					
				case ORDER:
					return true;
				default:
					return false;
			}
		}
	},
	
	/**
	 * 数字
	 */
	NUMBER,
	
	/**
	 * 日期
	 */
	DATETIME;
	
	
	/**
	 * 是否支持指定的操作
	 * @param operator
	 * @return
	 */
	public boolean support(Operator operator) {
		switch (operator) {
			case EQ:
			case NE:
			case GE:
			case GT:
			case LE:
			case LT:
				
			case BTN:
			case NBTN:
				
			case IN:
			case NIN:
				
			case ORDER:
				return true;
			default:
				return false;
		}
	}
}
