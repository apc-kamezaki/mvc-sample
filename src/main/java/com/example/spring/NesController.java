package com.example.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.spring.beans.SessionValueObject;
import com.example.spring.beans.SessionValueObject.SessionValueObjectBuilder;

@Controller
@RequestMapping(value = "/contents/nes")
public class NesController extends AbstractContentsController<SessionValueObject> {

	@Override
	protected String getContentsTopDirectory(String path) {
		return "/contents/nes";
	}

	@Override
	public SessionValueObject getModel(String path) {
		return new SessionValueObjectBuilder()
				.setPath(path)
				.setYear(2017)
				.setSite("1")
				.setGrade(6)
				.setMap(getExternalFolderHandler().getLocalVariables(getContentsTopDirectory(path)))
				.build();
	}

	@Override
	public String getExternalPathPrefix() {
		return "/contents/nes";
	}

}
