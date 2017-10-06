package com.techelevator.projects.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;


public class DesiredReservationTest {

	private DesiredReservation dr;
	
	@Before
	public void setUp() throws Exception {
		dr = new DesiredReservation();
	}
	
	@Test
	public void testCampgroundNumberZero() {
		assertNull(dr.logicSetterCampgroundNumber("0", 5));
		assertEquals(dr.getCampgroundNumber(), 0);
	}
	
	@Test
	public void testCampgroundNumberNotANumber() {
		assertFalse(dr.logicSetterCampgroundNumber("q", 5));
		assertEquals(dr.getCampgroundNumber(), 0);
	}
	
	@Test
	public void testCampgroundNumberIsAlphanumeric() {
		assertFalse(dr.logicSetterCampgroundNumber("q3!", 5));
		assertEquals(dr.getCampgroundNumber(), 0);
	}
	
	@Test
	public void testCampgroundNumberOneTooHigh() {
		assertFalse(dr.logicSetterCampgroundNumber("6", 5));
		assertEquals(dr.getCampgroundNumber(), 0);
	}
	
	@Test
	public void testCampgroundNumberTooLow() {
		assertFalse(dr.logicSetterCampgroundNumber("-1", 5));
		assertEquals(dr.getCampgroundNumber(), 0);
	}
	
	@Test
	public void testCampgroundNumberInRange() {
		assertTrue(dr.logicSetterCampgroundNumber("4", 5));
		assertEquals(dr.getCampgroundNumber(), 4);
	}
	
	@Test
	public void testCampgroundNumberOnRange() {
		assertTrue(dr.logicSetterCampgroundNumber("5", 5));
		assertEquals(dr.getCampgroundNumber(), 5);
	}
	
	@Test
	public void testArrivalDateBadNumberOfThings() {
		assertFalse(dr.logicSetterArrivalDate("5/6/7/8"));
		assertNull(dr.getArrivalDate());
	}
	
	
	@Test
	public void testArrivalDateAlphanumeric() {
		assertFalse(dr.logicSetterArrivalDate("2017/5b/18"));
		assertNull(dr.getArrivalDate());
	}
	
	@Test
	public void testArrivalDateYearWrongSize() {
		assertFalse(dr.logicSetterArrivalDate("20171/01/01"));
		assertNull(dr.getArrivalDate());
	}
	
	@Test
	public void testArrivalDateMonthWrongSize() {
		assertFalse(dr.logicSetterArrivalDate("2017/011/01"));
		assertNull(dr.getArrivalDate());
	}
	
	@Test
	public void testArrivalDateDayWrongSize() {
		assertFalse(dr.logicSetterArrivalDate("2017/01/011"));
		assertNull(dr.getArrivalDate());
	}
	
	@Test
	public void testArrivalDateTooBig() {
		assertFalse(dr.logicSetterArrivalDate("2017/13/01"));
		assertNull(dr.getArrivalDate());
	}
	
	@Test
	public void testArrivalLeapYear() {
		assertFalse(dr.logicSetterArrivalDate("2021/02/29"));
		assertNull(dr.getArrivalDate());
		assertTrue(dr.logicSetterArrivalDate("2020/02/29"));
		assertNotNull(dr.getArrivalDate());
	}
	
	@Test
	public void testArrivalDateBeforeToday() {
		assertFalse(dr.logicSetterArrivalDate("2000/01/01"));
		assertNull(dr.getArrivalDate());
	}
	
	@Test
	public void testArrivalDateIsAllGood() {
		assertTrue(dr.logicSetterArrivalDate("3000/01/01"));
		assertNotNull(dr.getArrivalDate());
	}
	
	@Test
	public void testDepartureDateIsBeforeArival() {
		dr.logicSetterArrivalDate("3000/01/01");
		assertFalse(dr.logicSetterDepartureDate("2999/01/01"));
		assertNull(dr.getDepartureDate());
	}
	
	@Test
	public void testDepartureDateIsAllGood() {
		dr.logicSetterArrivalDate("3000/01/01");
		assertTrue(dr.logicSetterDepartureDate("3001/01/01"));
		assertNotNull(dr.getDepartureDate());
	}



}
