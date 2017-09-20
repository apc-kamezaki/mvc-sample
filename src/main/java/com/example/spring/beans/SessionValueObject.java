package com.example.spring.beans;

public class SessionValueObject extends ExternalPath {
	
	private int year;
	private String site;
	private int grade;
	private boolean smartphone;
	
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
	
	public static class SessionValueObjectBuilder {
		private String path;
		private int year;
		private String site;
		private int grade;
		private boolean smartphone;

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
		
		public SessionValueObject build() {
			SessionValueObject vo = new SessionValueObject(path);
			vo.year = this.year;
			vo.site = this.site;
			vo.grade = this.grade;
			vo.smartphone = this.smartphone;
			return vo;
		}		
	}

}
