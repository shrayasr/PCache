package main.com.pcache.performance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import main.com.pcache.exceptions.PCacheException;
import main.com.pcache.utils.Commons;
import main.com.pcache.utils.Timer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class TestBed {

	private static int _noOfTimeseriess = 100;
	private static int _noOfClients = 100;
	private static int _startYear = 2010;
	private static int _endYear = 2020;
	private static int _startNoOfDays = 100;
	private static int _endNoOfDays = 200;
	
	private static Logger _logger;

	private static int _initialize() {
		
		Utils u = new Utils();
		int count = 0;
		
		CountDownLatch latch = new CountDownLatch(_noOfTimeseriess);

		List<String> timeseriess = new ArrayList<String>();
		
		for (int i=0;i<_noOfTimeseriess;i++) {

			int seedYear = Commons.randInt(_startYear, _endYear);
			String timeseriesDataPoints = u.generateTimeseries(seedYear, 
					_startNoOfDays, _endNoOfDays);
			
			timeseriess.add(timeseriesDataPoints);
			
			int noOfPoints = timeseriesDataPoints.split(" ")[0].split(",").length;
			
			count += noOfPoints;
		}
		
		try
		{
			Timer.start("ALLOCS");
		} catch (PCacheException e1)
		{
			e1.printStackTrace();
		}
		
		for (String timeseries : timeseriess) {

			String command = "ALLOC " + timeseries;

			new Thread(new Client(command, latch)).start();

			//System.out.println("ALLOC ["+seedYear+"] ["+noOfPoints+"]");

		}
		
		try
		{
			latch.await();
			Timer.end("ALLOCS");
			_logger.info("TIMING " + Timer.get("ALLOCS"));
			return count;
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			return 0;
		}
		catch (PCacheException e) {
			e.printStackTrace();
			return 0;
		}

	}

	private static void _doStuff() {

		
		CountDownLatch latch = new CountDownLatch(_noOfClients);
		
		for (int i=0; i<_noOfClients; i++) {

			int tsToWorkWith = Commons.randInt(1, _noOfTimeseriess);

			String command = "GETALL " + tsToWorkWith;

			Timer.lap("GETALLS");
			new Thread(new Client(command, latch)).start();
			
		}
		
		try
		{
			latch.await();
			Timer.end("GETALLS");
			_logger.info("TIMING " + Timer.get("GETALLS"));
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		catch (PCacheException e)
		{
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		
		PropertyConfigurator.configure("properties/log4j.properties");
		_logger = Logger.getLogger(TestBed.class.getName());
		
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
