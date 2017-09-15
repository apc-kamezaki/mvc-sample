package com.example.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.spring.beans.NygValueObject;

@Controller
@RequestMapping(value = "/nyg")
public class NygController extends AbstractExternalFileController<NygValueObject> {

	@Override
	public NygValueObject getModel(String path) {
		return new NygValueObject(path, getCurrentYear(), getSite());
	}

	@Override
	public String getExternalPathPrefix() {
		return "/nyg";
	}
	
	int getCurrentYear() {
		return 2017;
	}
	
	String getSite() {
		return "3";
	}

	@RequestMapping(value = {"/", "/index.html"}, method=RequestMethod.GET)
	public String index() {
		return String.format("redirect:/nyg/%d/%s_nyg.html", getCurrentYear(), getSite());
	}
}
