package com.pcache.DO.timeseries;

import java.util.ArrayList;
import java.util.List;

import com.pcache.exceptions.PCacheException;
import com.pcache.utils.Commons;

/**
 * The FixedTimeseries class allows another take on handling timeseries data.
 * This is modelled on Arrays and hence inserts are a bit costlier but then 
 * selects are super fast. 
 * 
 * The only downside to having something like an array based representation is
 * the fact that one has to deal with gaps in the data and doing "between" 
 * operations takes up a lot of understanding. The current implementation will 
 * "try" to solve this problem by asking for the "ticks" from the user itself.
 * This will allow us to pre-fill the array with nulls so that the gaps are 
 * handled in a better fashion. This approach however isn't tested. There ought
 * to be places that i'm overlooking. 
 * 
 * The timeseries data is also being stored in a parallal array. The initial
 * implementation was to NOT store the timeseries but then if we don't store the
 * timeseries, then during gets, we will have to generate the timeseries which 
 * would become a O(n) operation. The real purpose of this class is to make the 
 * gets *extremely* fast. 
 * 
 * This means that for every kind of data, the NULL values might differ. We 
 * require a value that we prefill that will indicate to you that a particular 
 * data point is a NULL point.
 * 
 * The tick itself should be specified in a string format. The general syntax
 * for specifying a tick is <number><unit>
 * 
 * 	The following units are acceptable:
 * 		d:  Day,
 * 		m:  Month,
 * 		y:  Year,
 * 		h:  Hour,
 * 		M:  Minute,
 * 		s:  Second.
 * 
 * So, a tick can be declared as 1d, 2m, 1s, etc.
 * 
 * The timestamps are to be in the ISO8601 format. Anything that isn't of the
 * ISO8601 standard will not be put into the cache.
 * 
 * Note: This is a very user input dependent type of storing timeseries.If wrong
 * input is given, the user is responsible for it. He will get back wrong output.
 * 
 * @param <T> the Type of data to store
 */
public class FixedTimeseries<T>
{

	private long _tick;
	private T _null;
	
	private List<Long> _timestamps;
	private List<T> _dataPoints;
	
	/**
	 * Constructor. Initialize a fixed timeseries. 
	 * @param timestamps an arraylist of timestamps 
	 * @param dataPoints an arraylist of the data points
	 * @param tickStr a tick string representing the duration between 2 given
	 * 			points
	 * @param nullValue what is the value to be used to represent nulls?
	 * @throws PCacheException thrown if:
	 * 			* Timestamps and data points aren't of the same length
	 */
	public FixedTimeseries(ArrayList<String> timestamps, ArrayList<T> dataPoints,
			String tickStr, T nullValue) throws PCacheException {
		
		if (timestamps.size() != dataPoints.size()) {
			throw new PCacheException("Timestamps and datapoints should be of" +
					"the same length");
		}
		
		// Pick up the starting and ending time stamps
		String startingTimestamp = timestamps.get(0);
		String endingTimestamp = timestamps.get(timestamps.size()-1);
		
		// Convert them to Milis
		long startingTimestampMilis = Commons.convertISO8601toMilis(startingTimestamp);
		long endingTimestampMilis = Commons.convertISO8601toMilis(endingTimestamp);
		
		// Declare a new arraylist for holding data points and timestamps
		this._dataPoints = new ArrayList<>();
		this._timestamps = new ArrayList<>();
		
		// Get the tick, in milis by parsing the tick string as per the rules
		this._tick = Commons.parseTickString(tickStr);
		this._null = nullValue;
		
		// Fill it initially with NULL values
		_fillNULLs(startingTimestampMilis, endingTimestampMilis, this._null);
		
		// Fill in the points
		_fillPoints(timestamps, dataPoints);
	}
	
	/**
	 * Given the timestamps and data points, update the already NULL filled set
	 * of points with the actual data points
	 * @param timestamps Arraylist of timestamps that feature in the dataset
	 * @param dataPoints Arraylist of data points
	 */
	private void _fillPoints(ArrayList<String> timestamps, ArrayList<T> dataPoints) {
		
		long seriesStartingTimestampMilis = this._timestamps.get(0);
		
		// Go through the list of timestamps
		for (int i=0;i<timestamps.size();i++) {
			
			String timestamp = timestamps.get(i);
			T dataPoint = dataPoints.get(i);
			
			// Get the timestamps representation in milis
			long timestampInMilis = Commons.convertISO8601toMilis(timestamp);
			
			// Get the index of that timestamp
			int index = Commons.getOffset(timestampInMilis, 
					seriesStartingTimestampMilis, this._tick);
			
			// Set the data point at that location
			this._dataPoints.set(index, dataPoint);
		}
		
	}
	
