package main.com.pcache.performance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import main.com.pcache.utils.Commons;

public class TestBed {

	private static int _noOfTimeseriess = 100;
	private static int _noOfClients = 100;
	private static int _startYear = 2010;
	private static int _endYear = 2020;
	private static int _startNoOfDays = 100;
	private static int _endNoOfDays = 200;

	private static int _initialize() {
		
		Utils u = new Utils();
		int count = 0;

		for (int i=0;i<_noOfTimeseriess;i++) {

			int seedYear = Commons.randInt(_startYear, _endYear);
			String timeseriesDataPoints = u.generateTimeseries(seedYear, 
					_startNoOfDays, _endNoOfDays);

			String command = "ALLOC " + timeseriesDataPoints;

			new Thread(new Client(command)).start();

			int noOfPoints = timeseriesDataPoints.split(" ")[0].split(",").length;

			count += noOfPoints;

			System.out.println("ALLOC ["+seedYear+"] ["+noOfPoints+"]");

		}

		return count;

	}

	private static void _doStuff() {

		for (int i=0; i<_noOfClients; i++) {

			int tsToWorkWith = Commons.randInt(1, _noOfTimeseriess);

			String command = "GETALL " + tsToWorkWith;

			new Thread(new Client(command)).start();
			
		}
		
	}

	public static void main(String[] args) {
		
		Properties propertyHandler = new Properties();
		
		try {
			propertyHandler.load(new FileInputStream("properties/testbed.properties"));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
		_noOfTimeseriess = Integer.parseInt(propertyHandler.getProperty(
				"NO_OF_TIMESERIES"));
		_noOfClients = Integer.parseInt(propertyHandler.getProperty(
				"NO_OF_CLIENTS"));
		_startYear = Integer.parseInt(propertyHandler.getProperty("START_YEAR"));
		_endYear = Integer.parseInt(propertyHandler.getProperty("END_YEAR"));
		_startNoOfDays = Integer.parseInt(propertyHandler.getProperty(
				"START_NO_OF_DAYS"));
		_endNoOfDays = Integer.parseInt(propertyHandler.getProperty(
				"END_NO_OF_DAYS"));

		int noOfPointsAllocated = _initialize();
		_doStuff();

		System.out.println("Points: " + noOfPointsAllocated);

	}

}
