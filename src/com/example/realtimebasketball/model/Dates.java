package com.example.realtimebasketball.model;

import org.litepal.crud.DataSupport;

public class Dates extends DataSupport{
	private Long id;
	private String dates;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

	@Override
	public String toString() {
		return "Dates [id=" + id + ", dates=" + dates + "]";
	}
	
	
}
