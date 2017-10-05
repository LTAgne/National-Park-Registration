package com.techelevator.projects.model;

import java.util.List;

public interface CampgroundDao {

	public List<Campground> getFiveCampgroundsInPark(Long parkId);
}
