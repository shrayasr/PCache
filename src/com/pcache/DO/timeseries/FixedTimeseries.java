package com.pcache.DO.timeseries;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.pcache.exceptions.PCacheException;

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
 * The storing of ticks gives us an advantage that we don't need to store the
 * timeseries data in itself. Storing the starting timestamp along with the 
 * ticks should be enough since seeking to a point would be a simple offset 
 * operation.
 * 
 * This also however means that for every kind of data, the NULL values might 
 * differ. We require a value that we prefill that will indicate to you that 
 * a particular data point is a NULL point.
 * 
 * @param <T> the Type of data to store
 */
public class FixedTimeseries<T>
{

	private long _startingTimestamp;
	private long _endingTimestamp;
	private long _tick;
	private T _null;
	private ArrayList<T> _dataPoints;
	
	public FixedTimeseries(ArrayList<String> timestamps, ArrayList<T> dataPoints,
			String tickStr, T nullValue) throws PCacheException {
		
		if (timestamps.size() != dataPoints.size()) {
			throw new PCacheException("Timestamps and datapoints should be of" +
					"the same length");
		}
		
		String startingTimestamp = timestamps.get(0);
		String endingTimestamp = timestamps.get(timestamps.size()-1);
		
		this._startingTimestamp = ISO8601toMilis(startingTimestamp);
		this._endingTimestamp = ISO8601toMilis(endingTimestamp);
		
		this._dataPoints = new ArrayList<>();
		this._tick = parseTickString(tickStr);
		this._null = nullValue;
		
		fillNULLs(this._startingTimestamp, this._endingTimestamp, this._null);
		fillPoints(timestamps, dataPoints);
	}
	
	private long ISO8601toMilis(String timestamp) {
		
		DateTimeFormatter ISO8601Formatter = ISODateTimeFormat.dateTime();
		return ISO8601Formatter.parseDateTime(timestamp).getMillis();
	}
	
	private void fillPoints(ArrayList<String> timestamps, ArrayList<T> dataPoints) {
		
		for (int i=0;i<timestamps.size();i++) {
			
			String timestamp = timestamps.get(i);
			T dataPoint = dataPoints.get(i);
			
			long timestampInMilis = ISO8601toMilis(timestamp);
			int index = (int)((timestampInMilis - this._startingTimestamp)
					/this._tick);
			
			this._dataPoints.set(index, dataPoint);
		}
		
	}
	
	private void fillNULLs(long from, long to, T nullValue) {
		
		long currentTimestamp = from;
		
		while (currentTimestamp <= to) {
			this._dataPoints.add(nullValue);
			currentTimestamp = currentTimestamp + this._tick;
		}
	}
	
}
