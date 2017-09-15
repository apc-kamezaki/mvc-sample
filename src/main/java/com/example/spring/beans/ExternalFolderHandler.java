package com.example.spring.beans;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ExternalFolderHandler {
	
	// TODO auto-wire by spring bean
	private ExternalFolderProperty property = new ExternalFolderProperty();
	
	public boolean isExists(String filename) {
		File file = new File(property.getExternalFolder(), filename);
		return file.exists();
	}

	public byte[] toByteArray(String filename) throws FileNotFoundException, IOException {
		File file = new File(property.getExternalFolder(), filename);
		
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
	
	public String getContentType(String ext) {
		String type = contentTypeMap.get(ext);
		return type != null ? type : contentTypeMap.get("");
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
