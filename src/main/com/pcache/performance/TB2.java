package main.com.pcache.performance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.com.pcache.exceptions.PCacheException;
import main.com.pcache.utils.Commons;
import main.com.pcache.utils.Timer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.joda.time.DateTime;

public class TB2
{

	private static Logger _logger;

	private static int _noOfTimeseriess = 200;
	private static int _noOfClients = 10;
	private static int _startYear = 2010;
	private static int _endYear = 2020;
	private static int _startNoOfDays = 100;
	private static int _endNoOfDays = 200;

	private static int _noOfClients_getall = 100;
	private static int _noOfClients_getfrom = 100;
	private static int _noOfClients_getto = 100;

	private static int _noOfClients_mod;


	private static void allocs() {

		try {
			_logger.info("[ALLOC]");

			CountDownLatch latch = new CountDownLatch(_noOfTimeseriess);

			_logger.info("Generating timeseries ... Start");
			Utils u = new Utils();
			List<String> timeseriess = new ArrayList<String>();
			for (int i=0; i < _noOfTimeseriess; i++) {

				int seedYear = Commons.randInt(_startYear, _endYear);
				String timeseries = u.generateTimeseries(seedYear, _startNoOfDays, _endNoOfDays);

				timeseriess.add(timeseries);
			}
			_logger.info("Generating timeseries ... End");

			ExecutorService execService = Executors.newFixedThreadPool(_noOfClients);

			Timer.start("allocs");
			_logger.info("Allocating timeseries ... Start");
			for (String timeseries : timeseriess) {

				String command = "ALLOC " + timeseries;
				execService.execute(new Thread(new Client(command, latch)));
			}

			latch.await();
			execService.shutdown();
			Timer.end("allocs");
			_logger.info("Allocating timeseries ... End");

			long timeTaken = Timer.get("allocs");

			_logger.info("ALLOC: " + timeTaken);

			_logger.info("[/ALLOC]");
		} 

		catch (InterruptedException ex) {
			_logger.error(ex);
		} 
		catch (PCacheException ex) {
			_logger.error(ex);
		}

	}

	private static void getAlls() {

		try {

			_logger.info("[GETALL_TB]");

			CountDownLatch latch = new CountDownLatch(_noOfClients_getall);
			ExecutorService executorService = Executors.newFixedThreadPool(_noOfClients_getall);

			Timer.start("getalls");
			for (int i=0; i<_noOfClients_getall; i++) {

				int ID = Commons.randInt(1, _noOfTimeseriess);
				String command = "GETALL " + ID;
				executorService.execute(new Thread(new Client(command, latch)));

			}

			latch.await();
			Timer.end("getalls");
			executorService.shutdown();
			_logger.info("GETALL: " + Timer.get("getalls"));

			_logger.info("[/GETALL_TB]");

		}

		catch (InterruptedException ex) {
			_logger.error(ex);
		} 
		catch (PCacheException e) {
			_logger.error(e);
		}

	}

	private static void deallocs() {

		try {

			_logger.info("[DEALLOCS_TB]");

			_noOfClients = 2000;

			CountDownLatch latch = new CountDownLatch(_noOfTimeseriess);
			ExecutorService executorService = Executors.newFixedThreadPool(_noOfClients);

			Timer.start("deallocs");

			for (int i=1; i<=_noOfTimeseriess; i++) {

				String command = "DEALLOC " + i;
				executorService.execute(new Thread(new Client(command, latch)));
			}

			latch.await();
			Timer.end("deallocs");
			executorService.shutdown();
			_logger.info("DEALLOCL: " + Timer.get("deallocs"));

			_logger.info("[/DEALLOCS_TB]");

		}

		catch (InterruptedException ex) {
			_logger.error(ex);
		} 
		catch (PCacheException e) {
			_logger.error(e);
		}

	}

	private static void getFroms() {

		try {

			_logger.info("[GETFROM_TB]");

			_logger.info("Generating timeseries ... Start");
			Utils u = new Utils();
			List<String> timeseriess = new ArrayList<String>();
			for (int i=0; i < _noOfTimeseriess; i++) {

				String timeseries = u.generateTimeseries(_startYear, _startNoOfDays, _endNoOfDays);

				timeseriess.add(timeseries);
			}
			_logger.info("Generating timeseries ... End");

			CountDownLatch latch = new CountDownLatch(_noOfTimeseriess);
			ExecutorService execService = Executors.newFixedThreadPool(_noOfClients_getfrom);

			_logger.info("Allocating timeseries ... Start");
			for (String timeseries : timeseriess) {

				String command = "ALLOC " + timeseries;
				execService.execute(new Thread(new Client(command, latch)));
			}

			latch.await();
			execService.shutdown();
			_logger.info("Allocating timeseries ... End");

			int lastID = 201;

			latch = new CountDownLatch(_noOfClients_getfrom);
			ExecutorService executorService = Executors.newFixedThreadPool(_noOfClients_getfrom);

			Timer.start("getfroms");
			for (int i=0; i<_noOfClients_getfrom; i++) {

				int ID = Commons.randInt(lastID, lastID + _noOfTimeseriess);
				int max = Commons.randInt(_startNoOfDays/2, _startNoOfDays);

				DateTime date = DateTime.parse(_startYear + "-01-01");
				date = date.plusDays(max);

				String dateISO8601 = date.toString("YYYY-MM-dd'T'HH:mm:ss.sssZZ");

				String command = "GETFROM " + ID + " " + dateISO8601;
				executorService.execute(new Thread(new Client(command, latch)));

			}

			latch.await();
			Timer.end("getfroms");
			executorService.shutdown();
			_logger.info("GETFROM: " + Timer.get("getfroms"));

			_logger.info("[/GETFROM_TB]");

		}

		catch (InterruptedException ex) {
			_logger.error(ex);
		} 
		catch (PCacheException e) {
			_logger.error(e);
		}

	}

