package com.example.spring.beans;

public class NesValueObject extends ExternalPath {
	private int year;
	private String site;
	private int grade;
	
	public NesValueObject(String path, int year, String site, int grade) {
		super(path);
		this.year = year;
		this.site = site;
		this.grade = grade;
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
	
	public String getComment() {
		return "<font color=\"red\">Sample</font>全体の年間スケジュールです。<br>" +
				   "この年間スケジュールは概略をお伝えするものであり、あくまでも予定で、確定ではないことを予めご了解ください。<br>" +
				   "また、年間スケジュールは、確定次第、随時変更・追加をしていきます。<br>"  +
				   "教室によっては、年間スケジュールと異なるスケジュールを組んでいる場合もありますので、<font color=\"red\">必ず教室独自の月間スケジュールと併せてご確認ください。</font>";
	}
}
