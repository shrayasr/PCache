package com.pcache.DO.timeseries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.pcache.exceptions.PCacheException;
import com.pcache.utils.Commons;

/**
 * The central class for storing timeseries
 */
public class VariableTimeseries {

	/**
	 * The main treemap to represent the timeseries.
	 * A treemap is favourable here because of the inbuilt ordering that comes
	 * along with it. This allows the points that are inserted later on to 
	 * find the right place. Also treemaps have a neat "submap" feature that
	 * allows to get a subset of a map
	 */
	private Map<Long, String> _timeseries;
	
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
	public VariableTimeseries (List<String> timestamps, 
			List<String> dataPoints) throws PCacheException {

		// Sanity Checks
		_exceptIfLengthUnequal(timestamps, dataPoints);
		
		// Declare a new tree map
		_timeseries = new TreeMap<Long, String>();

		_addOrUpdatePoints(timestamps, dataPoints);

	}
	
	/**
	 * Constructor. Create a variable timeseries out of an existing set
	 * @param timeseries the Long - string mapping to create the 
	 * 			variabletimeseries out of
	 */
	public VariableTimeseries(Map<Long, String> timeseries) {
		this._timeseries = timeseries;
	}

	/**
	 * Get the size of the timeseries
	 * @return the size of the timeseries
	 */
	public int size() {
		return this._timeseries.size();
	}

	/**
	 * Check if a given timestamp exists within the series
	 * @param timestamp the timestamp to check for
	 * @return true/false based on its existance
	 * @throws PCacheException 
	 */
	public boolean contains(String timestamp) throws PCacheException {

		// Convert the timestamp to a UNIX time representation,
		// getting the no. of miliseconds elapsed since EPOC
		long milisSinceEpoc  = Commons.convertISO8601toMilis(timestamp);

		return this._timeseries.containsKey(milisSinceEpoc);
	}

	/**
	 * A core procedure. This isn't called from the outside. 
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
	private void _addOrUpdatePoints(List<String> timestamps, 
			List<String> dataPoints) throws PCacheException{

		try {

			// Go through all the timestamps
			for (int i=0; i<timestamps.size(); i++) {

				// Pick up the timestamp and the data point
				String timestampISO8601 = timestamps.get(i);
				String dataPoint = dataPoints.get(i);

				// Convert the timestamp to a UNIX time representation,
				// getting the no. of miliseconds elapsed since EPOC
				long milisSinceEpoc = Commons.convertISO8601toMilis(timestampISO8601);

				// Add or update the timestamp, datapoint
				// Put does updates also. so 2 birds, one stone!
				_timeseries.put(milisSinceEpoc, dataPoint);

			}
		}

		catch (NullPointerException ex) {
			throw new PCacheException("Timestamps and dataPoints should not be " +
					"null", ex);
		}

	}

	/**
	 * Add points to the timeseries
	 * @param timestamps the set of timestamps to add
	 * @param dataPoints the associated set of data points to add
	 * @throws PCacheException thrown if timeseries isn't associated to the
	 * 			datapoints or if the points already exist in the timeseries
	 */
	public void addPoints(List<String> timestamps,
			List<String> dataPoints) throws PCacheException {

		// Sanity checks
		_exceptIfLengthUnequal(timestamps, dataPoints);
		_exceptIfPointsExist(timestamps);

		// Call the core procedure to add points into the timeseries
		_addOrUpdatePoints(timestamps, dataPoints);

	}

	/**
	 * Update points in the timeseries
	 * @param timestamps the set of timestamps to update
	 * @param dataPoints the associated set of data points to update
	 * @throws PCacheException thrown if timeseries isn't associated to the
	 * 			datapoints or if the points don't exist in the timeseries
	 */
	public void updatePoints(List<String> timestamps,
			List<String> dataPoints) throws PCacheException {

		// Sanity checks
		_exceptIfLengthUnequal(timestamps, dataPoints);
		_exceptIfNoPointsExist(timestamps);

		// Call the core procedure to update points in the timeseries
		_addOrUpdatePoints(timestamps, dataPoints);

	}

	/**
	 * Remove a set of points from the timeseries
	 * @param timestamps the set of timestamps to remove
	 * @throws PCacheException thrown if one or more points specified to be 
	 * 			deleted, doesn't exist
	 */
	public void removePoints(List<String> timestamps) 
			throws PCacheException {

		// Sanity Checks
		_exceptIfNoPointsExist(timestamps);

		List<Long> timestampsSinceEpoc = 
				Commons.convertISO8601toMilis(timestamps);

		for (long timestamp : timestampsSinceEpoc) {
			this._timeseries.remove(timestamp);
		}

	}

	/**
	 * Get the set of points between 2 given timeseries'
	 * @param timestampFrom the ISO8601 timestamp representing the from
	 * @param timestampTo the ISO8601 timestamp representing the to
	 * @return a map of the timeseries - data representation for the given 
	 * 			range
	 * @throws PCacheException 
	 */
	public VariableTimeseries getRangeBetween(String timestampFrom, 
			String timestampTo) throws PCacheException {

		// Convert from timestamp to miliseconds since EPOC
		long milisSinceEpocFrom = Commons.convertISO8601toMilis(timestampFrom);

		// Convert from timestamp to miliseconds since EPOC
		long milisSinceEpocTo = Commons.convertISO8601toMilis(timestampTo);

		// Return a timeseries
		return new VariableTimeseries(((TreeMap<Long, String>) this._timeseries)
				.subMap(milisSinceEpocFrom, true, milisSinceEpocTo, true));

	}

