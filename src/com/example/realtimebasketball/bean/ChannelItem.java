package com.example.realtimebasketball.bean;

import java.io.Serializable;

public class ChannelItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	/** 
	 * 으커ID
	 *  */
	public Integer id;
	/** 
	 * 으커NAME
	 *  */
	public String name;
	/** 
	 * 으커 rank
	 *  */
	public Integer orderId;
	

	public ChannelItem() {
	}

	public ChannelItem(int id, String name, int orderId) {
		this.id = Integer.valueOf(id);
		this.name = name;
		this.orderId = Integer.valueOf(orderId);
	}

	public int getId() {
		return this.id.intValue();
	}

	public String getName() {
		return this.name;
	}

	public int getOrderId() {
		return this.orderId.intValue();
	}

	

	public void setId(int paramInt) {
		this.id = Integer.valueOf(paramInt);
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setOrderId(int paramInt) {
		this.orderId = Integer.valueOf(paramInt);
	}

	

	public String toString() {
		return "ChannelItem [id=" + this.id + ", name=" + this.name
				+ ", selected=" +  "]";
	}
}