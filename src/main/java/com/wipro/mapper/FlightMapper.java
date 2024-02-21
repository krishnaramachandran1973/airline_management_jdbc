package com.wipro.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.wipro.model.BrownfieldFlight;
import com.wipro.model.Fare;
import com.wipro.model.Flight;
import com.wipro.model.FlightType;

public class FlightMapper {
	public static Flight mapFlight(ResultSet resultSet) throws SQLException {
		Fare fare = Fare.builder()
				.amount(resultSet.getDouble("amount"))
				.currency(resultSet.getString("currency"))
				.build();
		return BrownfieldFlight.builder()
				.flightNumber(resultSet.getString("flight_number"))
				.departureAirport(resultSet.getString("departure_airport"))
				.destinationAirport(resultSet.getString("destination_airport"))
				.departureTime(resultSet.getString("departure_time"))
				.arrivalTime(resultSet.getString("arrival_time"))
				.availableSeats(resultSet.getInt("available_seats"))
				.type(FlightType.valueOf(resultSet.getString("flight_type")))
				.fare(fare)
				.build();
	}

}
