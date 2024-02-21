package com.wipro.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Flight {
	private String flightNumber;
	private String departureAirport;
	private String destinationAirport;
	private String departureTime;
	private String arrivalTime;
	private Fare fare;
	private FlightType type;

	public abstract boolean checkAvailability();

	public abstract void decrementAvailableSeats();

}