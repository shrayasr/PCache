package com.pcache.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.pcache.engines.VariableTimeseriesEngine;
import com.pcache.exceptions.PCacheException;

public class VariableTimeseriesEngineTests
{
	
	@Test (expected=PCacheException.class)
	public void test_allocateNewVarTs_invalidTimestamp() throws PCacheException
	{
		
		List<String> timestamps = new ArrayList<String>() {{
			add("20aa10-01-08T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
			add(1);
		}};
		
		long id1 = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
	}
	
	@Test (expected=PCacheException.class)
	public void test_allocateNewVarTs_nulls() throws PCacheException
	{
		List<String> timestamps = null;
		List<Object> dataPoints = null;
		
		long id1 = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
	}
	
	@Test (expected=PCacheException.class)
	public void test_allocateNewVarTs_unequal() throws PCacheException
	{
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			add("2010-01-09T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
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
		
		long id1 = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
	}
	
	@Test
	public void test_allocateNewVarTs_ok() throws PCacheException
	{
		List<String> timestamps = new ArrayList<String>() {{
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
		
		List<Object> dataPoints = new ArrayList<Object>() {{
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
		
		long id1 = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		long id2 = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		assertEquals(id1+1, id2);
	}
	
	@Test (expected=PCacheException.class)
	public void test_addPoints_unequal() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
			add(3);
		}};
		
		List<String> timestampsToAdd = new ArrayList<String>() {{
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
		}};
		
		List<Object> dataPointsToAdd = new ArrayList<Object>() {{
			add(3);
			add(1);
			add(2);
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		VariableTimeseriesEngine.addPoints(id, timestampsToAdd, dataPointsToAdd);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_addPoints_nulls() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
			add(3);
		}};
		
		List<String> timestampsToAdd = null;
		List<Object> dataPointsToAdd = null;
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		VariableTimeseriesEngine.addPoints(id, timestampsToAdd, dataPointsToAdd);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_addPoints_invalidTimestamp() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
			add(3);
		}};
		
		List<String> timestampsToAdd = new ArrayList<String>() {{
			add("2010-01-02T12:00:00.000+05:30");
			add("2asdf010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
		}};
		
		List<Object> dataPointsToAdd = new ArrayList<Object>() {{
			add(1);
			add(2);
			add(3);
			add(1);
			add(2);
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		int beforeAdd = VariableTimeseriesEngine.size(id);
		VariableTimeseriesEngine.addPoints(id, timestampsToAdd, dataPointsToAdd);
		int afterAdd = VariableTimeseriesEngine.size(id);
		
		assertEquals(beforeAdd+5, afterAdd);
		
	}
	
	@Test
	public void test_addPoints_ok() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
			add(3);
		}};
		
		List<String> timestampsToAdd = new ArrayList<String>() {{
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
		}};
		
		List<Object> dataPointsToAdd = new ArrayList<Object>() {{
			add(1);
			add(2);
			add(3);
			add(1);
			add(2);
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		int beforeAdd = VariableTimeseriesEngine.size(id);
		VariableTimeseriesEngine.addPoints(id, timestampsToAdd, dataPointsToAdd);
		int afterAdd = VariableTimeseriesEngine.size(id);
		
		assertEquals(beforeAdd+5, afterAdd);
		
	}
}
