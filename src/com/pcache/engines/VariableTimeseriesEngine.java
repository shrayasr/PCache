package com.pcache.engines;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pcache.DO.timeseries.VariableTimeseries;
import com.pcache.exceptions.PCacheException;

/**
 * Class to hold the list of variable time series' in the system. 
 * 
 * Essentially, this is a simple static class whos base data structure is a 
 * map between a unique ID (Got from the IDEngine) and the variable timeseries 
 * associated to that ID.
 * 
 * The timestamps are to be in the ISO8601 format. Anything that isn't of the
 * ISO8601 standard will not be put into the cache.
 *
 */
public class VariableTimeseriesEngine
{
	// Map to hold the mapping
	private static Map<Long, VariableTimeseries> _idVarTsMap;

	// Static block to initialize the map
	static {
		_idVarTsMap = new HashMap<>();
	}
	
	/**
	 * Create a new timeseries and allocate it an ID 
	 * @param timestamps The list of timestamps to create
	 * @param dataPoints The list of dataPoints associated with the timeseries
	 * @return an ID that can be used to refer to the timeseries
	 * @throws PCacheException thrown if:
	 * 			* Lengths are unequal
	 * 			* Nulls
	 */
	public static long allocate(List<String> timestamps, 
			List<String> dataPoints) throws PCacheException {

		// Create a new variable timeseries with the given set of data
		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);

		// Fetch a new ID
		long id = IDEngine.generateID();

		// Add it to the map, return the ID
		_idVarTsMap.put(id, ts);
		return id;
	}

	/**
	 * Add points to a given timeseries
	 * @param id the Identifier of the timeseries to add points into
	 * @param timestamps the set of timestamps to add
	 * @param dataPoints the set of data points associated to the timeseries to
	 * 			add
	 * @throws PCacheException thrown if
	 * 			* Lengths are unequal
	 * 			* Nulls
	 * 			* ID passed doesn't exist
	 * 			* If points passed already exist
	 */
	public static void addPoints(long id, 
			List<String> timestamps, List<String> dataPoints) 
					throws PCacheException {

		// Sanity checks
		_exceptIfInvalidId(id);

		_idVarTsMap.get(id).addPoints(timestamps, dataPoints);

	}

	/**
	 * Modify points in a given timeseries
	 * @param id the Identifier of the timeseries to modify points from
	 * @param timestampsToModify the timestamps to modify
	 * @param newDataPoints the new values for the associated timestamps
	 * @throws PCacheException thrown if:
	 * 			* Lengths are unequal
	 * 			* Nulls
	 * 			* ID passed doesn't exist
	 * 			* If points passed don't exist
	 */
	public static void modifyPoints(long id,
			List<String> timestampsToModify, List<String> newDataPoints) 
					throws PCacheException {

		// Sanity checks
		_exceptIfInvalidId(id);

		_idVarTsMap.get(id).updatePoints(timestampsToModify, newDataPoints);

	}

	/**
	 * Remove points from a given timeseries
	 * @param id the Identifier of the timeseries to remove points from
	 * @param timestampsToRemove the timestamps to remove
	 * @throws PCacheException thrown if:
	 * 			* Nulls
	 * 			* ID passed doesn't exist
	 * 			* If points passed don't exist
	 */
	public static void removePoints(long id, List<String> timestampsToRemove) 
			throws PCacheException {

		// Sanity checks
		_exceptIfInvalidId(id);

		_idVarTsMap.get(id).removePoints(timestampsToRemove);
	}
	
	public static String get(long id, String timestamp) throws PCacheException {
		
		// Sanity Checks
		_exceptIfInvalidId(id);
		
		return null;
	}

	/**
	 * Get the list of ALL points in a given timeseries
	 * @param id the Identifier of the timeseries 
	 * @return the map between the timestamp and the object
	 * @throws PCacheException thrown if:
	 * 			* ID passed doesn't exist
	 */
	public static VariableTimeseries getAll(long id) throws PCacheException {

		// Sanity checks
		_exceptIfInvalidId(id);

		return _idVarTsMap.get(id).getAll();
	}

	/**
	 * Get a list of ALL points FROM a given timestamp in a given timeseries
	 * @param id the Identifier of the timeseries
	 * @param timestampFrom the timestamp to fetch points FROM
	 * @return the map between the timestamp and the object FROM that particular
	 * 			timestamp. INCLUDES that timestamp
	 * @throws PCacheException thrown if:
	 * 			* ID passed doesn't exist
	 * 			* Nulls
	 * 			* Timestamp isn't in ISO8601 format
	 */
	public static VariableTimeseries getFrom(long id, String timestampFrom) 
			throws PCacheException {

		// Sanity checks
		_exceptIfInvalidId(id);

		return _idVarTsMap.get(id).getRangeFrom(timestampFrom);
	}

	/**
	 * Get a list of ALL points TILL a given timestamp in a given timeseries
	 * @param id the Identifier of the timeseries
	 * @param timestampFrom the timestamp to fetch points TILL
	 * @return the map between the timestamp and the object TILL that particular
	 * 			timestamp. INCLUDES that timestamp
	 * @throws PCacheException thrown if:
	 * 			* ID passed doesn't exist
	 * 			* Nulls
	 * 			* Timestamp isn't in ISO8601 format
	 */
	public static VariableTimeseries getTo(long id, String timestampTo) 
			throws PCacheException {
		
		// Sanity checks
		_exceptIfInvalidId(id);
		
		return _idVarTsMap.get(id).getRangeTo(timestampTo);
	}

	/**
	 * Return the size of the timeseries
	 * @param id the Identifier of the timeseries
	 * @return the size of the timeseries
	 * @throws PCacheException thrown if:
	 * 			* Nulls
	 * 			* ID passed doesn't exist
	 */
	public static int size(long id) throws PCacheException {

		// Sanity checks
		_exceptIfInvalidId(id);

		return _idVarTsMap.get(id).size();
	}

	/**
	 * Throw an exception if the ID passed doesn't exist in the map
	 * @param id the Identifier of the timeseries
	 * @throws PCacheException thrown if:
	 * 			* ID passed doesn't exist
	 */
	private static void _exceptIfInvalidId(long id) throws PCacheException {

		if (_idVarTsMap.get(id) == null) {
			throw new PCacheException("That ID doesn't exist anymore in the " +
					"list of timeseries'. Maybe it is already deleted?");
		}

	}
}
