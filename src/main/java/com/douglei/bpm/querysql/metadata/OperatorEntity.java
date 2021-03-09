package com.douglei.bpm.querysql.metadata;

import java.io.Serializable;

/**
 * 
 * @author DougLei
 */
public class OperatorEntity implements Serializable{
	private Operator operator;
	private int times;
	
	public OperatorEntity(Operator operator, int times) {
		this.operator = operator;
		this.times = times;
	}
	
	@Override
	public boolean equals(Object obj) {
		return operator == ((OperatorEntity)obj).operator;
	}

	public Operator getOperator() {
		return operator;
	}
	public int getTimes() {
		return times;
	}
}
