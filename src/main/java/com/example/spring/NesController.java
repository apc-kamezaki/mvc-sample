package com.example.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring.beans.NesValueObject;

@Controller
@RequestMapping(value = "/nes")
public class NesController extends AbstractExternalFileController<NesValueObject> {
	private static final Logger logger = LoggerFactory.getLogger(NesController.class);
	
	private static final int CURRENT_YEAR = 2017;
	
	@RequestMapping(value = {"/", "/index.html"}, method=RequestMethod.GET)
	public String index() {
		return String.format("redirect:/nes/%d/", getCurrentYear());
	}
	
	private int getCurrentYear() {
		return CURRENT_YEAR;
	}
	
	@RequestMapping(value = {"/{year}/", "/{year}/index.html"}, method=RequestMethod.GET)
	public ModelAndView yearTop(@PathVariable("year") int year) {
		
		logger.info("nes index");
		String file = String.format("/nes/%d/%s_%d.html", year, "1", 6);
		NesValueObject vo = new NesValueObject(file, 2017, "1", 6);
		logger.info("Nes Index " + vo.getPath());
		return new ModelAndView("/include", "value", vo);
	}

	@Override
	public NesValueObject getModel(String path) {
		return new NesValueObject(path, 2017,  "1", 6);
	}

	@Override
	public String getExternalPathPrefix() {
		return "/nes";
	}
	
}
