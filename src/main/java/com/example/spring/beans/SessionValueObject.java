package com.example.spring.beans;

import java.util.Collections;
import java.util.Map;

public class SessionValueObject extends ExternalPath {
	
	private int year;
	private String site;
	private int grade;
	private boolean smartphone;
	private Map<String, Object> map;
	
	public SessionValueObject(String path) {
		super(path);
	}

	public int getYear() {
		return year;
	}
	public String getSite() {
		return site;
	}
	public int getGrade() {
		return grade;
	}
	public boolean isSmartphone() {
		return smartphone;
	}
	
	public Map<String, Object> getMap() {
		return map;
	}
	
	public static class SessionValueObjectBuilder {
		private String path;
		private int year;
		private String site;
		private int grade;
		private boolean smartphone;
		private Map<String, Object> map = Collections.emptyMap();

		public SessionValueObjectBuilder setPath(String path) {
			this.path = path;
			return this;
		}
		
		public SessionValueObjectBuilder setYear(int year) {
			this.year = year;
			return this;
		}
		public SessionValueObjectBuilder setSite(String site) {
			this.site = site;
			return this;
		}
		public SessionValueObjectBuilder setGrade(int grade) {
			this.grade = grade;
			return this;
		}
		public SessionValueObjectBuilder setSmartphone(boolean smartphone) {
			this.smartphone = smartphone;
			return this;
		}
		public SessionValueObjectBuilder setMap(Map<String, Object> map) {
			this.map = map;
			return this;
		}
		
		public SessionValueObject build() {
			SessionValueObject vo = new SessionValueObject(path);
			vo.year = this.year;
			vo.site = this.site;
			vo.grade = this.grade;
			vo.smartphone = this.smartphone;
			vo.map = this.map;
			return vo;
		}		
	}

}
