package com.example.spring;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring.beans.ExternalFolderHandler;
import com.example.spring.beans.SessionValueObject;
import com.example.spring.beans.SessionValueObject.SessionValueObjectBuilder;
import com.example.spring.exceptions.ExternalFileNotFoundException;

@Controller
@RequestMapping(value = "/contents/nes")
public class NesController extends AbstractExternalFileController<SessionValueObject> {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM");

	@Override
	public SessionValueObject getModel(String path) {
		return getDefaultSessionValueObjectBuilder(path).build();
	}

	private SessionValueObjectBuilder getDefaultSessionValueObjectBuilder(String path) {
		return new SessionValueObjectBuilder()
				.setPath(path)
				.setYear(2017)
				.setSite("1")
				.setGrade(6)
				.setMap(getExternalFolderHandler().getLocalVariables(getExternalPathPrefix()));
	}

	@Override
	public String getExternalPathPrefix() {
		return "/contents/nes";
	}

	@RequestMapping(value = "/", method=RequestMethod.GET)
	public ModelAndView top() throws ExternalFileNotFoundException {
		ExternalFolderHandler handler = getExternalFolderHandler();
		File template = new File(getExternalPathPrefix(), "index.ftl");
		logger().info("call / of nes");
		if (!handler.isExists(template.getPath())) {
			logger().error("cannot find " + template.getPath());
			throw new ExternalFileNotFoundException(template.getPath());
		}
		SessionValueObjectBuilder builder = getDefaultSessionValueObjectBuilder(template.getPath());
		Map<String, Object> map = new HashMap<>(getExternalFolderHandler().getLocalVariables(getExternalPathPrefix()));
		String currentDate = dateFormat.format(new Date());
		map.put("current",currentDate);
		builder.setMap(map);
		return new ModelAndView(getTemplate(template.getPath()), "value", builder.build());
	}
}
