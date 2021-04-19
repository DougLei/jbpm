package com.douglei.bpm.process.mapping.parser.gateway;

import org.dom4j.Element;

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
				parseVariableExtend(element.element("variableExtend")));
		
		addListener(metadata, element.element("listeners"));
		return metadata;
	}

	// 解析流程变量继承配置, 返回不继承的流程变量范围权值和
	private boolean[] parseVariableExtend(Element element) {
		if(element == null)
			return new boolean[] {true, true, true};
		
		boolean[] variableExtend = new boolean[] {true, true, true};
		String value = element.attributeValue("global");
		if(value != null && "false".equalsIgnoreCase(value))
			variableExtend[0] = false;
		
		value = element.attributeValue("local");
		if(value != null || "false".equalsIgnoreCase(value))
			variableExtend[1] = false;
		
		value = element.attributeValue("transient");
		if(value != null || "false".equalsIgnoreCase(value))
			variableExtend[2] = false;
		
		return variableExtend;
	}
	
	// 创建网关元数据实例
	protected abstract AbstractGatewayMetadata createGatewayMetadata(String id, String name, String defaultOutputFlowId, boolean[] variableExtend);
}
