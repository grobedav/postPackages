package com.timone;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ServiceTest2 {

	public static final String ZIP_CODE = "73572";
	public static final String ZIP_CODE1 = "73571";
	private IService service;

	@Before
	public void setUp() throws Exception {
		service = new Service();
		URI uri = ClassLoader.getSystemResource("").toURI();
		String mainPath = Paths.get(uri).toString();
		Path path = Paths.get(mainPath ,"fees.txt");
		service.readFile(path);
	}

	@Test
	public void sortData() {
		service.addData("73301", "10.9");
		service.addData("90876", "67.9");
		LinkedHashMap<String, Double> expectedData = new LinkedHashMap<>();
		expectedData.put("90876", 67.9);
		expectedData.put("73301", 10.9);
		assertEquals(expectedData.toString(), service.getSortedData().toString());
	}

	@Test
	public void addData() {
		Map<String, Double> testData;
		service.addData(ZIP_CODE, "20.65");
		service.addData(ZIP_CODE, "20.0");
		service.addData(ZIP_CODE1, "10.0");
		testData = service.getSortedData();
		assertEquals(Double.valueOf(40.65), testData.get(ZIP_CODE));
		assertEquals(Double.valueOf(10.0), testData.get(ZIP_CODE1));
	}

	@Test
	public void getTotalFees() {
		service.addData(ZIP_CODE, "20.65");
		service.addData(ZIP_CODE, "20.0");
		service.addData(ZIP_CODE1, "5.0");
		Map<String, Double> totalFees = service.getTotalFees();
		assertEquals(Double.valueOf(10.0), totalFees.get(ZIP_CODE));
		assertEquals(Double.valueOf(2.5), totalFees.get(ZIP_CODE1));
	}
}
