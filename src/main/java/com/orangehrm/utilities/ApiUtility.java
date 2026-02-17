package com.orangehrm.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiUtility {

	// Method to send the GET request
	public static Response sendGetRequest(String endpoint) {
		return RestAssured.get(endpoint);
	}

	// Method to send the POST request
	public static Response sendPostRequest(String endpoint, String payload) {
		return RestAssured.given().header("conent-type", "application/json").body(payload).post();

	}
	// Method to validate the response status

	public static boolean validateStatusCode(Response response, int statuscode) {
		return response.getStatusCode() == statuscode;
	}

	// Method to extract value from JSON response
	public static String getJsonValue(Response response, String value) {
		return response.jsonPath().getString(value);
	}

}
