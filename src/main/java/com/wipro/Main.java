package com.wipro;

import java.util.List;

import com.wipro.model.Flight;
import com.wipro.model.Passenger;
import com.wipro.model.Ticket;
import com.wipro.service.BookingService;
import com.wipro.service.FlightSearchService;
import com.wipro.service.MySqlService;
import com.wipro.service.impl.MySqlServiceImpl;

public class Main {
	public static void main(String[] args) {
		MySqlService mySqlService = new MySqlServiceImpl();

		// List of available flights
		List<Flight> flights = mySqlService.findAllFlights();

		// Search available flights from JFK to LAX
		FlightSearchService flightSearcher = new FlightSearchService(flights);
		List<Flight> availableFlights = flightSearcher.searchFlights("DEL", "BOM");
		System.out.println("Available Flights from DEL to BOM:");
		availableFlights.stream()
				.forEach(flight -> System.out.println(flight.getFlightNumber() + " - Departure: "
						+ flight.getDepartureTime() + ", Arrival: " + flight.getArrivalTime()));

		// Book a ticket for the first available flight
		if (!availableFlights.isEmpty()) {
			Flight firstFlight = availableFlights.get(0);
			Passenger passenger = Passenger.builder()
					.name("John Doe")
					.passportNumber("AB123456")
					.build();
			BookingService bookingSystem = new BookingService();
			Ticket ticket = bookingSystem.bookTicket(firstFlight, passenger);
			System.out.println("\nTicket Details:");
			ticket.displayTicketDetails();

			Passenger passenger2 = Passenger.builder()
					.name("Jane Doe")
					.passportNumber("CD123456")
					.build();
			Ticket ticket2 = bookingSystem.bookTicket(firstFlight, passenger2);
			System.out.println("\nTicket Details:");
			ticket2.displayTicketDetails();

//			Passenger passenger3 = new Passenger("Susan Doe", "EF123456");
//			Ticket ticket3 = bookingSystem.bookTicket(firstFlight, passenger3);
//			System.out.println("\nTicket Details:");
//			ticket3.displayTicketDetails();
		} else {
			System.out.println("No available flights for booking.");
		}

		String flightNumber = availableFlights.get(0)
				.getFlightNumber();
		// mySqlService.deleteFlight(flightNumber);
	}
}
