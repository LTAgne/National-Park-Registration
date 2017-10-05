package com.techelevator.projects.model;

import java.time.LocalDate;

public class DesiredReservation {

//Instance Variable
	private int campgroundNumber;
	private LocalDate arrivalDate;
	private LocalDate departureDate;
	private LocalDate trial;
	
//Logic Setters	
	public Boolean logicSetterCampgroundNumber(String userInput, int listSize) {
		if (userInput.equals("0")){
			return null;
		}else if(!(Integer.parseInt(userInput) > 0 && Integer.parseInt(userInput) < listSize)){
			return false;
		}
		campgroundNumber = Integer.parseInt(userInput);
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
	
//Dumb Ol' Getters
	public int getCampgroundNumber() {
		return campgroundNumber;
	}

	public LocalDate getArrivalDate() {
		return arrivalDate;
	}

	public LocalDate getDepartureDate() {
		return departureDate;
	}

//Date Checker
	private boolean dateChecker(String userInput){
		String[] splitDate = userInput.split("\\D");
		if(splitDate.length != 3){
			return false;
		}
		if(splitDate[0].length() != 4 && splitDate[1].length() != 2 && splitDate[2].length() != 2){
			return false;
		}
		int year = Integer.parseInt(splitDate[0]);
		int month = Integer.parseInt(splitDate[1]);
		int day = Integer.parseInt(splitDate[2]);
		
		if(month > 0 && month < 13){
			if (month == 1 && day < 1 && day > 31){
				return false;
			}else if ((month == 2 && day < 1 && day > 28) || (year % 4 == 0 && month == 02 && day < 1 && day > 29)){
				return false;
			}else if (month == 3 && day < 1 && day > 31){
				return false;
			}else if (month == 4 && day < 1 && day > 30){
				return false;
			}else if (month == 5 && day < 1 && day > 31){
				return false;
			}else if (month == 6 && day < 1 && day > 30){
				return false;
			}else if (month == 7 && day < 1 && day > 31){
				return false;
			}else if (month == 8 && day < 1 && day > 31){
				return false;
			}else if (month == 9 && day < 1 && day > 30){
				return false;
			}else if (month == 10 && day < 1 && day > 31){
				return false;
			}else if (month == 11 && day < 1 && day > 30){
				return false;
			}else if (month == 12 && day < 1 && day > 31){
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
	
	

}
