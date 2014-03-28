package com.pcache.DO;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.pcache.exceptions.PCacheException;

/**
 * Data Object to store the list of timeseries in the cache
 */
public class Timeseries<T> {

	private TreeMap<Long, T> _timeseries;

	public int size() {
		return this._timeseries.size();
	}
	
	public Timeseries (ArrayList<String> timestampsISO8601, 
			ArrayList<T> dataPoints) throws PCacheException {
		
		_timeseries = new TreeMap<Long, T>();
		
		if (timestampsISO8601.size() != dataPoints.size()) {
			throw new PCacheException("Sizes don't match. The number of data " +
					"points should equal the number of timestamps");
		}
		
		DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
		
		for (int i=0; i<timestampsISO8601.size(); i++) {
			
			String timestampISO8601 = timestampsISO8601.get(i);
			T dataPoint = dataPoints.get(i);
			
			long milisSinceEpoc = formatter.parseDateTime(timestampISO8601)
					.getMillis();
			
			_timeseries.put(milisSinceEpoc, dataPoint);
			
		}
		
	}

}
