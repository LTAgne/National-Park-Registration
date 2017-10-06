package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.DesiredReservation;
import com.techelevator.projects.model.Site;
import com.techelevator.projects.model.SiteDao;

public class SiteJdbcDao implements SiteDao{

	private JdbcTemplate jdbcTemplate;
	
	public SiteJdbcDao (DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public List<Site> getFiveAvailableSitesInPark(DesiredReservation dr) {
		List<Site> siteList = new ArrayList<>();
		String sqlGetFiveAvailabeSites = "SELECT s.* FROM site s JOIN reservation r ON s.site_id = r.site_id  WHERE campground_id = ? AND ? NOT BETWEEN from_date AND to_date AND ? NOT BETWEEN from_date AND to_date AND from_date NOT BETWEEN ? AND ? AND to_date NOT BETWEEN ? AND ? GROUP BY s.site_id LIMIT 5;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetFiveAvailabeSites, dr.getCampgroundId(), dr.getArrivalDate(), dr.getDepartureDate(), dr.getArrivalDate(), dr.getDepartureDate(), dr.getArrivalDate(), dr.getDepartureDate());
		while(results.next()){
			siteList.add(mapRowToSite(results));
		}
		return siteList;
	}

	
	private Site mapRowToSite(SqlRowSet results) {
		Site theSite = new Site();
		theSite.setSiteId(results.getLong("site_id"));
		theSite.setCampgroundId(results.getInt("campground_id"));
		theSite.setSiteNumber(results.getInt("site_number"));
		theSite.setMaxOccupancy(results.getInt("max_occupancy"));
		theSite.setAccessible(results.getBoolean("accessible"));
		theSite.setMaxRvLength(results.getInt("max_rv_length"));
		theSite.setUtilities(results.getBoolean("utilities"));
		return theSite;
	}

}
