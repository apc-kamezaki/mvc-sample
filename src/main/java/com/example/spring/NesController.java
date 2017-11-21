package com.example.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.spring.beans.SessionValueObject;
import com.example.spring.beans.SessionValueObject.SessionValueObjectBuilder;

@Controller
@RequestMapping(value = "/contents/nes")
public class NesController extends AbstractContentsController<SessionValueObject> {

	@Override
	public SessionValueObject getModel(String path) {
		return new SessionValueObjectBuilder()
				.setPath(path)
				.setYear(2017)
				.setSite("1")
				.setGrade(6)
				.setMap(getExternalFolderHandler().getLocalVariables(getExternalPathPrefix()))
				.build();
	}

	@Override
	public String getExternalPathPrefix() {
		return "/contents/nes";
	}

}
