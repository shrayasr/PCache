package com.pcache.DO;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.pcache.exceptions.PCacheException;

/**
 * The central class for storing timeseries
 *
 * @param <T> the Class associated to the data being stored in the timeseries
 */
public class Timeseries<T> {

	/**
	 * The main treemap to represent the timeseries.
	 * A treemap is favourable here because of the inbuilt ordering that comes
	 * along with it. This allows the points that are inserted later on to 
	 * find the right place. Also treemaps have a neat "submap" feature that
	 * allows to get a subset of a map
	 */
	private Map<Long, T> _timeseries;

	/**
	 * Get the size of the timeseries
	 * @return the size of the timeseries
	 */
	public int size() {
		return this._timeseries.size();
	}

	/**
	 * Add or Update points inside the timeseries. The nature of the TreeMap 
	 * allows us to do both of these things in the same call since the .put()
	 * function will replace an existsing value if it exists
	 * @param timestamps the set of timestamps to add/update
	 * @param dataPoints the associated set of data to the timestamps. 
	 * 			Note: there should be a one to one correlation between the
	 * 			timestamps and the data points
	 * @throws PCacheException thrown if there is no one to one correlation 
	 * 			between the timestamps and the data points
	 */
	public void addOrUpdatePoints(ArrayList<String> timestamps, 
			ArrayList<T> dataPoints) throws PCacheException{

		// If the 2 arraylists aren't of the same size, throw an exception
		if (timestamps.size() != dataPoints.size()) {
			throw new PCacheException("Sizes don't match. The number of data " +
					"points should equal the number of timestamps");
		}
		
		// Pick up a ISO date time formatter
		// the .dateTime() means that it should be in the 
		// ISO8601 format of YYYY-MM-DDTHH:MM:SS.SSS+Z
		DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
		
		// Go through all the timestamps
		for (int i=0; i<timestamps.size(); i++) {
			
			// Pick up the timestamp and the data point
			String timestampISO8601 = timestamps.get(i);
			T dataPoint = dataPoints.get(i);
			
			// Convert the timestamp to a UNIX time representation,
			// getting the no. of miliseconds elapsed since EPOC
			long milisSinceEpoc = formatter.parseDateTime(timestampISO8601)
					.getMillis();
			
			// Add or update the timestamp, datapoint
			// Put does updates also. so 2 birds, one stone!
			_timeseries.put(milisSinceEpoc, dataPoint);
			
		}

	}

	/**
	 * Remove a set of points from the timeseries
	 * @param timestamps the set of timestamps to remove
	 */
	public void removePoints(ArrayList<String> timestamps) {

		// Pick up a ISO date time formatter
		// the .dateTime() means that it should be in the 
		// ISO8601 format of YYYY-MM-DDTHH:MM:SS.SSS+Z
		DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
		
		// Go through all the timestamps
		for (int i=0; i<timestamps.size(); i++) {
			
			// Pick up the timestamp and the data point
			String timestampISO8601 = timestamps.get(i);
			
			// Convert the timestamp to a UNIX time representation,
			// getting the no. of miliseconds elapsed since EPOC
			long milisSinceEpoc = formatter.parseDateTime(timestampISO8601)
					.getMillis();
			
			_timeseries.remove(milisSinceEpoc);
			
		}

		
	}
	
	/**
	 * Get the set of points between 2 given timeseries'
	 * @param timestampFrom the ISO8601 timestamp representing the from
	 * @param timestampTo the ISO8601 timestamp representing the to
	 * @return a map of the timeseries - data representation for the given 
	 * 			range
	 */
	public Map<Long, T> getRangeBetween(String timestampFrom, 
			String timestampTo) {
		
		// Pick up a ISO8601 formatter
		DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
		
		// Convert from timestamp to miliseconds since EPOC
		long milisSinceEpocFrom = formatter.parseDateTime(timestampFrom)
				.getMillis();

		// Convert from timestamp to miliseconds since EPOC
		long milisSinceEpocTo = formatter.parseDateTime(timestampTo)
				.getMillis();

		// Return a map
		return ((TreeMap<Long, T>) this._timeseries).subMap(milisSinceEpocFrom, 
				true, milisSinceEpocTo, true);

	}

	/**
	 * Get the set of points from a given timestamp till the last one
	 * @param timestampFrom the ISO8601 timestamp representing the from
	 * @return a map of the timeseries - data representation for the given 
	 * 			range
	 */
	public Map<Long, T> getRangeFrom(String timestampFrom) {

		// Pick up a ISO8601 formatter
		DateTimeFormatter formatter = ISODateTimeFormat.dateTime();

		// Convert from timestamp to miliseconds since EPOC
		long milisSinceEpocFrom = formatter.parseDateTime(timestampFrom)
				.getMillis();

		// Get the last key in the series of timestamps
		long lastKey = ((TreeMap<Long, T>) this._timeseries).lastKey();

		// Return a map
		return ((TreeMap<Long, T>) this._timeseries).subMap(milisSinceEpocFrom, 
				true, lastKey, true);
		
	}

	/**
	 * Get the set of points from the first one till a given timestamp 
	 * @param timestampFrom the ISO8601 timestamp representing the from
	 * @return a map of the timeseries - data representation for the given 
	 * 			range
	 */
	public Map<Long, T> getRangeTo(String timestampTo) {

		// Pick up a ISO8601 formatter
		DateTimeFormatter formatter = ISODateTimeFormat.dateTime();

		// Convert from timestamp to miliseconds since EPOC
		long milisSinceEpocTo = formatter.parseDateTime(timestampTo)
				.getMillis();

		// Get the first key in the series of timestamps
		long firstKey = ((TreeMap<Long, T>) this._timeseries).firstKey();

		// Return a map
		return ((TreeMap<Long, T>) this._timeseries).subMap(firstKey, 
				true, milisSinceEpocTo, true);
		
	}

	/**
	 * Get the entire timeseries
	 * @return the entire timeseries map
	 */
	public Map<Long, T> getAll() {
		return this._timeseries;
	}
	
	/**
	 * Constructor. Initialize a time series. 
	 * @param timestamps an array of ISO8601 timestamps. The timestamps are to 
	 * 			be in ISO8601 format with miliseconds. 
	 * 			i.e. YYYY-MM-DDTHH:MM:SS.SSS+Z (2014-03-30T20:13:00.000+05:30)
	 * @param dataPoints the data points associated with the timestamps. They
	 * 			SHOULD have a one to one correlation.
	 * @throws PCacheException thrown if the no. of timestamps do not match
	 * 			the no. of data points
	 */
	public Timeseries (ArrayList<String> timestamps, 
			ArrayList<T> dataPoints) throws PCacheException {
		
		// Declare a new tree map
		_timeseries = new TreeMap<Long, T>();

		addOrUpdatePoints(timestamps, dataPoints);
		
	}

}
