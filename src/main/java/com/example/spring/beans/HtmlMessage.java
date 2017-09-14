package com.example.spring.beans;

public class HtmlMessage extends Message {
	private String path;
	
	public HtmlMessage(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}

}
