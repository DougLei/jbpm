package com.douglei.bpm.process.parser.flow;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.flow.FlowMetadata;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.tmp.data.FlowTemporaryData;
import com.douglei.tools.utils.StringUtil;
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
		
		// TODO 具体的条件写法还有待调整
		String conditionExpr = null;
		Element conditionExprElement = element.element("conditionExpr");
		if(conditionExprElement != null) {
			String tmp = conditionExprElement.getTextTrim();
			if(StringUtil.notEmpty(tmp))
				conditionExpr = tmp.substring(2, tmp.length()-1);
		}
		return new FlowMetadata(
				temporaryData.getId(), 
				element.attributeValue("name"), 
				temporaryData.getSource(), 
				temporaryData.getTarget(), 
				order, 
				conditionExpr);
	}

	@Override
	public Type getType() {
		return Type.FLOW;
	}
}
