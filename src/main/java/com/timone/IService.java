package com.timone;

import java.util.Map;

public interface IService {
    boolean isValidWeight(String weight);
    boolean isValidZipCode(String zipCode);
    public Map<String,Double> getSortedData();
    void readFile(String fileName) throws RuntimeException;
	boolean isValidFee(String fee);
	boolean isEmptyFees();
	double getFee(Double weight);
	void addData(String zipCode, String weight) throws RuntimeException;
	Map<String, Double> getTotalFees();
}
