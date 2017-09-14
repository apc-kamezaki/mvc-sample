package com.example.spring.beans;

public class HtmlMessage extends ExternalPath {
	private String title = "TITLE";
	private String description = "Description";
	
	public HtmlMessage(String path) {
		super(path);
	}
	
	public HtmlMessage(String path, String title) {
		super(path);
		this.title = title != null ? title : "";
	}
	
	public HtmlMessage(String path, String title, String description) {
		super(path);
		this.title = title != null ? title : "";
		this.description = description != null ? description : "";
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}

}
