package com.example.spring.beans;

public class Message {
	private String title = "TITLE";
	private String description = "Description";
	
	public Message() {}
	
	public Message(String title) {
		this.title = title != null ? title : "";
	}
	
	public Message(String title, String description) {
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
