package com.douglei.bpm.core.process.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.bean.annotation.ParserBean;
import com.douglei.bpm.core.process.executer.task.Task;
import com.douglei.bpm.core.process.parser.impl.flow.FlowParser;
import com.douglei.bpm.core.process.parser.impl.task.event.StartEventParser;
import com.douglei.tools.instances.scanner.ClassScanner;
import com.douglei.tools.utils.reflect.ClassLoadUtil;
import com.douglei.tools.utils.reflect.ConstructorUtil;

/**
 * 解析器容器
 * 这里不包含{@link StartEventParser}和{@link FlowParser}两个解析器, 它们在中{@link ProcessParser}独立使用
 * @author DougLei
 */
@Bean(transaction = false)
public class ParserContainer {
	private Map<String, Parser<Element, ? extends Task>> parserMap = new HashMap<String, Parser<Element,? extends Task>>();
	
	@SuppressWarnings({ "rawtypes", "unchecked" } )
	public ParserContainer() {
		List<String> classes = new ClassScanner().scan(ParserContainer.class.getPackage().getName() + ".impl");
		Parser parser;
		for (String clazz : classes) {
			if(ClassLoadUtil.loadClass(clazz).getAnnotation(ParserBean.class) != null) {
				parser = (Parser) ConstructorUtil.newInstance(clazz);
				parserMap.put(parser.elementName(), parser);
			}
		}
	}
	
	/**
	 * 解析
	 * @param element
	 * @return
	 */
	public Task parse(Element element) {
		Parser<Element, ? extends Task> parser = parserMap.get(element.getName());
		if(parser == null)
			throw new ProcessParseException("无法解析<"+element.getName()+">元素");
		return parser.parse(element);
	}
}
