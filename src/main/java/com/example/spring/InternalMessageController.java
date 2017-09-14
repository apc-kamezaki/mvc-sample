package com.example.spring;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring.beans.ExternalFolderHandler;
import com.example.spring.beans.HtmlMessage;
import com.example.spring.beans.Message;
import com.example.spring.beans.Path;
import com.example.spring.exceptions.ExternalFileNotFoundException;

@Controller
@RequestMapping(value = "/internal")
public class InternalMessageController {
	private static final Logger logger = LoggerFactory.getLogger(InternalMessageController.class);
	private static final AntPathMatcher apm = new AntPathMatcher();
	
	// TODO auto-wire by spring-bean
	private ExternalFolderHandler folderHandler = new ExternalFolderHandler();

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView message() {
		logger.info("Welcome internal");
		return new ModelAndView("basic", "message", new Message());
	}
	
	@RequestMapping(value = "/include", method = RequestMethod.GET)
	public ModelAndView include() {
		logger.info("Internal include");
		return new ModelAndView("/include", "path", new Path("/external/external-static.html"));
	}

	/*
	@RequestMapping(value = "/message", method = RequestMethod.GET)
	public ModelAndView replaceableMessage() {
		logger.info("Internal include");
		return new ModelAndView("/include", "path", new HtmlMessage("/external/info/message.html"));
	}
	*/
	
	@RequestMapping(value = "/**/{file:.+\\.html?}", method = RequestMethod.GET)
	public ModelAndView staticHtml(HttpServletRequest req, HttpServletResponse res) throws ExternalFileNotFoundException {
		String fullPath = (String) req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String) req.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String path = apm.extractPathWithinPattern(bestMatchPattern, fullPath);
		path = path.startsWith("/") ? path : "/" + path;
		logger.info("Internal static html " + path);
		if (!folderHandler.isExists(path)) {
			throw new ExternalFileNotFoundException(path);
		}
		return new ModelAndView("/include", "path", new HtmlMessage(path));
	}

	@RequestMapping(value = "/**/{file:(?!(?:.+\\.html?)$).+$}", method = RequestMethod.GET)
	public void staticFile(HttpServletRequest req, HttpServletResponse res) throws ExternalFileNotFoundException, IOException {
		String fullPath = (String) req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String) req.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String path = apm.extractPathWithinPattern(bestMatchPattern, fullPath);
		path = path.startsWith("/") ? path : "/" + path;
		int lastDot = fullPath.lastIndexOf(".");
		String ext = (lastDot >= 0 && lastDot < fullPath.length() - 1) ? fullPath.substring(lastDot + 1) : "";
		logger.info("Internal file from " + path + " ext : " + ext);
		
		try {
			byte[] data = folderHandler.toByteArray(path);
			
			res.setContentType(folderHandler.getContentType(ext));
			if (folderHandler.isBinary(ext)) {
				res.setCharacterEncoding(null);
			}
			res.getOutputStream().write(data);
			res.getOutputStream().flush();
		} catch (FileNotFoundException fe) {
			throw new ExternalFileNotFoundException(path);
		}
		// FIXME: Do not need to handle other exceptions?
	}
}
