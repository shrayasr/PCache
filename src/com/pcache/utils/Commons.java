package com.pcache.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.pcache.exceptions.PCacheException;

public class Commons
{

	public static long convertISO8601toMilis(String timestamp) 
			throws IllegalArgumentException {
		
		DateTimeFormatter ISO8601Formatter = ISODateTimeFormat.dateTime();
		return ISO8601Formatter.parseDateTime(timestamp).getMillis();
	}
	
	/**
	 * Convert the given timestamps to UNIX time representation (in miliseconds)
	 * @param timestamps the set of timestamps to convert
	 * @return a list of timestamps in miliseconds since EPOC format
	 */
	public static List<Long> convertISO8601toMilis(
			List<String> timestamps) {

		List<Long> timestampsSinceEpoc = new ArrayList<>();

		// Go through all the timestamps
		for (int i=0; i<timestamps.size(); i++) {

			// Pick up the timestamp 
			String timestampISO8601 = timestamps.get(i);

			// Convert the timestamp to a UNIX time representation,
			// getting the no. of miliseconds elapsed since EPOC
			long milisSinceEpoc = Commons.convertISO8601toMilis(
					timestampISO8601);

			timestampsSinceEpoc.add(milisSinceEpoc);
		}

		return timestampsSinceEpoc;

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
	public static long parseTickString(String tickStr) throws PCacheException {
			
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
	
	public static int safeLongToInt(long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (l + " cannot be cast to int without changing its value.");
	    }
	    return (int) l;
	}
	
	/**
	 * Calculate the index to offset. 
	 * 
	 * It is calculated as (timestamp - starting_timestamp) / tick 
	 * 
	 * Eg:
	 * 		starting_timestamp = Jan 1 2013 = 1356998400
	 * 		timestamp = Jan 5 2014 = 1357344000
	 * 		tick = 1d = no. of milis in a day = 86400
	 * 
	 * 		Index of Jan 5th = (1357430400 - 1356998400) / 86400
	 * 						 = 4
	 * 
	 * @param timestamp the timestamp to calculate the offset for
	 * @param startime_timestamp the starting timestamp in the series
	 * @param tick the tick value between timestamps
	 * @return the offset into the array for that timestamp
	 */
	public static int getOffset(long timestamp, long startime_timestamp, 
			long tick) {
		
		return (safeLongToInt((timestamp - startime_timestamp) / tick));
	}
}
