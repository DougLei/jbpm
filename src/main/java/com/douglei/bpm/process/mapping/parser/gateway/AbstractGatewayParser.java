package com.douglei.bpm.process.mapping.parser.gateway;

import org.dom4j.Element;

import com.douglei.bpm.module.runtime.variable.Scope;
import com.douglei.bpm.process.mapping.metadata.gateway.AbstractGatewayMetadata;
import com.douglei.bpm.process.mapping.parser.GeneralParser;
import com.douglei.bpm.process.mapping.parser.Parser;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;
import com.douglei.bpm.process.mapping.parser.temporary.data.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
public abstract class AbstractGatewayParser extends GeneralParser implements Parser<TaskTemporaryData, AbstractGatewayMetadata>{

	@Override
	public AbstractGatewayMetadata parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		Element element = temporaryData.getElement();
		AbstractGatewayMetadata metadata = createGatewayMetadata(
				temporaryData.getId(), 
				element.attributeValue("name"), 
				element.attributeValue("defaultOutputFlow"),  
				parseVariableExtend(element.element("variableExtend")),
				element);
		
		addListener(metadata, element.element("listeners"));
		return metadata;
	}

	// 解析流程变量继承配置, 返回不继承的流程变量范围权值和
	private int parseVariableExtend(Element element) {
		if(element == null)
			return 4;
		
		int unextendScopeWeight = 0;
		String value = element.attributeValue("global");
		if(value != null && "false".equalsIgnoreCase(value))
			unextendScopeWeight += Scope.GLOBAL.getWeight();
		
		value = element.attributeValue("local");
		if(value != null || "false".equalsIgnoreCase(value))
			unextendScopeWeight += Scope.LOCAL.getWeight();
		
		value = element.attributeValue("transient");
		if(value != null || "false".equalsIgnoreCase(value))
			unextendScopeWeight += Scope.TRANSIENT.getWeight();
		
		return unextendScopeWeight;
	}
	
	// 创建网关元数据实例
	protected abstract AbstractGatewayMetadata createGatewayMetadata(String id, String name, String defaultOutputFlowId, int unextendScopeWeight, Element element);
}