package main.com.pcache.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.com.pcache.exceptions.PCacheException;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;


public class Commons
{

	public static long convertISO8601toMilis(String timestamp) 
			throws PCacheException {
		
		try {
			DateTimeFormatter ISO8601Formatter = ISODateTimeFormat.dateTime();
			return ISO8601Formatter.parseDateTime(timestamp).getMillis();
		}
		
		catch (NullPointerException ex) {
			throw new PCacheException("Timestamp can't be null", ex);
		}
		
		catch (IllegalArgumentException ex) {
			throw new PCacheException("Invalid timestamp format, Format is " +
					"restricted to ISO8601", ex);
		}
	}
	
	/**
	 * Convert the given timestamps to UNIX time representation (in miliseconds)
	 * @param timestamps the set of timestamps to convert
	 * @return a list of timestamps in miliseconds since EPOC format
	 */
	public static List<Long> convertISO8601toMilis(
			List<String> timestamps) throws PCacheException {

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
	
	public static int safeLongToInt(long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (l + " cannot be cast to int without changing its value.");
	    }
	    return (int) l;
	}
	
}
