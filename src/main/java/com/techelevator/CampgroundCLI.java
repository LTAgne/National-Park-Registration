package com.techelevator;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.projects.model.Campground;
import com.techelevator.projects.model.CampgroundDao;
import com.techelevator.projects.model.DesiredReservation;
import com.techelevator.projects.model.Park;
import com.techelevator.projects.model.ParkDao;
import com.techelevator.projects.model.Reservation;
import com.techelevator.projects.model.ReservationDao;
import com.techelevator.projects.model.Site;
import com.techelevator.projects.model.SiteDao;
import com.techelevator.projects.model.jdbc.CampgroundJdbcDao;
import com.techelevator.projects.model.jdbc.ParkJdbcDao;
import com.techelevator.projects.model.jdbc.ReservationJdbcDao;
import com.techelevator.projects.model.jdbc.SiteJdbcDao;
import com.techelevator.projects.view.Menu;

public class CampgroundCLI {

//Constants
	private static final String RETURN_TO_MAIN_SCREEN = "Return to Main Menu";
	
	private static final String VIEW_CAMPGROUNDS = "View Campgrounds";
	private static final String[] PARK_INFORMATION_MENU_OPTIONS = new String[] {
																		   VIEW_CAMPGROUNDS,
																		   RETURN_TO_MAIN_SCREEN,
																		   };
	
	private static final String SEARCH_RESERVATION = "Search for Available Reservation Dates";
	private static final String[] CAMPGROUND_INFORMATION_MENU_OPTIONS = new String[] {
																		   SEARCH_RESERVATION,
																		   RETURN_TO_MAIN_SCREEN,};
	
//Instance Variables
	private Menu menu;
	private ParkDao parkDao;
	private CampgroundDao campgroundDao;
	private SiteDao siteDao;
	private ReservationDao reservationDao;


//Constructor
	public CampgroundCLI(DataSource dataSource) {
		this.menu = new Menu(System.in, System.out);
		
		parkDao = new ParkJdbcDao(dataSource);
		campgroundDao = new CampgroundJdbcDao(dataSource);
		siteDao = new SiteJdbcDao(dataSource);
		reservationDao = new ReservationJdbcDao(dataSource);
	}

//Public Methods
	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}
	
	public void run() {
		//displayApplicationBanner();	
		while(true) {
			printHeading("Select a Park for Further Details");
			String[] options = appendQuitToStringArray(parkDao.getParksListToNameArray(parkDao.getAllParks()));
			String choice = (String)menu.getChoiceFromOptions(options);
			if(choice.equals("Quit")){
				System.exit(0);
			}else for(int i = 0; i < options.length -1; i++){
				if(choice.equals(options[i])){
					handleParks(options[i]);
				}
			}
		}
	}
	
	
