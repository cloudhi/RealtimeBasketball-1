package com.example.realtimebasketball.model;

import org.litepal.crud.DataSupport;

public class CbaNews extends DataSupport {
	private Long id;
	private String title;
	private String url;
	private String photoUrl;
	private String source;
	private String date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "CbaNews [id=" + id + ", title=" + title + ", url=" + url
				+ ", photoUrl=" + photoUrl + ", source=" + source + ", date="
				+ date + "]";
	}

}
