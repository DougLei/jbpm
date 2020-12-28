package com.douglei.bpm.process.metadata;

import java.util.Comparator;

import com.douglei.bpm.process.metadata.flow.FlowMetadata;

/**
 * flow的排序比较器
 * @author DougLei
 */
public class FlowSortComparator implements Comparator<FlowMetadata>{
	private static final FlowSortComparator INSTANCE = new FlowSortComparator();
	public static FlowSortComparator getInstance() {
		return INSTANCE;
	}
	
	@Override
	public int compare(FlowMetadata f1, FlowMetadata f2) {
		if(f1.getOrder() == f2.getOrder()) {
			return 0;
		}else if(f1.getOrder() < f2.getOrder()) {
			return -1;
		}
		return 1;
	}
}
