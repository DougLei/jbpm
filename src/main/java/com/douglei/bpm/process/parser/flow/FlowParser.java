package com.douglei.bpm.process.parser.flow;

import org.dom4j.Element;

import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.process.node.flow.Flow;
import com.douglei.bpm.process.node.flow.FlowMode;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.tools.utils.datatype.VerifyTypeMatchUtil;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class FlowParser implements Parser<FlowTemporaryData, Flow> {

	@Override
	public String elementName() {
		return "flow";
	}

	@Override
	public Flow parse(FlowTemporaryData temporaryData) throws ProcessParseException {
		Element element = temporaryData.getElement();
		
		int order = 0;
		String orderValue = element.attributeValue("order");
		if(VerifyTypeMatchUtil.isInteger(orderValue))
			order = Integer.parseInt(orderValue);
		
		return new Flow(temporaryData.getId(), element.attributeValue("name"), order, FlowMode.toValue(element.attributeValue("mode")), element.attributeValue("conditionExpr"));
	}
}
