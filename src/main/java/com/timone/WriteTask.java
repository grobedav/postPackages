package com.timone;

import java.util.Map;

public class WriteTask implements Runnable {
	private IService service;

	public WriteTask(IService service) {
		this.service = service;
	}

	public void run() {
		System.out.println();
		System.out.println("**********Output***********");
		System.out.println("***************************");
		Map<String, Double> data = service.getSortedData();
		if (!data.isEmpty() && service.isEmptyFees())
			data.entrySet().stream()
					.forEach(x -> System.out.println(String.format("%s %.3f", x.getKey(), x.getValue())));
		if (!data.isEmpty() && !service.isEmptyFees()) {
			Map<String, Double> totalFees = service.getTotalFees();
			data.entrySet().stream().forEach(x -> System.out
					.println(String.format("%s %.3f %.2f", x.getKey(), x.getValue(), totalFees.get(x.getKey()))));
		}
		System.out.println("***************************");
	}
}
