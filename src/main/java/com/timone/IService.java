package com.timone;


import java.nio.file.Path;
import java.util.Map;

public interface IService {
    boolean isValidWeight(String weight);
    boolean isValidZipCode(String zipCode);
    public Map<String,Double> getSortedData();
	boolean isValidFee(String fee);
	boolean isEmptyFees();
	double getFee(Double weight);
	void addData(String zipCode, String weight) throws RuntimeException;
	Map<String, Double> getTotalFees();
	void readFile(Path fileName) throws RuntimeException;
}
