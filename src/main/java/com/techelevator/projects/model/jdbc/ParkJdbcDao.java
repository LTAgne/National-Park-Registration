package com.techelevator.projects.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.projects.model.Park;
import com.techelevator.projects.model.ParkDao;

public class ParkJdbcDao implements ParkDao{
	
//Instance Variables
	private JdbcTemplate jdbcTemplate;

//Constructor
	public ParkJdbcDao (DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

//Public Methods
	public List<Park> getAllParks(){
		List<Park> parkList = new ArrayList<>();
		String sqlGetAllParks = "SELECT * FROM park ORDER BY name";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllParks);
		while(results.next()){
			parkList.add(mapRowToPark(results));
		}
		return parkList;
	}
	
	public String[] getParksListToNameArray(List<Park> parkList){
		String[] nameArray = new String[parkList.size()];
		for(int i = 0; i<nameArray.length; i++){
			nameArray[i]=parkList.get(i).getName();
		}
		return nameArray;
	}
	
	public Park returnParkByName(String name){
		Park returnPark = new Park();
		String sqlGetParkInfo = "SELECT * FROM park WHERE name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetParkInfo, name);
		results.next();
		returnPark.setName(name);
		returnPark.setLocation(results.getString("location"));
		returnPark.setEstablishDate(results.getDate("establish_date").toLocalDate()); 
		returnPark.setArea(results.getInt("area"));
		returnPark.setVisitors(results.getInt("visitors"));
		returnPark.setDescription(results.getString("description"));
		returnPark.setParkId(results.getLong("park_id"));
		return returnPark;
	}

//Unused, may be implemented at a later date
	public Park createPark(String name, String location, LocalDate establishDate, int area, int visitors, String description){
			Park park = new Park();
			park.setName(name);
			park.setLocation(location);
			park.setEstablishDate(establishDate);
			park.setArea(area);
			park.setVisitors(visitors);
			park.setDescription(description);
			
			String sqlCreatePark = "INSERT INTO park (name, location, establish_date, area, visitiors, description) VALUES (?,?,?,?,?,?) RETURNING park_id";
			park.setParkId(jdbcTemplate.queryForObject(sqlCreatePark,Long.class, name, location, establishDate, area, visitors, description));
			return park;
	}
	
//Private Methods
	private Park mapRowToPark(SqlRowSet results) {
		Park thePark = new Park();
		thePark.setParkId(results.getLong("park_id"));
		thePark.setName(results.getString("name"));
		thePark.setLocation(results.getString("location"));
		thePark.setEstablishDate(results.getDate("establish_date").toLocalDate());
		thePark.setArea(results.getInt("area"));
		thePark.setVisitors(results.getInt("visitors"));
		thePark.setDescription(results.getString("description"));
		return thePark;
	}

}
