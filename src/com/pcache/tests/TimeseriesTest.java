package com.pcache.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pcache.DO.Timeseries;
import com.pcache.exceptions.PCacheException;

public class TimeseriesTest
{

	@Test
	public void testTimeseries() throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			
		}};
		
		ArrayList<String> dataPoints = new ArrayList<String>() {{
			
			add("UP");
			
		}};
		
		Timeseries<String> ts = new Timeseries<String>(timestamps, dataPoints);
		
	}

	@Test
	public void testTimeseriesRangeBetween() throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			add("2010-01-11T12:00:00.000+05:30");
			add("2010-01-23T12:00:00.000+05:30");
			add("2010-01-27T12:00:00.000+05:30");
			
		}};
		
		ArrayList<String> dataPoints = new ArrayList<String>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries<String> ts = new Timeseries<String>(timestamps, dataPoints);

		String timestampFrom = "2010-01-05T12:00:00.000+05:30";
		String timestampTo = "2010-01-20T12:00:00.000+05:30";
		

		Map<Long, String> tsSub = ts.rangeBetween(timestampFrom, timestampTo);

		assertEquals(3, tsSub.size());

	}

	@Test
	public void testTimeseriesRangeFrom() throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			add("2010-01-11T12:00:00.000+05:30");
			add("2010-01-23T12:00:00.000+05:30");
			add("2010-01-27T12:00:00.000+05:30");
			
		}};
		
		ArrayList<String> dataPoints = new ArrayList<String>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries<String> ts = new Timeseries<String>(timestamps, dataPoints);

		String timestampFrom = "2010-01-05T12:00:00.000+05:30";

		Map<Long, String> tsSub = ts.rangeFrom(timestampFrom);

		assertEquals(5, tsSub.size());

	}

	@Test
	public void testTimeseriesRangeTo() throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			add("2010-01-11T12:00:00.000+05:30");
			add("2010-01-23T12:00:00.000+05:30");
			add("2010-01-27T12:00:00.000+05:30");
			
		}};
		
		ArrayList<String> dataPoints = new ArrayList<String>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries<String> ts = new Timeseries<String>(timestamps, dataPoints);

		String timestampTo = "2010-01-20T12:00:00.000+05:30";
		
		Map<Long, String> tsSub = ts.rangeTo(timestampTo);

		assertEquals(6, tsSub.size());

	}

}
