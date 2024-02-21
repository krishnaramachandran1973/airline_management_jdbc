package com.wipro.service;

import java.util.List;

import com.wipro.model.Flight;
import com.wipro.model.FlightType;

public interface MySqlService {

	List<Flight> findAllFlights();

	void saveFlight(Flight flight, FlightType type);

	void deleteFlight(String flightNumber);

	void updateFlight(Flight flight);
}
