package com.douglei.bpm.core.process.parser;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.douglei.bpm.core.process.executer.Node;
import com.douglei.tools.instances.scanner.ClassScanner;
import com.douglei.tools.utils.reflect.ClassLoadUtil;
import com.douglei.tools.utils.reflect.ConstructorUtil;
import com.douglei.tools.utils.reflect.ValidationUtil;

/**
 * 
 * @author DougLei
 */
class ParserContainer {
	private Map<String, Parser<Element, ? extends Node>> parsers;
	
	public ParserContainer() {
		parsers = new HashMap<String, Parser<Element,? extends Node>>();
		
		ClassScanner scanner = new ClassScanner();
		List<String> classPaths = scanner.scan(ParserContainer.class.getPackage().getName() + ".impl");
		
		
		Class<?> clz;
		Parser<Element, ? extends Node> parser;
		for (String cp : classPaths) {
			clz = ClassLoadUtil.loadClass(cp);
			if(Modifier.isAbstract(clz.getModifiers()) || !ValidationUtil.isExtendClass(clz, ActionParser.class)) {
				continue;
			}
			actionResolver= (ActionParser) ConstructorUtil.newInstance(clz);
			MAP.put(actionResolver.getType().toUpperCase(), actionResolver);
		}
		
		
		scanner.destroy();
	}
}
