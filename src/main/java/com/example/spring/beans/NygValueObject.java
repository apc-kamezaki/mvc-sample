package com.example.spring.beans;

public class NygValueObject extends ExternalPath {
	private int year;
	private String site;

	public NygValueObject(String path, int year, String site) {
		super(path);
		this.year = year;
		this.site = site;
	}

	public int getYear() {
		return year;
	}

	public String getSite() {
		return site;
	}
	
	public String getDescription() {
		return "Sample学習や、Sample受験に関してよく使われる「用語」の解説集です。<br>" +
	            "Sampleで使われる用語や、保護者会に参加して、スタッフが話している用語で分からないときは「基礎用語集　～Sample編～」を、私立・国立中高一貫校に関して使われる用語や、学校説明会などに参加して、学校の先生方が話している用語で分からないときは「基礎用語集　～中学受験編～」や「基礎用語集　～中高一貫校編～」を参考にしてください。<br>" +
	            "目次の項目をクリックするとその用語の解説にジャンプします。<br>" +
	            "目次に戻る時は、「▲目次」をクリックしてください。";
	}

}
