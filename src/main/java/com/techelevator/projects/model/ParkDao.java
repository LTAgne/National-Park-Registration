package com.techelevator.projects.model;

import java.time.LocalDate;
import java.util.List;

public interface ParkDao {
	
	public List<Park> getAllParks();
	public Park createPark(String name, String location, LocalDate establishDate, int area, int visitors, String description);
	public String[] getParksListToNameArray(List<Park> parkList);
	public Park returnParkByName(String name);
}
