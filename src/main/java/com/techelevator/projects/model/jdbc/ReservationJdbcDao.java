package com.techelevator.projects.model.jdbc;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.projects.model.DesiredReservation;
import com.techelevator.projects.model.Reservation;
import com.techelevator.projects.model.ReservationDao;


public class ReservationJdbcDao implements ReservationDao{
	
	JdbcTemplate jdbcTemplate;
	
	public ReservationJdbcDao (DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public Reservation makeReservation(DesiredReservation dr){
		Reservation reservation = new Reservation();
		reservation.setName(dr.getReservationName());
		reservation.setSiteId(dr.getSiteId());
		reservation.setFromDate(dr.getArrivalDate());
		reservation.setToDate(dr.getDepartureDate());
		reservation.setCreateReservationDate(LocalDate.now());
		String sqlCreateReservation = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date) VALUES (?,?,?,?,?) RETURNING reservation_id";
		reservation.setReservationId(jdbcTemplate.queryForObject(sqlCreateReservation,Long.class, reservation.getSiteId(), reservation.getName(), reservation.getFromDate(), reservation.getToDate(), reservation.getCreateReservationDate()));
		return reservation;
	}
}
