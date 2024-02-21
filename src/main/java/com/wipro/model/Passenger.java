package com.wipro.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Passenger {
	private String name;
	private String passportNumber;
}
