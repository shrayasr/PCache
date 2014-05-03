package com.pcache.DO.timeseries;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.pcache.exceptions.PCacheException;

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
	
	/**
	 * Parse a tick string and return the no. of miliseconds in that tick.
	 * Eg: 1D is 1 Day. 1 Day contains 86400000 miliseconds.
	 * 
	 * @param tickStr the string representing the no. of ticks between timestamps
	 * 			in the array. 
	 * 
	 * 			The following units are acceptable:
	 * 				d:  Day,
	 * 				m:  Month,
	 * 				y:  Year,
	 * 				h:  Hour,
	 * 				M:  Minute,
	 * 				s:  Second.
	 * 
	 * @return no. of miliseconds in the tick string.
	 * @throws PCacheException 
	 */
	private long parseTickString(String tickStr) throws PCacheException {
			
		Pattern pattern = Pattern.compile("([0-9]+)([dmyHMs])$");
		Matcher matcher = pattern.matcher(tickStr);
		
		int tickDuration = 1;
		String tickUnit = "d";
		
		if (matcher.groupCount() != 2) {
			throw new PCacheException("Invalid format for tick");
		}
		
		if (matcher.matches()) {
			tickDuration = Integer.parseInt(matcher.group(1));
			tickUnit = matcher.group(2);
		}
		
		if (tickUnit.equals("m") || tickUnit.equals("y")) {
			throw new PCacheException("Tick value in months or years not " +
					"supported yet");
		}
		
		long milisInSecond = 1000;
		long milisInMinute = milisInSecond * 60;
		long milisInHour = milisInMinute * 60;
		long milisInDay = milisInHour * 24;
		
		switch(tickUnit) {
		
		case "s":
			return (tickDuration * milisInSecond);
			
		case "M":
			return (tickDuration * milisInMinute);
			
		case "h":
			return (tickDuration * milisInHour);
			
		case "d":
			return (tickDuration * milisInDay);
			
		default:
			throw new PCacheException("Tick format not supported");
		
		}
	}
	
}
