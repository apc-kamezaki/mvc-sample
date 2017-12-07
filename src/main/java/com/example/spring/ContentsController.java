package com.example.spring;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.spring.beans.ExternalFolderHandler;
import com.example.spring.beans.SessionValueObject;
import com.example.spring.beans.SessionValueObject.SessionValueObjectBuilder;
import com.example.spring.exceptions.ExternalFileNotFoundException;

@Controller
@RequestMapping(value = "/contents")
public class ContentsController extends AbstractExternalFileController<SessionValueObject> {
	private static final AntPathMatcher apm = new AntPathMatcher();
	private static final Pattern subPathPattern = Pattern.compile("^(\\/contents\\/.+?)\\/.*$");
    private static final Pattern devicePathPattern = Pattern.compile("^(\\/contents\\/.+?\\/pc|sp)\\/.*$");

	@Override
	public SessionValueObject getModel(String path) {
		return new SessionValueObjectBuilder()
				.setPath(path)
				.setYear(2017)
				.setSite("1")
				.setGrade(6)
				.setMap(getExternalFolderHandler().getLocalVariables(getSubPath(path)))
				.build();
	}

	@Override
	public String getExternalPathPrefix() {
		return "/contents";
	}
	
	@Override
	protected String getTemplate(String path) {
        String subPath = getDeviceOrSubPath(path);
		File template = new File(subPath, "template.ftl");
		if (getExternalFolderHandler().isExists(template.getPath())) {
			return String.format("%s/template", subPath);
		} else {
			return super.getTemplate(path);
		}
	}

	@RequestMapping(value = {"/", "/index.html"}, method=RequestMethod.GET)
	public ModelAndView index(HttpServletRequest req) throws ExternalFileNotFoundException {
		String fullPath = (String) req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String) req.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String path = new File(getExternalPathPrefix(), apm.extractPathWithinPattern(bestMatchPattern, fullPath)).getPath();
		logger().info("not supported yet");
		throw new ExternalFileNotFoundException(path);
	}
	
	@RequestMapping(value = "/{sub}/", method = RequestMethod.GET)
	public ModelAndView subIndex(@PathVariable("sub") String sub, HttpServletRequest req) throws ExternalFileNotFoundException {
		String fullPath = (String) req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String) req.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String path = new File(getExternalPathPrefix(), apm.extractPathWithinPattern(bestMatchPattern, fullPath)).getPath() + "/"+ sub;
		logger().info("sub index : " + path + " sub : " + sub);
		ExternalFolderHandler handler = getExternalFolderHandler();
		File template = new File(path, "index.ftl");
		File html = new File(path, "index.html");
		File htm = new File(path, "index.htm");
		if (handler.isExists(template.getPath())) {
			return new ModelAndView(String.format("%s/%s/index", getExternalPathPrefix(), sub), "value", getModel(html.getPath()));
		} else if (handler.isExists(html.getPath())) {
			return new ModelAndView("redirect:index.html");
		} else if (handler.isExists(htm.getPath())) {
			return new ModelAndView("redirect:index.htm");
		}
		logger().info("not supported yet " + path);
		throw new ExternalFileNotFoundException(path);
	}
	
    @RequestMapping(value = "/{sub}/{device:pc|sp}/", method = RequestMethod.GET)
    public ModelAndView deviceIndex(@PathVariable("sub") String sub, @PathVariable("device") String device, HttpServletRequest req) throws ExternalFileNotFoundException {
        String fullPath = (String) req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) req.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String path = String.format("%s/%s/%s",
                new File(getExternalPathPrefix(), apm.extractPathWithinPattern(bestMatchPattern, fullPath)).getPath(),
                sub,
                device);
        logger().info("sub/device index : " + path + " sub : " + sub + " device : " + device);
        ExternalFolderHandler handler = getExternalFolderHandler();
        File template = new File(path, "index.ftl");
        File html = new File(path, "index.html");
        File htm = new File(path, "index.htm");
        if (handler.isExists(template.getPath())) {
            return new ModelAndView(String.format("%s/%s/%s/index", getExternalPathPrefix(), sub, device), "value", getModel(template.getPath()));
        } else if (handler.isExists(html.getPath())) {
            return new ModelAndView("redirect:index.html");
        } else if (handler.isExists(htm.getPath())) {
            return new ModelAndView("redirect:index.htm");
        }
        logger().info("not supported yet " + path);
        throw new ExternalFileNotFoundException(path);
    }
	
	private String getSubPath(String path) {
		Matcher m = subPathPattern.matcher(path);
		if (m.find()) {
			return m.group(1);
		}
		return null;
	}
	
	private String getDeviceOrSubPath(String path) {
	    Matcher dm = devicePathPattern.matcher(path);
	    if (dm.find()) {
            return dm.group(1);
	    }
	    return getSubPath(path);
	}
}
