package edu.ycp.cs320.cspath1.sqldb;

import java.util.Scanner;

import edu.ycp.cs320.cspath1.persist.DatabaseProvider;

import edu.ycp.cs320.cspath1.persist.YCPDatabase;



public class InitDatabase {
	public static void init(Scanner keyboard) {
		System.out.print("Which database (0=fake, 1=derby): ");
		int which = Integer.parseInt(keyboard.nextLine());
		if (which == 1) {
			DatabaseProvider.setInstance(new YCPDatabase());
		} else {
			throw new IllegalArgumentException("Invalid choice: " + which);
		}
	}
}
