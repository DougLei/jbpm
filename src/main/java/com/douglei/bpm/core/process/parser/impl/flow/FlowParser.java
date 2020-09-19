package com.douglei.bpm.core.process.parser.impl.flow;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.core.process.executer.flow.Flow;
import com.douglei.bpm.core.process.parser.Parser;
import com.douglei.bpm.core.process.parser.ProcessParseException;
import com.douglei.bpm.core.process.parser.element.FlowElement;
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
	public Flow parse(FlowElement flowElement) throws ProcessParseException {
		Element element = flowElement.getElement();
		String attributeValue;
		
		int order = 0;
		attributeValue = element.attributeValue("order");
		if(VerifyTypeMatchUtil.isInteger(attributeValue))
			order = Integer.parseInt(attributeValue);
		
		String conditionExpr = element.attributeValue("conditionExpr");
		if(StringUtil.isEmpty(conditionExpr))
			conditionExpr = null;
		
		return new Flow(flowElement.getId(), flowElement.getName(), order, conditionExpr);
	}
}
