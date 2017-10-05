package com.techelevator.projects.model;

import java.math.BigDecimal;

public class Campground {

//Instance Variables
	private Long campgroundId;
	private int parkId;
	private String name;
	private String openFromMM;
	private String openToMM;
	private BigDecimal dailyFee;
	
//Getters and Setters
	public Long getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(Long campgroundId) {
		this.campgroundId = campgroundId;
	}
	public int getParkId() {
		return parkId;
	}
	public void setParkId(int parkId) {
		this.parkId = parkId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOpenFromMM() {
		return openFromMM;
	}
	public void setOpenFromMM(String openFromMM) {
		this.openFromMM = openFromMM;
	}
	public String getOpenToMM() {
		return openToMM;
	}
	public void setOpenToMM(String openToMM) {
		this.openToMM = openToMM;
	}
	public BigDecimal getDailyFee() {
		return dailyFee;
	}
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}

}
