package com.douglei.bpm.core.process.parser.impl.flow;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.core.process.executer.flow.Flow;
import com.douglei.bpm.core.process.parser.FlowElement;
import com.douglei.bpm.core.process.parser.Parser;
import com.douglei.bpm.core.process.parser.ProcessParseException;
import com.douglei.tools.utils.StringUtil;
import com.douglei.tools.utils.datatype.VerifyTypeMatchUtil;

/**
 * 
 * @author DougLei
 */
@Bean(transaction = false)
public class FlowParser implements Parser<FlowElement, Flow> {

	@Override
	public String elementName() {
		return "flow";
	}

	@Override
	public Flow parse(FlowElement parameter) throws ProcessParseException {
		Element flowElement = parameter.getElement();
		String id = parseId(flowElement);
		String attributeValue;
		
		int order = 0;
		attributeValue = flowElement.attributeValue("order");
		if(VerifyTypeMatchUtil.isInteger(attributeValue))
			order = Integer.parseInt(attributeValue);
		
		String conditionExpr = flowElement.attributeValue("conditionExpr");
		if(StringUtil.isEmpty(conditionExpr))
			conditionExpr = null;
		
		return new Flow(id, flowElement.attributeValue("name"), order, conditionExpr);
	}
}
