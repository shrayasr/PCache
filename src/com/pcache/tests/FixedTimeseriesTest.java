package com.pcache.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pcache.DO.timeseries.FixedTimeseries;
import com.pcache.exceptions.PCacheException;

public class FixedTimeseriesTest
{

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void test_constructor() throws PCacheException
	{
		
		ArrayList<String> timeseries = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			add("2010-01-11T12:00:00.000+05:30");
			add("2010-01-12T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Integer> dataPoints = new ArrayList<Integer>() {{
			
			add(1);
			add(2);
			add(3);
			add(1);
			add(2);
			add(3);
			add(1);
			add(2);
			add(3);
			add(3);
			
		}};
		
		FixedTimeseries<Integer> x = new FixedTimeseries<Integer>(timeseries, dataPoints, "1d", -1);
		
		assert true;
		
	}
	
	@Test
	public void test_addPoints() throws PCacheException {
		
		ArrayList<String> initialTimeseries = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Integer> initialDataPoints = new ArrayList<Integer>() {{
			
			add(1);
			add(2);
			add(3);
			add(1);
			
		}};
		
		FixedTimeseries<Integer> tsInt = new FixedTimeseries<Integer>(
				initialTimeseries, initialDataPoints, "1d", -1);
		
		ArrayList<String> toAppendTimeseries = new ArrayList<String>() {{
			
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			add("2010-01-11T12:00:00.000+05:30");
			add("2010-01-12T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Integer> toAppendDataPoints = new ArrayList<Integer>() {{
			
			add(2);
			add(3);
			add(1);
			add(2);
			add(3);
			add(3);
			
		}};
		
		int beforeAppend = tsInt.size();
		tsInt.addPoints(toAppendTimeseries, toAppendDataPoints);
		int afterAppend = tsInt.size();
		
		// adding a +2 to make room for the 2 days that were left out
		// in between. The fixedTS adds nulls in those places
		assertEquals((beforeAppend+6+2), afterAppend);
		
	}
	
	@Test
	public void test_removeFrom() throws PCacheException {
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Integer> dataPoints = new ArrayList<Integer>() {{
			
			add(1);
			add(2);
			add(3);
			add(1);
			
		}};
		
		FixedTimeseries<Integer> ts = new FixedTimeseries<Integer>(timestamps, dataPoints, "1d", -1);
		
		int beforeSize = ts.size();
		ts.removeFrom("2010-01-02T12:00:00.000+05:30");
		int afterSize = ts.size();
		
		assertEquals(beforeSize-3, afterSize);
		
	}
	
	@Test
	public void test_removeTill() throws PCacheException {
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Integer> dataPoints = new ArrayList<Integer>() {{
			
			add(1);
			add(2);
			add(3);
			add(1);
			
		}};
		
		FixedTimeseries<Integer> ts = new FixedTimeseries<Integer>(timestamps, dataPoints, "1d", -1);
		
		int beforeSize = ts.size();
		ts.removeTill("2010-01-02T12:00:00.000+05:30");
		int afterSize = ts.size();
		
		assertEquals(beforeSize-2, afterSize);
		
	}

}
