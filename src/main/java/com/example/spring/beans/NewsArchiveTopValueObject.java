package com.example.spring.beans;

public class NewsArchiveTopValueObject {
	private final ArchiveItem[] items = new ArchiveItem[3];
	
	public NewsArchiveTopValueObject(int[] years) {
		for (int i = 0; i < years.length; i++) {
			items[i].year = years[i];
			items[i].active = true;
		}
	}
	
	public ArchiveItem[] getItems() {
		return items;
	}
	
	public static class ArchiveItem {
		private int year;
		private boolean active = false;
		
		public int getYear() {
			return year;
		}

		public boolean isActive() {
			return active;
		}
	}

}
