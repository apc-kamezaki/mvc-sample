package com.example.spring;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring.beans.ExternalFolderHandler;
import com.example.spring.beans.JdnDataBean;
import com.example.spring.beans.SessionValueObject;
import com.example.spring.beans.SessionValueObject.SessionValueObjectBuilder;
import com.example.spring.exceptions.ExternalFileNotFoundException;

@Controller
@RequestMapping(value = "/contents/jdn")
public class JdnController extends AbstractContentsController<SessionValueObject> {

	@Override
	public SessionValueObject getModel(String path) {
		SessionValueObjectBuilder builder = getDefaultSessionValueObjectBuilder(path);
		return builder
				.setMap(getExternalFolderHandler().getLocalVariables(getExternalPathPrefix()))
				.build();
	}
	
	private SessionValueObjectBuilder getDefaultSessionValueObjectBuilder(String path) {
		return new SessionValueObjectBuilder()
				.setPath(path)
				.setYear(2017)
				.setSite("1")
				.setGrade(6);
	}

	@Override
	public String getExternalPathPrefix() {
		return "/contents/jdn";
	}
	
	@RequestMapping(value = "/index.html", method=RequestMethod.GET)
	public ModelAndView index() throws ExternalFileNotFoundException {
		ExternalFolderHandler handler = getExternalFolderHandler();
		File html = new File(getExternalPathPrefix(), "index.html");
		logger().info("call index.html of jdn");
		if (!handler.isExists(html.getPath())) {
			logger().error("cannot find " + html.getPath());
			throw new ExternalFileNotFoundException(html.getPath());
		}
		SessionValueObjectBuilder builder = getDefaultSessionValueObjectBuilder(html.getPath());
		Map<String, Object> map = new HashMap<>(getExternalFolderHandler().getLocalVariables(getExternalPathPrefix()));
		map.put("list",getDataList());
		builder.setMap(map);
		return new ModelAndView(getTemplate(html.getPath()), "value", builder.build());
	}

	@RequestMapping(value = "/dgibk/index.html", params = { "y" }, method=RequestMethod.GET)
	public ModelAndView dgibkIndex(
			@RequestParam(value = "y") String yesr
			) throws ExternalFileNotFoundException {
		ExternalFolderHandler handler = getExternalFolderHandler();
		File html = new File(getExternalPathPrefix(), "dgibk/index.html");
		logger().info("call index.html of jdn dgibk");
		if (!handler.isExists(html.getPath())) {
			logger().error("cannot find " + html.getPath());
			throw new ExternalFileNotFoundException(html.getPath());
		}
		SessionValueObjectBuilder builder = getDefaultSessionValueObjectBuilder(html.getPath());
		Map<String, Object> map = new HashMap<>(getExternalFolderHandler().getLocalVariables(getExternalPathPrefix()));
		map.put("yesr", yesr);
		map.put("pageNum", pageNumMap.get(yesr));
		builder.setMap(map);
		return new ModelAndView(getTemplate(html.getPath()), "value", builder.build());
	}
	
	private List<JdnDataBean> getDataList() {
		return Arrays.asList(
				makeJdnDataBean("2015"),
				makeJdnDataBean("2014"),
				makeJdnDataBean("2013")
				);
	}
	
	private JdnDataBean makeJdnDataBean(final String yesr) {
		return new JdnDataBean(yesr);
	}
	
	private Map<String, Integer> pageNumMap = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;

		{
			put("2015", 50);
			put("2014", 48);
			put("2013", 46);
		}
	};
}
