package com.wipro.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Fare {
	private double amount;
	private String currency;
}
