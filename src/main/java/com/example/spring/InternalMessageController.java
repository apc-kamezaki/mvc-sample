package com.example.spring;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring.beans.Message;

@Controller
@RequestMapping(value = "/internal")
public class InternalMessageController {
	private static final Logger logger = LoggerFactory.getLogger(InternalMessageController.class);
	private static final AntPathMatcher apm = new AntPathMatcher();

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView message() {
		logger.info("Welcome internal");
		return new ModelAndView("basic", "message", new Message());
	}
	
	@RequestMapping(value = "/include", method = RequestMethod.GET)
	public ModelAndView include() {
		logger.info("Internal include");
		return new ModelAndView("/include", "path", "/external/external-static.html");
	}
	
	@RequestMapping(value = "/**/{file:.+\\.html?}", method = RequestMethod.GET)
	public ModelAndView staticHtml(HttpServletRequest req) {
		String fullPath = (String) req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String) req.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String path = apm.extractPathWithinPattern(bestMatchPattern, fullPath);
		path = path.startsWith("/") ? path : "/" + path;
		logger.info("Internal static html " + path);
		return new ModelAndView("/include", "path", path);
	}

	@RequestMapping(value = "/**/{file:(?!(?:.+\\.html?)$).+$}", method = RequestMethod.GET)
	public ModelAndView staticFile(HttpServletRequest req) {
		String fullPath = (String) req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String) req.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String path = apm.extractPathWithinPattern(bestMatchPattern, fullPath);
		path = path.startsWith("/") ? path : "/" + path;
		logger.info("Internal file " + path);
		return new ModelAndView("/include", "path", "/external/external-static.html");
	}
}