//Private Sub-Menu Handling	
	private void handleParks(String parkName) {
		printHeading(parkName);
		Park currentPark = parkDao.returnParkByName(parkName);
		System.out.println("Location: " + String.format("%17s", currentPark.getLocation()));
		System.out.println("Established: " + String.format("%14s", currentPark.getEstablishDate().toString()));  
		System.out.println("Area: " + String.format("%21s", NumberFormat.getNumberInstance(Locale.US).format(currentPark.getArea())));
		System.out.println("Annual Visitors: " + String.format("%10s", NumberFormat.getNumberInstance(Locale.US).format(currentPark.getVisitors())) + "\n");
		
		int letterCount = 0;
		for(String word : currentPark.getDescription().split(" ")){
			System.out.print(word + " ");
			letterCount += word.length();
			if(letterCount >= 60){
				System.out.println();
				letterCount = 0;
			}
		}
		System.out.println("\n");
		
		while(true){
			String choice = (String)menu.getChoiceFromOptions(PARK_INFORMATION_MENU_OPTIONS);
			if(choice.equals(VIEW_CAMPGROUNDS)) {
				handleParkCampgrounds(currentPark);
				break;
			} else if(choice.equals(RETURN_TO_MAIN_SCREEN)) {
				break;
			} 
		}
	}
	
	
	
	
	
	
	private void handleParkCampgrounds(Park currentPark) {
		
		printHeading(currentPark.getName() + " National Park Campgrounds");
		List<Campground> campgroundList = campgroundDao.getFiveCampgroundsInPark(currentPark.getParkId());
		displayCampgrounds(campgroundList);
		
		while(true){
			String choice = (String)menu.getChoiceFromOptions(CAMPGROUND_INFORMATION_MENU_OPTIONS);
			if(choice.equals(SEARCH_RESERVATION)) {
				handleReservations(campgroundList);
				break;
			} else if(choice.equals(RETURN_TO_MAIN_SCREEN)) {
				break;
			}
		}
	}



	private void handleReservations(List<Campground> campgroundList) {
		DesiredReservation dr = new DesiredReservation();
		Scanner input = new Scanner(System.in);
		
		displayCampgrounds(campgroundList);
		
		System.out.println("Which campground number would you like to reserve? (Enter 0 to cancel)");
		String userInput = input.nextLine();
		Boolean numberSuccessful = dr.logicSetterCampgroundNumber(userInput, campgroundList.size());
		if (numberSuccessful == null){
			return;
		}
		if (!numberSuccessful){
			System.out.println("Not valid input.");
			return;
		}
		
		System.out.println("What is the arrival date? (YYYY/MM/DD)");
		userInput = input.nextLine();
		if (!dr.logicSetterArrivalDate(userInput)){
			System.out.println("Not valid input.");
			return;
		}
		System.out.println("What is the departure date? (YYYY/MM/DD)");
		userInput = input.nextLine();
		if (!dr.logicSetterDepartureDate(userInput)){
			System.out.println("Not valid input.");
			return;
		}
		Campground selectedCampground = dr.logicSetterCampgroundId(campgroundList);
		if(!(dr.isCampgroundOpen(selectedCampground, dr))){
			System.out.println("Sorry, the park is not open, please select another date");
			return;
		}
		List<Site> availableSites = siteDao.getFiveAvailableSitesInPark(dr);
		printHeading("Results Matching Your Search Criteria");
		if(availableSites.isEmpty()){
			System.out.println("There are no available sites during that time.  Please select another date or campground.");
			return;
		}
		displaySites(availableSites, selectedCampground);
		
		System.out.println("\n Which site should be reserved? (Enter 0 to Cancel)");
		userInput = input.nextLine();
		Boolean userInputMatchedOptions = dr.logicSetterSiteId(availableSites, userInput);
		if(userInputMatchedOptions == null){
			return;
		}else if(userInputMatchedOptions == false){
			System.out.println("Not an available campsite, please select another");
			return;
		}
		System.out.println("What name should the reservation be under?");
		dr.setReservationName(input.nextLine());
		Reservation finalReservation = reservationDao.makeReservation(dr);
		System.out.println("The reservation has been made for " + finalReservation.getName() + " and the confirmation ID is " + finalReservation.getReservationId() +".");
		
	}

//Private Utility Methods
	private void displayCampgrounds(List<Campground> campgroundList){
		System.out.println(String.format("%35s", "Name") + String.format("%20s", "Open") + String.format("%20s", "Close") + String.format("%20s", "Daily Fee"));
		for(int i = 0; i < campgroundList.size(); i++){
			System.out.println("#"+ (i+1)  + String.format("%33s", campgroundList.get(i).getName())
							+ String.format("%20s", intMonthToStringMonth(campgroundList.get(i).getOpenFromMM()))
									+ String.format("%20s", intMonthToStringMonth(campgroundList.get(i).getOpenToMM()))
											+  String.format("%20s", "$" +campgroundList.get(i).getDailyFee() +"0"));
			campgroundList.get(i).setDisplayListLocation(i+1);
		}
		return;
	}
	
	private void displaySites(List<Site> siteList, Campground c){
		System.out.println(String.format("%15s", "Site No.") + String.format("%20s", "Max Occupancy") + String.format("%20s", "ADA Accessible?") + String.format("%20s", "Max RV Length") + String.format("%20s", "Utility") + String.format("%20s", "Cost"));
		for(int i = 0; i < siteList.size(); i++){
			System.out.println(String.format("%15s", siteList.get(i).getSiteNumber()) 
					+ String.format("%20s", siteList.get(i).getMaxOccupancy())
							+ String.format("%20s", siteList.get(i).isAccessible())
									+ String.format("%20s", siteList.get(i).getMaxRvLength())
										+ String.format("%20s", siteList.get(i).isUtilities())
											+  String.format("%20s", "$" + c.getDailyFee()));
		}
		return;
	}
	
	private String intMonthToStringMonth(String month){
		if (month.equals("01")){
			return "January";
		}else if (month.equals("02")){
			return "February";
		}else if (month.equals("03")){
			return "March";
		}else if (month.equals("04")){
			return "April";
		}else if (month.equals("05")){
			return "May";
		}else if (month.equals("06")){
			return "June";
		}else if (month.equals("07")){
			return "July";
		}else if (month.equals("08")){
			return "August";
		}else if (month.equals("09")){
			return "September";
		}else if (month.equals("10")){
			return "October";
		}else if (month.equals("11")){
			return "November";
		}else{
			return "December";
		}
	}
	
	private String[] appendQuitToStringArray(String[] oldArray){
		String[] appendedArray = new String[oldArray.length+1];
		for(int i = 0; i < oldArray.length; i++){
			appendedArray[i] = oldArray[i];
		}
		appendedArray[oldArray.length] = "Quit";
		return appendedArray;
	}

	private void printHeading(String headingText) {
		System.out.println("\n"+headingText);
		for(int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
}
