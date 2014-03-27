package com.pcache.DO;

import java.util.ArrayList;
import java.util.Date;

import com.pcache.exceptions.PCacheException;

/**
 * Data Object to store the list of timeseries in the cache
 * @author shrayas
 *
 */
public class Timeseries {

	private ArrayList<Date> _timestamps;
	private ArrayList<Object> _dataPoints;

	public int size() {
		return this._timestamps.size();
	}

	/**
	 * Constructor. Initialize a time series 
	 * @param timestamps the list of timestamps
	 * @param dataPoints the associated list of data points 
	 * @throws PCacheException thrown if different length arrays are provided
	 * 			for timestamps and dataPoints
	 */
	public Timeseries (ArrayList<Date> timestamps, 
			ArrayList<Object> dataPoints) throws PCacheException {

		if (timestamps.size() != dataPoints.size()) {
			throw new PCacheException("Timeseries data not correlated with"
					+ " data points provided. (Should be same length)");
		}
		
		this._timestamps = timestamps;
		this._dataPoints = dataPoints;
		
	}

}
