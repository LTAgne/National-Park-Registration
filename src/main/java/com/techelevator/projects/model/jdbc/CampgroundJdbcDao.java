package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Campground;
import com.techelevator.projects.model.CampgroundDao;


public class CampgroundJdbcDao implements CampgroundDao{
	private JdbcTemplate jdbcTemplate;
	
	public CampgroundJdbcDao (DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public List<Campground> getFiveCampgroundsInPark(Long parkId) {
		List<Campground> campgroundList = new ArrayList<>();
		String sqlGetAllCampgrounds = "SELECT * FROM campground WHERE park_id = ? LIMIT 5";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampgrounds, parkId);
		while(results.next()){
			campgroundList.add(mapRowToCampground(results));
		}
		return campgroundList;
	}

	
	private Campground mapRowToCampground(SqlRowSet results) {
		Campground theCampground = new Campground();
		theCampground.setParkId(results.getInt("park_id"));
		theCampground.setName(results.getString("name"));
		theCampground.setCampgroundId(results.getLong("campground_id"));
		theCampground.setOpenFromMM(results.getString("open_from_mm"));
		theCampground.setOpenToMM(results.getString("open_to_mm"));
		theCampground.setDailyFee(results.getBigDecimal("daily_fee"));
		return theCampground;
	}

}
