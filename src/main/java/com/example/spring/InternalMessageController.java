package com.example.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring.beans.Message;
import com.example.spring.beans.ExternalPath;
import com.example.spring.beans.HtmlMessage;

@Controller
@RequestMapping(value = "/internal")
public class InternalMessageController extends AbstractExternalFileController<HtmlMessage> {
	private static final Logger logger = LoggerFactory.getLogger(InternalMessageController.class);

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView message() {
		logger.info("Welcome internal");
		return new ModelAndView("basic", "message", new Message());
	}
	
	@RequestMapping(value = "/include", method = RequestMethod.GET)
	public ModelAndView include() {
		logger.info("Internal include");
		return new ModelAndView("/include", "value", new ExternalPath("/external/external-static.html"));
	}

	@Override
	public HtmlMessage getModel(String path) {
		return new HtmlMessage(path);
	}

	@Override
	public String getExternalPathPrefix() {
		return "";
	}
}
