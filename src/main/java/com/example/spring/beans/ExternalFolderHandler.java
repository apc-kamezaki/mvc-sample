package com.example.spring.beans;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ExternalFolderHandler {
	public static final String LocalVariablesFileName = "variables.json";
	private static final Logger logger = LoggerFactory.getLogger(ExternalFolderHandler.class);

	@NonNull
	private final ExternalFolderProperty property;
	
	private Gson gson = new Gson();
	
	public boolean isExists(String filename) {
		File file = getFile(filename);
		return file.exists();
	}

	public Map<String, Object> getLocalVariables(String localPath) {
		Map<String, Object> localVariables = Collections.emptyMap();
		if (localPath == null || localPath.length() < 1) {
			return localVariables;
		}
		File localFile = getFile(new File(localPath, LocalVariablesFileName).getAbsolutePath());
		try (FileReader reader = new FileReader(localFile)) {
			localVariables = gson.fromJson(reader, new TypeToken<Map<String, Object>>(){}.getType());
		} catch (JsonParseException e) {
			logger.debug("parse gson error", e);
		} catch (IOException e) {
			logger.debug("read gson error", e);
		} finally {
		}
		return localVariables;
	}
	
	public String getContentType(String ext) {
		String type = contentTypeMap.get(ext);
		return type != null ? type : contentTypeMap.get("");
	}
	
	public void copyFile(String filename, OutputStream os) throws IOException {
		Files.copy(getPath(filename), os);
	}

	protected File getFile(String filename) {
		return new File(property.getExternalFolder(), filename);
	}
	
	private Path getPath(String filename) {
		return FileSystems.getDefault().getPath(property.getExternalFolder(), filename);
	}
	
	
	public boolean isBinary(String ext) {
		switch (ext) {
		case "html":
		case "htm":
		case "css":
		case "js":
		case "xml":
			return false;
		default:
			return true;
		}
	}
	
	private static final Map<String, String> contentTypeMap;
	static {
		contentTypeMap = new HashMap<>();
		contentTypeMap.put("jpg", "image/jpeg");
		contentTypeMap.put("jpeg", "image/jpeg");
		contentTypeMap.put("png", "image/png");
		contentTypeMap.put("gif", "image/gif");
		contentTypeMap.put("pdf", "application/pdf");
		contentTypeMap.put("css", "text/css");
		contentTypeMap.put("xml", "application/xml");
		contentTypeMap.put("js", "text/javascript");
		contentTypeMap.put("swf","application/x-shockwave-flash");
		contentTypeMap.put("", "application/octet-stream");
	}
}