	/**
	 * Get the set of points from a given timestamp till the last one
	 * @param timestampFrom the ISO8601 timestamp representing the from
	 * @return a map of the timeseries - data representation for the given 
	 * 			range
	 * @throws PCacheException 
	 */
	public VariableTimeseries getRangeFrom(String timestampFrom) 
			throws PCacheException {

		// Convert from timestamp to miliseconds since EPOC
		long milisSinceEpocFrom = Commons.convertISO8601toMilis(timestampFrom);

		// Get the last key in the series of timestamps
		long lastKey = ((TreeMap<Long, String>) this._timeseries).lastKey();

		try {
		// Return a map
		return new VariableTimeseries(((TreeMap<Long, String>) this._timeseries)
				.subMap(milisSinceEpocFrom, true, lastKey, true));
		}
		
		catch (IllegalArgumentException ex) {
			throw new PCacheException("Timestamp provided exceeds the max " +
					"timestamp stored in the cache.", ex);
		}

	}

	/**
	 * Get the set of points from the first one till a given timestamp 
	 * @param timestampFrom the ISO8601 timestamp representing the from
	 * @return a map of the timeseries - data representation for the given 
	 * 			range
	 * @throws PCacheException 
	 */
	public VariableTimeseries getRangeTo(String timestampTo) 
			throws PCacheException {

		// Convert from timestamp to miliseconds since EPOC
		long milisSinceEpocTo = Commons.convertISO8601toMilis(timestampTo);

		// Get the first key in the series of timestamps
		long firstKey = ((TreeMap<Long, String>) this._timeseries).firstKey();

		try {
		// Return a map
		return new VariableTimeseries(((TreeMap<Long, String>) this._timeseries)
				.subMap(firstKey, true, milisSinceEpocTo, true));
		}
		
		catch (IllegalArgumentException ex) {
			throw new PCacheException("Timestamp provided preceeds the min " +
					"timestamp stored in the cache.", ex);
		}

	}

	/**
	 * Get the entire timeseries
	 * @return the entire timeseries map
	 */
	public VariableTimeseries getAll() {
		return this;
	}
	
	/**
	 * Return a particular timestamps value
	 * @param timestamp the timestamp to look for in the map
	 * @return the value for that timestamp, null if the timestamp isn't present
	 * @throws PCacheException thrown if:
	 * 			* timestamp not in ISO8601 format
	 */
	public String getOne(String timestamp) throws PCacheException {
		
		long milis = Commons.convertISO8601toMilis(timestamp);
		return this._timeseries.get(milis);
	}
	
	/**
	 * Convert the current timeseries to JSON format with the key as the 
	 * ISO8601 formatted timestamp 
	 * @return the JSON
	 */
	public String toJson() {
		
		Map<String, String> jsonMap = new TreeMap<String, String>();
		
		for (Entry<Long, String> entry : this._timeseries.entrySet())
		{
		   DateTime d = new DateTime(entry.getKey()); 
		   jsonMap.put(d.toString(), entry.getValue());
		}
		
		final Gson gson = new Gson();
		return gson.toJson(jsonMap);
		
	}


	/**
	 * Check if the length of the timeseries is not equal to the length of the
	 * data points that it is associated with 
	 * @param timestamps the set of timestamps
	 * @param dataPoints the associated set of data points
	 * @throws PCacheException thrown if the length of both are unequal
	 */
	private void _exceptIfLengthUnequal(List<String> timestamps,
			List<String> dataPoints) throws PCacheException {

		try {
			if (timestamps.size() != dataPoints.size()) {
				throw new PCacheException("Sizes don't match. The number of data " +
						"points should equal the number of timestamps");
			}
		}

		catch (NullPointerException ex) {
			throw new PCacheException("Timestamps and dataPoints should not be " +
					"null", ex);
		}

		catch (IllegalArgumentException ex) {
			throw new PCacheException("One or more of the timestamps isn't in " +
					"ISO8601 format", ex);
		}


	}

	/**
	 * Check if the set of points already exist in the cache
	 * @param timestamps the set of timestamps to check
	 * @throws PCacheException thrown if the set of points already exist in the
	 * 			cache
	 */
	private void _exceptIfPointsExist(List<String> timestamps) 
			throws PCacheException {

		try {
			// Get the EPOC representations
			List<Long> timestampsInMilis = 
					Commons.convertISO8601toMilis(timestamps);

			// Go through the timestamps
			for (long timestamp : timestampsInMilis) {

				// If timeseries already contains it, except
				if (this._timeseries.containsKey(timestamp)) {
					throw new PCacheException("Some point(s) already exist in the "
							+ "timeseries");
				}

			}
		}

		catch (NullPointerException ex) {
			throw new PCacheException("Timestamps and dataPoints should not be " +
					"null", ex);
		}

		catch (IllegalArgumentException ex) {
			throw new PCacheException("One or more of the timestamps isn't in " +
					"ISO8601 format", ex);
		}

	}

	/**
	 * Check if the set of points don't exist in the cache
	 * @param timestamps the set of timestamps to check
	 * @throws PCacheException thrown if the set of points don't exist in the
	 * 			cache
	 */
	private void _exceptIfNoPointsExist(List<String> timestamps) 
			throws PCacheException {

		try {
			// Get the EPOC representations
			List<Long> timestampsInMilis = 
					Commons.convertISO8601toMilis(timestamps);

			// Go through the timestamps
			for (long timestamp : timestampsInMilis) {

				// If the timeseries doesn't contain it, except
				if (!this._timeseries.containsKey(timestamp)) {
					throw new PCacheException("Some point(s) don't exist in the "
							+ "timeseries");
				}

			}
		}

		catch (NullPointerException ex) {
			throw new PCacheException("Timestamps and dataPoints should not be " +
					"null", ex);
		}

		catch (IllegalArgumentException ex) {
			throw new PCacheException("One or more of the timestamps isn't in " +
					"ISO8601 format", ex);
		}

	}

}
