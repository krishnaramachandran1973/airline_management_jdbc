package com.wipro.model;

import lombok.Builder;

@Builder
public class Ticket {
	private Flight flight;
	private Passenger passenger;

	public void displayTicketDetails() {
		System.out.println("Passenger Name: " + passenger.getName());
		System.out.println("Passenger Passport: " + passenger.getPassportNumber());
		System.out.println("Flight Number: " + flight.getFlightNumber());
		System.out.println("Departure Airport: " + flight.getDepartureAirport());
		System.out.println("Destination Airport: " + flight.getDestinationAirport());
		System.out.println("Departure Time: " + flight.getDepartureTime());
		System.out.println("Arrival Time: " + flight.getArrivalTime());
		Fare fare = flight.getFare();
		System.out.println("Fare: " + fare.getCurrency() + " " + fare.getAmount());
	}
}
