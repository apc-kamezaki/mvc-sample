package com.example.spring;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring.beans.ExternalFolderHandler;
import com.example.spring.beans.Message;

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
	public ResponseEntity<byte[]> staticFile(HttpServletRequest req) throws FileNotFoundException, IOException {
		String fullPath = (String) req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String) req.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String path = apm.extractPathWithinPattern(bestMatchPattern, fullPath);
		path = path.startsWith("/") ? path : "/" + path;
		int lastDot = fullPath.lastIndexOf(".");
		String ext = (lastDot >= 0 && lastDot < fullPath.length() - 1) ? fullPath.substring(lastDot + 1) : "";
		logger.info("Internal file from " + path + " ext : " + ext);
		
		// TODO handle exception
		byte[] data = folderHandler.toByteArray(path);
		HttpHeaders headers = new HttpHeaders();
		headers.put("Content-type", Arrays.asList(folderHandler.getContentType(ext)));
		return new ResponseEntity<>(data, headers, HttpStatus.OK);
	}
}
