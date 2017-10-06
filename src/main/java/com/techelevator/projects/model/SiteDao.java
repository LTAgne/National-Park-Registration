package com.techelevator.projects.model;

import java.util.List;

public interface SiteDao {
	
	public List<Site> getFiveAvailableSitesInPark(DesiredReservation dr);
}
