package com.timone;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Service implements IService {
	private Pattern forWeight = Pattern.compile("^([0-9]|[1-9][0-9]|[1-9][0-9][0-9])(\\.\\d{1,3})?$");
	private Pattern forFee = Pattern.compile("^([0-9]|[1-9][0-9]|[1-9][0-9][0-9])(\\.\\d{1,2})?$");
	private Pattern forZipCode = Pattern.compile("^\\d{5}$");
	private Map<String, Double> data;
	private Map<Double, Double> fees;
	private Map<String, List<Double>> totalFees;

	Service() {
		data = Collections.synchronizedMap(new HashMap<String, Double>());
		totalFees = Collections.synchronizedMap(new HashMap<String, List<Double>>());
		fees = Collections.synchronizedMap(new TreeMap<Double, Double>(Collections.reverseOrder()));
	}

	@Override
	public boolean isValidWeight(String weight) {
		if (weight == null) {
			return false;
		}
		Matcher matcher = forWeight.matcher(weight);
		return matcher.matches();
	}

	@Override
	public boolean isValidZipCode(String zipCode) {
		if (zipCode == null) {
			return false;
		}
		Matcher matcher = forZipCode.matcher(zipCode);
		return matcher.matches();
	}

	@Override
	public Map<String, Double> getSortedData() {
		LinkedHashMap<String, Double> reverseSortedMap = new LinkedHashMap<>();
		synchronized (data) {
			data.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
					.forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
		}
		return reverseSortedMap;
	}

	@Override
	public Map<String, Double> getTotalFees() {
		Map<String, Double> countFees = new HashMap<String, Double>();
		if (fees.isEmpty()) {
			throw new UnsupportedOperationException("Fees for weight do not exist, define them first!");
		}
		synchronized (totalFees) {
			for (Entry<String, List<Double>> totalFee : totalFees.entrySet()) {
				Double fee = 0.0;
				for (Double weight : totalFee.getValue()) {
					fee += getFee(weight);
				}
				countFees.put(totalFee.getKey(), fee);
			}
		}
		return countFees;
	}

	@Override
	public void addData(String zipCode, String weight) throws RuntimeException {
		if (!isValidWeight(weight)) {
			throw new RuntimeException(
					"weight should be: positive number, >0 and <1000, maximal 3 decimal places, . (dot) as decimal separator");

		}
		if (!isValidZipCode(zipCode)) {
			throw new RuntimeException("postal code should be: fixed 5 digits");
		}
		addData(zipCode, Double.parseDouble(weight));
	}

	private void addData(String zipCode, Double weight) {
		data.computeIfPresent(zipCode, (key, value) -> value + weight);
		data.computeIfAbsent(zipCode, key -> weight);
		totalFees.computeIfPresent(zipCode, (key, value) -> {
			value.add(weight);
			return value;
		});
		totalFees.computeIfAbsent(zipCode, (k) -> {
			List<Double> v = new ArrayList<>();
			v.add(weight);
			return v;
		});
	}

	@Override
	public void readFile(String fileName) throws RuntimeException {
		synchronized (fees) {
			fees.clear();
			List<String> lines = Collections.emptyList();
			try {
				lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
				for (String line : lines) {
					String[] values = line.split(" ");
					if (values.length != 2) {
						fees.clear();
						throw new RuntimeException("Every line should have just two parameters");
					}
					if (!isValidWeight(values[0])) {
						fees.clear();
						throw new RuntimeException(
								"weight should be: positive number, >0 and <1000, maximal 3 decimal places, . (dot) as decimal separator");
					}
					if (!isValidFee(values[1])) {
						fees.clear();
						throw new RuntimeException(
								"fee should be: positive number, >0 and <1000, maximal 2 decimal places, . (dot) as decimal separator");
					}
					fees.put(Double.valueOf(values[0]), Double.valueOf(values[1]));
				}
			} catch (IOException e) {
				fees.clear();
				new RuntimeException(e.getMessage());
			}
		}
	}

	@Override
	public boolean isValidFee(String fee) {
		if (fee == null) {
			return false;
		}
		Matcher matcher = forFee.matcher(fee);
		return matcher.matches();
	}

	@Override
	public double getFee(Double weight) {
		double minFee = 0.0;
		synchronized (fees) {
			for (Entry<Double, Double> entry : fees.entrySet()) {
				if (weight >= entry.getKey()) {
					return entry.getValue();
				}
				// only last value will stay in minFee, is reverse sorted map,
				// min value will be last
				minFee = entry.getValue();
			}
		}
		return minFee;
	}

	@Override
	public boolean isEmptyFees() {
		return fees.isEmpty();
	}

}
