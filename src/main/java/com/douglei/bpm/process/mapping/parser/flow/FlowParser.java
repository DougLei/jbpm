package com.douglei.bpm.process.mapping.parser.flow;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.mapping.metadata.flow.FlowMetadata;
import com.douglei.bpm.process.mapping.parser.GeneralParser;
import com.douglei.bpm.process.mapping.parser.Parser;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;
import com.douglei.bpm.process.mapping.parser.temporary.data.FlowTemporaryData;
import com.douglei.tools.datatype.DataTypeValidateUtil;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class FlowParser extends GeneralParser implements Parser<FlowTemporaryData, FlowMetadata> {

	@Override
	public FlowMetadata parse(FlowTemporaryData temporaryData) throws ProcessParseException {
		Element element = temporaryData.getElement();
		
		int order = 0;
		String orderValue = element.attributeValue("order");
		if(DataTypeValidateUtil.isInteger(orderValue))
			order = Integer.parseInt(orderValue);
		
		FlowMetadata metadata = new FlowMetadata(
				temporaryData.getId(), 
				element.attributeValue("name"), 
				temporaryData.getSource(), 
				temporaryData.getTarget(), 
				order, 
				parseConditionExpression(element.element("conditionExpression")));
		
		addListener(metadata, element.element("listeners"));
		return metadata;
	}
	
	@Override
	public Type getType() {
		return Type.FLOW;
	}
}
