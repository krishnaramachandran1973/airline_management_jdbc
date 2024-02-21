package com.wipro.service;

import com.wipro.exceptions.BookingException;
import com.wipro.model.Flight;
import com.wipro.model.Passenger;
import com.wipro.model.Ticket;

public class BookingService {
	public Ticket bookTicket(Flight flight, Passenger passenger) {
		if (flight.checkAvailability()) {
			flight.decrementAvailableSeats();
			return Ticket.builder()
					.flight(flight)
					.passenger(passenger)
					.build();
		}
		throw new BookingException("Please check seat availability");
	}
}