	/**
	 * Given the from, to and NULL, fill the timeseries with NULLS
	 * @param from milisecond representation of the from point
	 * @param to milisecond representation of the to point
	 * @param nullValue the null value to use 
	 */
	private void _fillNULLs(long from, long to, T nullValue) {
		
		long currentTimestamp = from;
		
		while (currentTimestamp <= to) {
			this._timestamps.add(currentTimestamp);
			this._dataPoints.add(nullValue);
			currentTimestamp = currentTimestamp + this._tick;
		}
	}
	
	/**
	 * Add points to the given timeseries.
	 * 
	 * Note: Since it is an append only timeseries class, the first point of the
	 * 		 list of timestamps to be appended should be greater than what
	 * 		 already exists in the timeseries
	 * 
	 * @param timestamps the set of timestamps to append
	 * @param dataPoints the set of correlated data points to the timestamps
	 * @throws PCacheException thrown if:
	 * 			* Size of the timestamps and data points don't match
	 * 			* If the first point of the list of timestamps to be appended
	 * 			  isn't greater than what already exists
	 */
	public void addPoints(ArrayList<String> timestamps, ArrayList<T> dataPoints) 
			throws PCacheException {
		
		// Get the last timestamp of the series
		long seriesEndingTimestampMilis = this._timestamps.get(this._timestamps
				.size()-1);
		
		// Get the first timestamp in what needs to be added
		String startingTimestamp = timestamps.get(0);
		long startingTimestampMilis = Commons.convertISO8601toMilis(startingTimestamp);
		
		// what needs to be added should be greater than what exists
		if (startingTimestampMilis < seriesEndingTimestampMilis) {
			throw new PCacheException("New timeseries has the starting point" +
					" before the existing timeseries. Are you trying to insert" +
					" points? Insertions aren't supported in FixedTimeseries." +
					" Use variableTimeseries for that.");
		}
		
		// Get the last timestamp of what needs to be added
		String endingTimestamp = timestamps.get(timestamps.size()-1);
		long endingTimestampMilis = Commons.convertISO8601toMilis(endingTimestamp);
		
		// Fill with nulls and then fill points
		_fillNULLs(startingTimestampMilis, endingTimestampMilis, this._null);
		_fillPoints(timestamps, dataPoints);
	}
	
	/**
	 * Remove data points till a specified timestamp
	 * @param toTimestamp timestamp to remove till
	 * @throws PCacheException thrown if:
	 * 			* The timestamp to remove doesn't exist in the timeseries
	 */
	public void removeTill(String toTimestamp) throws PCacheException {
		
		// Get the timestamp of where the series starts
		long seriesStartingTimestamp = this._timestamps.get(0);
		
		// Get the timestamp of where the series needs to be truncated till 
		// in miliseconds
		long toMilis = Commons.convertISO8601toMilis(toTimestamp);
		
		/*
		 * Calculate the offset. 
		 * 
		 * Consider this structure: 
		 * [0] 2012-01-01
		 * [1] 2012-01-02
		 * [2] 2012-01-03
		 * [3] 2012-01-04
		 * [4] 2012-01-05
		 * [5] 2012-01-06
		 * 
		 * If i want to remove TILL 2012-01-04, the offset would be calculated
		 * as [3] so what needs to remain as part of the time series is from 
		 * [4] till the rest. 
		 * 
		 * This is why, after getting the offset, we add 1 
		 * 
		 */
		int offset  = Commons.getOffset(toMilis, seriesStartingTimestamp, 
				this._tick);
		offset = offset + 1;
		
		// If the offset ends up being greater than the size of the data points
		//  He's an idiot
		if (offset > this._dataPoints.size() || offset < 0) {
			throw new PCacheException("Timestamp doesn't exist");
		}
		
		/*
		 * Get a sublist FROM offset till the end of list, therby effectivly
		 * removing everything from 0 till the offset-1
		 * 
		 * NOTE:
		 *    The nature of the `subList` makes it so that we need to add 
		 *    1 to the size() since the 2nd parameter is exclusive and not
		 *    inclusive
		 */
		this._dataPoints = this._dataPoints.subList( offset, 
				this._dataPoints.size());
		
		this._timestamps = this._timestamps.subList(offset, 
				this._timestamps.size());
		
	}
	
