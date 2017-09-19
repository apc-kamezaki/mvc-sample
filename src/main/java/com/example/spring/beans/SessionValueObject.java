package com.example.spring.beans;

public class SessionValueObject extends ExternalPath {
	
	public SessionValueObject(String path) {
		super(path);
	}

	private int year;
	private String site;
	
	public int getYear() {
		return year;
	}
	public String getSite() {
		return site;
	}
	
	public static class SessionValueObjectBuilder {
		private String path;
		private int year;
		private String site;

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
		
		public SessionValueObject build() {
			SessionValueObject vo = new SessionValueObject(path);
			vo.year = this.year;
			vo.site = this.site;
			return vo;
		}		
	}

}
