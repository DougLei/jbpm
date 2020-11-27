package com.douglei.bpm.process.parser.flow;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.flow.FlowMetadata;
import com.douglei.bpm.process.metadata.node.flow.FlowMode;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.tools.utils.datatype.VerifyTypeMatchUtil;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class FlowParser implements Parser<FlowTemporaryData, FlowMetadata> {

	@Override
	public FlowMetadata parse(FlowTemporaryData temporaryData) throws ProcessParseException {
		Element element = temporaryData.getElement();
		
		int order = 0;
		String orderValue = element.attributeValue("order");
		if(VerifyTypeMatchUtil.isInteger(orderValue))
			order = Integer.parseInt(orderValue);
		
		return new FlowMetadata(temporaryData.getId(), element.attributeValue("name"), getNodeType(), order, FlowMode.toValue(element.attributeValue("mode")), element.attributeValue("conditionExpr"));
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.FLOW;
	}
}
