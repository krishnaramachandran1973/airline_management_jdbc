package com.wipro.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.wipro.mapper.FlightMapper;
import com.wipro.model.BrownfieldFlight;
import com.wipro.model.Flight;
import com.wipro.model.FlightType;
import com.wipro.service.DatabaseConnection;
import com.wipro.service.MySqlService;

public class MySqlServiceImpl implements MySqlService {
	private static Connection connection;

	static {
		try {
			connection = DatabaseConnection.getConnection();
			initDb();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void initDb() throws SQLException {
		try {
			// Disable foreign key checks
			disableForeignKeyChecks();

			// Clear data from the flights table
			clearTable("flights");

			// Clear data from the fares table
			clearTable("fares");

			// Re-enable foreign key checks
			enableForeignKeyChecks();

			System.out.println("Table data cleared successfully.");

			System.out.println("Generating sample data");
			// Generating sample flights with fare
			String[] flightNumbers = { "DL123", "UA456", "BA789", "AF101" };
			String[] departureAirports = { "HYD", "DEL", "TRV", "VTC" };
			String[] destinationAirports = { "CCU", "BOM", "COK", "GAU" };
			String[] departureTimes = { "10:00", "12:00", "15:00", "14:00" };
			String[] arrivalTimes = { "14:00", "14:30", "07:00", "06:30" };
			int[] availableSeats = { 150, 120, 200, 180 };
			double[] fareAmounts = { 200.00, 180.00, 500.00, 450.00 };
			String[] currencies = { "INR", "INR", "INR", "INR" };
			String[] flights = { "DOMESTIC", "DOMESTIC", "INTERNATIONAL", "INTERNATIONAL", };

			// Inserting sample flights into the flights table
			String flightsInsertQuery = "INSERT INTO flights (flight_number, departure_airport, destination_airport, departure_time, arrival_time, available_seats, flight_type) VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement flightsInsertStatement = connection.prepareStatement(flightsInsertQuery);
			for (int i = 0; i < flightNumbers.length; i++) {
				flightsInsertStatement.setString(1, flightNumbers[i]);
				flightsInsertStatement.setString(2, departureAirports[i]);
				flightsInsertStatement.setString(3, destinationAirports[i]);
				flightsInsertStatement.setString(4, departureTimes[i]);
				flightsInsertStatement.setString(5, arrivalTimes[i]);
				flightsInsertStatement.setInt(6, availableSeats[i]);
				flightsInsertStatement.setString(7, flights[i]);
				flightsInsertStatement.addBatch();
			}
			flightsInsertStatement.executeBatch();

			// Inserting sample fares into the fares table
			String faresInsertQuery = "INSERT INTO fares (flight_number, amount, currency) VALUES (?, ?, ?)";
			PreparedStatement faresInsertStatement = connection.prepareStatement(faresInsertQuery);
			for (int i = 0; i < flightNumbers.length; i++) {
				faresInsertStatement.setString(1, flightNumbers[i]);
				faresInsertStatement.setDouble(2, fareAmounts[i]);
				faresInsertStatement.setString(3, currencies[i]);
				faresInsertStatement.addBatch();
			}
			faresInsertStatement.executeBatch();

			// Closing resources
			flightsInsertStatement.close();
			faresInsertStatement.close();
			// connection.close();

			System.out.println("Sample flights with fare inserted successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Method to disable foreign key checks
	private static void disableForeignKeyChecks() throws SQLException {
		try (Statement statement = connection.createStatement()) {
			statement.execute("SET FOREIGN_KEY_CHECKS = 0");
		}
	}

	// Method to enable foreign key checks
	private static void enableForeignKeyChecks() throws SQLException {
		try (Statement statement = connection.createStatement()) {
			statement.execute("SET FOREIGN_KEY_CHECKS = 1");
		}
	}

	// Method to clear data from a table
	public static void clearTable(String tableName) throws SQLException {
		String deleteQuery = "DELETE FROM " + tableName;
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(deleteQuery);
		}
	}

	@Override
	public List<Flight> findAllFlights() {
		List<Flight> flights = new ArrayList<>();

		// SQL query to retrieve flights with fare information
		String query = "SELECT f.*, fa.amount, fa.currency FROM flights f "
				+ "JOIN fares fa ON f.flight_number = fa.flight_number";

		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

			// Process the ResultSet
			while (resultSet.next()) {
				/*
				 * String flightNumber = resultSet.getString("flight_number"); String
				 * departureAirport = resultSet.getString("departure_airport"); String
				 * destinationAirport = resultSet.getString("destination_airport"); String
				 * departureTime = resultSet.getString("departure_time"); String arrivalTime =
				 * resultSet.getString("arrival_time"); int availableSeats =
				 * resultSet.getInt("available_seats"); FlightType type =
				 * FlightType.valueOf(resultSet.getString("flight_type")); double fareAmount =
				 * resultSet.getDouble("amount"); String currency =
				 * resultSet.getString("currency");
				 * 
				 * // Create Fare object Fare fare = Fare.builder() .amount(fareAmount)
				 * .currency(currency) .build();
				 * 
				 * // Create Flight object Flight flight = BrownfieldFlight.builder()
				 * .flightNumber(flightNumber) .departureAirport(departureAirport)
				 * .destinationAirport(destinationAirport) .departureTime(departureTime)
				 * .arrivalTime(arrivalTime) .availableSeats(availableSeats) .type(type)
				 * .fare(fare) .type(FlightType.DOMESTIC) .build();
				 */
				Flight flight = FlightMapper.mapFlight(resultSet);
				// Add flight to the list
				flights.add(flight);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return flights;
	}

	@Override
	public void saveFlight(Flight flight, FlightType type) {
		try (PreparedStatement flightStatement = connection
				.prepareStatement("INSERT INTO flights VALUES (?, ?, ?, ?, ?, ?, ?)");
				PreparedStatement fareStatement = connection.prepareStatement("INSERT INTO fares VALUES (?, ?, ?)")) {
			flightStatement.setString(1, flight.getFlightNumber());
			flightStatement.setString(2, flight.getDepartureAirport());
			flightStatement.setString(3, flight.getDestinationAirport());
			flightStatement.setString(4, flight.getDepartureTime());
			flightStatement.setString(5, flight.getArrivalTime());
			flightStatement.setInt(6, ((BrownfieldFlight) flight).getAvailableSeats());
			flightStatement.setString(7, type.name());
			flightStatement.executeUpdate();

			fareStatement.setString(1, flight.getFlightNumber());
			fareStatement.setDouble(2, flight.getFare()
					.getAmount());
			fareStatement.setString(3, flight.getFare()
					.getCurrency());
			fareStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void deleteFlight(String flightNumber) {
		try (PreparedStatement flightStatement = connection
				.prepareStatement("DELETE FROM flights WHERE flight_number = ?");
				PreparedStatement fareStatement = connection
						.prepareStatement("DELETE FROM fares WHERE flight_number = ?")) {
			flightStatement.setString(1, flightNumber);
			flightStatement.executeUpdate();

			fareStatement.setString(1, flightNumber);
			fareStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	@Override
	public void updateFlight(Flight flight) {
		try (PreparedStatement flightStatement = connection.prepareStatement(
				"UPDATE flights SET departure_airport = ?, destination_airport = ?, departure_time = ?, arrival_time = ?, available_seats = ? WHERE flight_number = ?");
				PreparedStatement fareStatement = connection
						.prepareStatement("UPDATE fares SET amount = ?, currency = ? WHERE flight_number = ?")) {
			flightStatement.setString(1, flight.getDepartureAirport());
			flightStatement.setString(2, flight.getDestinationAirport());
			flightStatement.setString(3, flight.getDepartureTime());
			flightStatement.setString(4, flight.getArrivalTime());
			flightStatement.setInt(5, ((BrownfieldFlight) flight).getAvailableSeats());
			flightStatement.setString(6, flight.getFlightNumber());
			flightStatement.executeUpdate();

			fareStatement.setDouble(1, flight.getFare()
					.getAmount());
			fareStatement.setString(2, flight.getFare()
					.getCurrency());
			fareStatement.setString(3, flight.getFlightNumber());
			fareStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
