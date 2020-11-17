package com.douglei.bpm.process.parser.flow;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.executor.flow.Flow;
import com.douglei.bpm.process.executor.flow.FlowMode;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.tools.utils.datatype.VerifyTypeMatchUtil;

/**
 * 
 * @author DougLei
 */
@Bean(isTransaction = false)
public class FlowParser implements Parser<FlowMetadata, Flow> {

	@Override
	public String elementName() {
		return "flow";
	}

	@Override
	public Flow parse(FlowMetadata flowMetadata) throws ProcessParseException {
		Element element = flowMetadata.getElement();
		
		int order = 0;
		String orderValue = element.attributeValue("order");
		if(VerifyTypeMatchUtil.isInteger(orderValue))
			order = Integer.parseInt(orderValue);
		
		return new Flow(flowMetadata.getId(), element.attributeValue("name"), order, FlowMode.toValue(element.attributeValue("mode")), element.attributeValue("conditionExpr"));
	}
}