	/**
	 * Remove data points from a specified timestamp till the end
	 * @param fromTimestamp timestamp to remove from
	 * @throws PCacheException thrown if:
	 * 			* The timestamp to remove doesn't exist in the timeseries
	 */
	public void removeFrom(String fromTimestamp) throws PCacheException {
		
		// Get the timestamp of where the series starts
		long seriesStartingTimestamp = this._timestamps.get(0);
		
		// Get the timestamp of where the series needs to be truncated from 
		// in miliseconds
		long fromMilis = Commons.convertISO8601toMilis(fromTimestamp);
		
		/*
		 * Calculate the offset. 
		 * 
		 * Consider this structure: 
		 * [0] 2012-01-01
		 * [1] 2012-01-02
		 * [2] 2012-01-03
		 * [3] 2012-01-04
		 * [4] 2012-01-05
		 * [5] 2012-01-06
		 * 
		 * If i want to remove FROM 2012-01-04, the offset would be calculated
		 * as [3] so what needs to remain as part of the time series is from 
		 * [0] till [2]
		 * 
		 * This is why, after getting the offset, we subtract 1 
		 * 
		 */
		int offset  = Commons.getOffset(fromMilis, seriesStartingTimestamp, 
				this._tick);
		offset = offset - 1;
		
		// If the offset ends up being greater than the size of the data points
		//  He's an idiot
		if (offset > this._dataPoints.size() || offset < 0) {
			throw new PCacheException("Timestamp doesn't exist");
		}
		
		/*
		 * Get a sublist FROM 0 till the offset position, therby effectivly
		 * removing everything from offset+1 till the end
		 * 
		 * NOTE:
		 *    The nature of the `subList` makes it so that we need to add 
		 *    1 to the offset since the 2nd parameter is exclusive and not
		 *    inclusive
		 */
		this._dataPoints = this._dataPoints.subList(0, (offset+1));
		this._timestamps = this._timestamps.subList(0, (offset+1));
	}
	
	/**
	 * Modify an point within the timeseries
	 * @param timestampToModify the timestamp of the point to modify
	 * @param newValue the new value for the point
	 * @throws PCacheException thrown if:
	 * 			* The timestamp to remove doesn't exist in the timeseries
	 */
	public void modifyPoint(String timestampToModify, T newValue) throws PCacheException {
		
		// Get the timestamp of where the series starts
		long seriesStartingTimestamp = this._timestamps.get(0);
		
		// All dat timestamp conversion shizbomb
		// Get the timestamp to modify the value of in miliseconds
		long timestampToModifyInMilis = Commons.convertISO8601toMilis(timestampToModify);
		
		// Get its offset
		int offset = Commons.getOffset(timestampToModifyInMilis, 
				seriesStartingTimestamp, this._tick);
		
		// If the offset calculated is more than the no. of data points
		// He's an idiot
		if (offset > this._dataPoints.size() || offset < 0) {
			throw new PCacheException("Timestamp doesn't exist");
		}
		
		// Update the value at that offset
		this._dataPoints.set(offset, newValue);
		
	}
	
	/**
	 * Return the size of the timeseries. 
	 * 
	 * This isn't really the size of the data points in the timeseries but the 
	 * total no. of points in the timeseries. This means that it accounts for
	 * the points that are missed out in what was provided. 
	 * 
	 * Eg:
	 * 	If the initial set contained points for the following days:
	 * 			Mon - Tue - Wed - Thu - Fri - Mon - Tue
	 * 	i.e. missing out the Sat and Sun.
	 * 
	 *  The Cache fills those 2 points with null values. So even though the
	 *  effective size of the cache would only be 7 points. The size would
	 *  return 7 + 2 = 9 points since it accounts for the missed out parts as
	 *  well
	 *  
	 * @return the "effective" size of the cache
	 */
	public int size() {
		return this._dataPoints.size();
	}
	
}
