package com.douglei.bpm.process.api.user.option.impl.dispatch;

import org.dom4j.Element;

import com.douglei.bpm.process.api.user.option.AbstractOptionParser;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.option.Option;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;
import com.douglei.tools.datatype.DataTypeValidateUtil;

/**
 * 
 * @author DougLei
 */
public abstract class AbstractDispatchOptionParser extends AbstractOptionParser{
	
	@Override
	public boolean supportMultiple() {
		return true;
	}

	@Override
	public Option parse(String name, int order, UserTaskMetadata metadata, Element element) throws ProcessParseException {
		Element parameterElement = element.element("parameter");
		if(parameterElement == null) 
			throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+">"+getXmlStruct()+"下必须配置<parameter>");
		
		// 解析suggest
		String suggest = parameterElement.attributeValue("suggest");
		boolean suggestIsRequired = DataTypeValidateUtil.isBoolean(suggest)?
				"true".equalsIgnoreCase(suggest):metadata.getCandidate().getHandlePolicy().suggestIsRequired();
		
		// 解析attitude
		String attitude = parameterElement.attributeValue("attitude");
		boolean attitudeIsRequired = DataTypeValidateUtil.isBoolean(attitude)?
				"true".equalsIgnoreCase(attitude):metadata.getCandidate().getHandlePolicy().attitudeIsRequired();
		
		return parse(name, order, suggestIsRequired, attitudeIsRequired, metadata, parameterElement, element);
	}

	/**
	 * 解析调度类Option
	 * @param name
	 * @param order
	 * @param suggestIsRequired
	 * @param attitudeIsRequired
	 * @param metadata
	 * @param parameterElement
	 * @param element
	 * @return
	 */
	protected abstract Option parse(String name, int order, boolean suggestIsRequired, boolean attitudeIsRequired, UserTaskMetadata metadata, Element parameterElement, Element element);
}
