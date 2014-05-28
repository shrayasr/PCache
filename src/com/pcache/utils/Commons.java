package com.pcache.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.pcache.exceptions.PCacheException;

public class Commons
{

	public static long ISO8601toMilis(String timestamp) {
		
		DateTimeFormatter ISO8601Formatter = ISODateTimeFormat.dateTime();
		return ISO8601Formatter.parseDateTime(timestamp).getMillis();
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
	
	public static int getOffset(long milis, long startingMilis, long tick) {
		return (safeLongToInt((milis - startingMilis) / tick));
	}
}
