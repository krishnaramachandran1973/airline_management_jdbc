package com.wipro.service;

import java.util.List;
import java.util.stream.Collectors;

import com.wipro.model.Flight;

public class FlightSearchService {
	private List<Flight> flights;

	public FlightSearchService(List<Flight> flights) {
		this.flights = flights;
	}

	public List<Flight> searchFlights(String departureAirport, String destinationAirport) {
		return flights.stream()
				.filter(flight -> flight.getDepartureAirport()
						.equals(departureAirport)
						&& flight.getDestinationAirport()
								.equals(destinationAirport)
						&& flight.checkAvailability())
				.collect(Collectors.toList());
	}
}
