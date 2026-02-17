package com.orangehrm.utilities;

import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviders {
	private static final String FILE_PATH = System.getProperty("user.dir")
			+ "/src/test/resources/testdata/TestData.xlsx";

	@DataProvider(name = "validLoginData")
	public static Object[][] validLoginData() {
		return getSheetData("validLoginData");
	}

	@DataProvider(name = "inValidLoginData")
	public static Object[][] inValidLoginData() {
		return getSheetData("inValidLoginData");
	}

	@DataProvider(name = "empVerification")
	public static Object[][] empVerification() {
		return getSheetData("empVerification");
	}
	
	// To work with data providers we need to work with object of 2d-array that will
	// accept any type of data
	private static Object[][] getSheetData(String sheetName) {
		List<String[]> sheetData = ExcelReaderUtility.getSheetData(FILE_PATH, sheetName);
		// Get the excel : size of total rows and total columns length
		Object[][] data = new Object[sheetData.size()][sheetData.get(0).length];
		for (int i = 0; i < sheetData.size(); i++) {
			data[i] = sheetData.get(i);
		}
		return data;

	}

}
