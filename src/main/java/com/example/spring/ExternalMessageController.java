package com.example.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring.beans.Message;

@Controller
@RequestMapping(value = "/external")
public class ExternalMessageController {
	private static final Logger logger = LoggerFactory.getLogger(ExternalMessageController.class);

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView message() {
		logger.info("Welcome external");
		return new ModelAndView("/external/external", "message", new Message());
	}
	
	@RequestMapping(value = "/extra", method = RequestMethod.GET)
	public ModelAndView extraMessage() {
		return new ModelAndView("/external/external", "message", new Message("Extra"));
	}

	@RequestMapping(value = "/extra-static", method = RequestMethod.GET)
	public ModelAndView extraStatic() {
		return new ModelAndView("/external/include", "path", "/external/external-static.html");
	}
}
