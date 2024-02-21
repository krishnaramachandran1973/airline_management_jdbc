package com.wipro.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class BrownfieldFlight extends Flight {
	@Getter
	@Setter
	private int availableSeats;

	@Builder
	public BrownfieldFlight(String flightNumber, String departureAirport, String destinationAirport,
			String departureTime, String arrivalTime, Fare fare, int availableSeats, FlightType type) {
		super(flightNumber, departureAirport, destinationAirport, departureTime, arrivalTime, fare, type);
		this.availableSeats = availableSeats;
	}

	@Override
	public boolean checkAvailability() {
		return availableSeats > 0;
	}

	// Method to decrement available seats
	public void decrementAvailableSeats() {
		// Assuming the seat count cannot go below 0
		if (checkAvailability()) {

			this.setAvailableSeats(this.getAvailableSeats() - 1);

		}
	}

}
