package com.example.spring;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring.beans.ExternalFolderHandler;
import com.example.spring.beans.ExternalPath;
import com.example.spring.exceptions.ExternalFileNotFoundException;

public abstract class AbstractExternalFileController<T extends ExternalPath> {
	private final Logger logger;
	private static final AntPathMatcher apm = new AntPathMatcher();
	
	// TODO auto-wire by spring-bean
	private ExternalFolderHandler folderHandler = new ExternalFolderHandler();

	public AbstractExternalFileController() {
		super();
		logger = LoggerFactory.getLogger(this.getClass());
	}
	
	protected Logger logger() {
		return logger;
	}
	
	protected ExternalFolderHandler getExternalFolderHandler() {
		return folderHandler;
	}
	
	public abstract T getModel(String path);
	public abstract String getExternalPathPrefix();
	
	@RequestMapping(value = "/**/{file:.+\\.html?}", method = RequestMethod.GET)
	public ModelAndView staticHtml(HttpServletRequest req, HttpServletResponse res) throws ExternalFileNotFoundException {
		String fullPath = (String) req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String) req.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String path = new File(getExternalPathPrefix(), apm.extractPathWithinPattern(bestMatchPattern, fullPath)).getPath();
		logger.info("Internal static html " + path);
		if (!folderHandler.isExists(path)) {
			throw new ExternalFileNotFoundException(path);
		}
		return new ModelAndView("/include", "value", getModel(path));
	}

	@RequestMapping(value = "/**/{file:(?!(?:.+\\.html?)$).+$}", method = RequestMethod.GET)
	public void staticFile(HttpServletRequest req, HttpServletResponse res) throws ExternalFileNotFoundException {
		String fullPath = (String) req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String) req.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String path = new File(getExternalPathPrefix(), apm.extractPathWithinPattern(bestMatchPattern, fullPath)).getPath();
		int lastDot = fullPath.lastIndexOf(".");
		String ext = (lastDot >= 0 && lastDot < fullPath.length() - 1) ? fullPath.substring(lastDot + 1) : "";
		logger.info("Internal file from " + path + " ext : " + ext);
		
		try {
			folderHandler.copyFile(path, res.getOutputStream());
		} catch (IOException ie) {
			logger.error("copy file error", ie);
			throw new ExternalFileNotFoundException(path);
		}
	}
}
