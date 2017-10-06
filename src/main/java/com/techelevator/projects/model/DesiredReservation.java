package com.techelevator.projects.model;

import java.time.LocalDate;
import java.util.List;

public class DesiredReservation {

//Instance Variable
	private int campgroundSelectedNumber;
	private Long campgroundId;
	private LocalDate arrivalDate;
	private LocalDate departureDate;
	private LocalDate trial;
//May be moved in a refactor
	private Long siteId;
	private String reservationName;
	
//Logic Setters	
	public Boolean logicSetterCampgroundNumber(String userInput, int listSize) {
		if (userInput.equals("0")){
			return null;
		}else if (userInput.split("\\D").length != 1){
			return false;
		}else if(Integer.parseInt(userInput) < 0  || Integer.parseInt(userInput) > listSize){
			return false;
		}
		campgroundSelectedNumber = Integer.parseInt(userInput);
		return true;
	}
	
	public boolean logicSetterArrivalDate(String userInput) {
		if(!dateChecker(userInput)){
			return false;
		}
		arrivalDate = trial;
		trial = null;
		return true;
	}
	
	public boolean logicSetterDepartureDate(String userInput) {
		if(!dateChecker(userInput)){
			return false;
		}
		if(trial.isBefore(arrivalDate)){
			return false;
		}
		departureDate = trial;
		trial = null;
		return true;
	}
	
	public Campground logicSetterCampgroundId(List<Campground> campgrounds) {
		for(Campground element : campgrounds){
			if(element.getDisplayListLocation() == campgroundSelectedNumber){
				campgroundId = element.getCampgroundId();
				return element;
			}
		}
		return null;
	}
	
	public Boolean logicSetterSiteId(List<Site> sites, String userInput){
		if(userInput.equals("0")){
			return null;
		}
		if (userInput.split("\\D").length != 1){
			return false;
		}
		for(Site element : sites){
			if(Integer.parseInt(userInput) == element.getSiteNumber()){
				siteId = element.getSiteId();
				return true;
			}
		}
		return false;
	}
//Dumb Ol' Getters and Setter
	public int getCampgroundNumber() {
		return campgroundSelectedNumber;
	}

	public LocalDate getArrivalDate() {
		return arrivalDate;
	}

	public LocalDate getDepartureDate() {
		return departureDate;
	}
	
	public Long getCampgroundId() {
		return campgroundId;
	}


	public String getReservationName() {
		return reservationName;
	}

	public Long getSiteId(){
		return siteId;
	}
	public void setReservationName(String reservationName) {
		this.reservationName = reservationName;
	}
	
//Utility Methods
	private boolean dateChecker(String userInput){
		String[] splitDate = userInput.split("\\D");
		if(splitDate.length != 3){
			return false;
		}
		if(splitDate[0].length() != 4 || splitDate[1].length() != 2 || splitDate[2].length() != 2){
			return false;
		}
		int year = Integer.parseInt(splitDate[0]);
		int month = Integer.parseInt(splitDate[1]);
		int day = Integer.parseInt(splitDate[2]);
		
		if(month > 0 && month < 13){
			if (day < 1){
				return false;
			}
			if (month == 1 && day < 1 && day > 31){
				return false;
			}else if ((month == 2 && year % 4 != 0 && day > 28) || (year % 4 == 0 && month == 02 && day > 29)){
				return false;
			}else if (month == 3 && day > 31){
				return false;
			}else if (month == 4 && day > 30){
				return false;
			}else if (month == 5 && day > 31){
				return false;
			}else if (month == 6 && day > 30){
				return false;
			}else if (month == 7 && day > 31){
				return false;
			}else if (month == 8 && day > 31){
				return false;
			}else if (month == 9 && day > 30){
				return false;
			}else if (month == 10 && day > 31){
				return false;
			}else if (month == 11 && day > 30){
				return false;
			}else if (month == 12 && day > 31){
				return false;
			}
		}else{
			return false;
		}
		
		trial = LocalDate.of(year, month, day);
		
		if (trial.isBefore(LocalDate.now())){
			trial = null;
			return false;
		}
		return true;
	}
	
	public boolean isCampgroundOpen(Campground campground, DesiredReservation dr){
		int closeMonth = Integer.parseInt(campground.getOpenToMM());
		int openMonth = Integer.parseInt(campground.getOpenFromMM());
		int arrivalMonth = dr.getArrivalDate().getMonth().getValue();
		int departureMonth = dr.getDepartureDate().getMonth().getValue();
		
		if(closeMonth < openMonth){
			if(arrivalMonth >= closeMonth && arrivalMonth < openMonth){
				return false;
			}
			if(departureMonth >= closeMonth && departureMonth < openMonth){
				return false;
			}
			return true;
		}else if(closeMonth <= arrivalMonth && arrivalMonth < openMonth +12){
			return false;
		}else if(closeMonth <= departureMonth && departureMonth < openMonth +12){
			return false;
		}else if(closeMonth <= arrivalMonth+12 && arrivalMonth+12 < openMonth +12){
			return false;
		}else if(closeMonth <= departureMonth+12 && departureMonth+12 < openMonth +12){
			return false;
		}
		return true;
	}


}