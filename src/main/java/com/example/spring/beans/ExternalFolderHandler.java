package com.example.spring.beans;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class ExternalFolderHandler {
	public static final String LocalVariablesFileName = "variables.json";	
	private static final Logger logger = LoggerFactory.getLogger(ExternalFolderHandler.class);
	
	// TODO auto-wire by spring bean
	private ExternalFolderProperty property = new ExternalFolderProperty();
	private Gson gson = new Gson();
	
	public boolean isExists(String filename) {
		File file = getFile(filename);
		return file.exists();
	}

	public byte[] toByteArray(String filename) throws FileNotFoundException, IOException {
		File file = getFile(filename);
		
		try (
				InputStream is = new BufferedInputStream(new FileInputStream(file));
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			byte[] buffer= new byte[2048];
			int length;
			while ((length = is.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			out.flush();
			return out.toByteArray();
		} finally {
			
		}
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
	
	protected File getFile(String filename) {
		return new File(property.getExternalFolder(), filename);
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
