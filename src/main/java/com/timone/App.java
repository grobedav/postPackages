package com.timone;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.timeone.concurrent.concurrent.WriteTask;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		IService service = new Service();
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(new WriteTask(service), 60, 60,
				TimeUnit.SECONDS);

		while (true) {
			Scanner scanner = new Scanner(System.in); // Create a Scanner object
			System.out.println("Enter weight of package and destination postal code separated by space.");
			System.out.println("Example 10.24 73301");
			System.out.println("Or 10.24 73301 fees.txt");
			String line = scanner.nextLine(); // Read user input
			System.out.println();

			if ("quit".equalsIgnoreCase(line)) {
				scheduledFuture.cancel(true);
				executor.shutdown();
				scanner.close();
				break;
			}
			String[] values = line.split(" ");
			if (values == null || values.length != 2 && values.length != 3) {
				System.out.println("We need two or three parameters.");
			} else if (values.length == 2) {
				try {
					service.addData(values[1], values[0]);
				} catch (RuntimeException e) {
					System.out.println(e.getMessage());
				}
			} else if (values.length == 3) {
				try {
					service.addData(values[1], values[0]);
					service.readFile(values[2]);
				} catch (RuntimeException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
}