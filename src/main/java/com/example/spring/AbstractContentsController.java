package com.example.spring;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring.beans.ExternalFolderHandler;
import com.example.spring.beans.ExternalPath;
import com.example.spring.exceptions.ExternalFileNotFoundException;

public abstract class AbstractContentsController<T extends ExternalPath> extends AbstractExternalFileController<T> {
	protected static final AntPathMatcher apm = new AntPathMatcher();
	
	protected abstract String getContentsTopDirectory(String path);
	
	@RequestMapping(value = {"/", "/index.html"}, method=RequestMethod.GET)
	public ModelAndView index(HttpServletRequest req) throws ExternalFileNotFoundException {
		String fullPath = (String) req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String) req.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String path = new File(getExternalPathPrefix(), apm.extractPathWithinPattern(bestMatchPattern, fullPath)).getPath();
		ExternalFolderHandler handler = getExternalFolderHandler();
		File template = new File(path, "index.ftl");
		File html = new File(path, "index.html");
		File htm = new File(path, "index.htm");
		if (handler.isExists(template.getPath())) {
			return new ModelAndView(String.format("%s/index", getExternalPathPrefix()), "value", getModel(html.getPath()));
		} else if (handler.isExists(html.getPath())) {
			return new ModelAndView(getTemplate(html.getPath()), "value", getModel(html.getPath()));
		} else if (handler.isExists(htm.getPath())) {
			return new ModelAndView(getTemplate(htm.getPath()), "value", getModel(htm.getPath()));			
		}
		
		logger().info("not supported yet");
		throw new ExternalFileNotFoundException(path);
	}
}