	private static void getTos() {

		try {

			_logger.info("[GETTO_TB]");

			int lastID = 201;

			CountDownLatch latch = new CountDownLatch(_noOfClients_getto);
			ExecutorService executorService = Executors.newFixedThreadPool(_noOfClients_getto);

			Timer.start("gettos");
			for (int i=0; i<_noOfClients_getto; i++) {

				int ID = Commons.randInt(lastID, lastID + _noOfTimeseriess);
				int max = Commons.randInt(_startNoOfDays/2, _startNoOfDays);

				DateTime date = DateTime.parse(_startYear + "-01-01");
				date = date.plusDays(max);

				String dateISO8601 = date.toString("YYYY-MM-dd'T'HH:mm:ss.sssZZ");

				String command = "GETTO " + ID + " " + dateISO8601;
				executorService.execute(new Thread(new Client(command, latch)));

			}

			latch.await();
			Timer.end("gettos");
			executorService.shutdown();
			_logger.info("GETTO: " + Timer.get("gettos"));

			_logger.info("[/GETTO_TB]");

		}

		catch (InterruptedException ex) {
			_logger.error(ex);
		} 
		catch (PCacheException e) {
			_logger.error(e);
		}

	}
	
	private static void mods() {
		
		String[] valueOptions = {"UP","DOWN"};
		
		String newValue = valueOptions[Commons.randInt(0, 1)];
		
		try {

			_logger.info("[MOD_TB]");

			int lastID = 201;
			
			List<String> changeStrings = new ArrayList<String>();
			for (int i=0; i<_noOfClients_mod; i++) {
				
				String changeString = "";
				String changeStringTs = "";
				String changeStringData = "";
				
				int startTs = Commons.randInt(1, (int)(_startNoOfDays/2));
				int endTs = Commons.randInt((_startNoOfDays/2) + 1, _startNoOfDays);

				DateTime date = DateTime.parse(_startYear + "-01-01");
				DateTime startDate = date.plusDays(startTs);
				DateTime endDate = startDate.plusDays(endTs);
				
				for (DateTime d = startDate; d.isBefore(endDate); d = d.plusDays(1)) {
					
					changeStringTs += d.toString("YYYY-MM-dd'T'HH:mm:ss.sssZZ") + ",";
					changeStringData += newValue + ",";
				}
				
				changeStringTs = changeStringTs.substring(0, changeStringTs.length()-1);
				changeStringData = changeStringData.substring(0, changeStringData.length()-1);
				
				changeString += changeStringTs + " " + changeStringData;
				
				changeStrings.add(changeString);
			}
			
			CountDownLatch latch = new CountDownLatch(_noOfClients_mod);
			ExecutorService executorService = Executors.newFixedThreadPool(_noOfClients_mod);

			Timer.start("mods");
			for (int i=0; i<_noOfClients_mod; i++) {

				int ID = Commons.randInt(lastID, lastID + _noOfTimeseriess);

				String command = "MOD " + ID + " " + changeStrings.get(i);
				executorService.execute(new Thread(new Client(command, latch)));

			}

			latch.await();
			Timer.end("mods");
			executorService.shutdown();
			_logger.info("MOD: " + Timer.get("mods"));

			_logger.info("[/MOD_TB]");

		}

		catch (InterruptedException ex) {
			_logger.error(ex);
		} 
		catch (PCacheException e) {
			_logger.error(e);
		}
		
	}

	private static void initializeLogs() {

		PropertyConfigurator.configure("properties/log4j.properties");
		_logger = Logger.getLogger(TB2.class.getName());
	}

	private static void initializeProperties() {

		try {
			
			Properties propertyHandler = new Properties();
			
			propertyHandler.load(new FileInputStream("properties/tb2.properties"));

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
			
			_noOfClients_getall = Integer.parseInt(propertyHandler.getProperty(
					"GETALL_CLIENTS"));
			_noOfClients_getfrom = Integer.parseInt(propertyHandler.getProperty(
					"GETALL_CLIENTS"));
			_noOfClients_getto = Integer.parseInt(propertyHandler.getProperty(
					"GETALL_CLIENTS"));
			_noOfClients_mod = Integer.parseInt(propertyHandler.getProperty(
					"MOD_CLIENTS"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public static void main(String[] args) {

		initializeLogs();
		initializeProperties();

		_logger.info("Test bed ... Initialized");

		allocs();
		getAlls();
		deallocs();
		getFroms();
		getTos();
		mods();

		_logger.info("Test bed ... Done");

	}
}
