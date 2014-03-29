package com.pcache.DO;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.pcache.exceptions.PCacheException;

/**
 * Data Object to store the list of timeseries in the cache
 */
public class Timeseries<T> {

	private Map<Long, T> _timeseries;

	public int size() {
		return this._timeseries.size();
	}
	
	public Map<Long, T> rangeBetween(String timestampFrom, 
			String timestampTo) {
		
		DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
		
		long milisSinceEpocFrom = formatter.parseDateTime(timestampFrom)
				.getMillis();

		long milisSinceEpocTo = formatter.parseDateTime(timestampTo)
				.getMillis();

		return ((TreeMap<Long, T>) this._timeseries).subMap(milisSinceEpocFrom, 
				true, milisSinceEpocTo, true);

	}

	public Map<Long, T> rangeFrom(String timestampFrom) {

		DateTimeFormatter formatter = ISODateTimeFormat.dateTime();

		long milisSinceEpocFrom = formatter.parseDateTime(timestampFrom)
				.getMillis();

		long lastKey = ((TreeMap<Long, T>) this._timeseries).lastKey();

		return ((TreeMap<Long, T>) this._timeseries).subMap(milisSinceEpocFrom, 
				true, lastKey, true);
		
	}

	public Map<Long, T> rangeTo(String timestampTo) {

		DateTimeFormatter formatter = ISODateTimeFormat.dateTime();

		long milisSinceEpocTo = formatter.parseDateTime(timestampTo)
				.getMillis();

		long firstKey = ((TreeMap<Long, T>) this._timeseries).firstKey();

		return ((TreeMap<Long, T>) this._timeseries).subMap(firstKey, 
				true, milisSinceEpocTo, true);
		
	}
	
	public Timeseries (ArrayList<String> timestamps, 
			ArrayList<T> dataPoints) throws PCacheException {
		
		_timeseries = new TreeMap<Long, T>();
		
		if (timestamps.size() != dataPoints.size()) {
			throw new PCacheException("Sizes don't match. The number of data " +
					"points should equal the number of timestamps");
		}
		
		DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
		
		for (int i=0; i<timestamps.size(); i++) {
			
			String timestampISO8601 = timestamps.get(i);
			T dataPoint = dataPoints.get(i);
			
			long milisSinceEpoc = formatter.parseDateTime(timestampISO8601)
					.getMillis();
			
			_timeseries.put(milisSinceEpoc, dataPoint);
			
		}
		
	}

}
