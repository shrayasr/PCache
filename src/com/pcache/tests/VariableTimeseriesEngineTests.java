package com.pcache.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	@Test (expected = PCacheException.class)
	public void test_addPoints_timestampExists() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
			add(3);
		}};
		
		List<String> timestampsToAdd = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<Object> dataPointsToAdd = new ArrayList<Object>() {{
			add(1);
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
	
	@Test (expected=PCacheException.class)
	public void test_modifyPoints_timestampNoExist() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
			add(3);
		}};
		
		List<String> timestampsToModify = new ArrayList<String>() {{
			add("2012-01-01T12:00:00.000+05:30");
		}};
		
		List<Object> dataPointsToModify = new ArrayList<Object>() {{
			add(3);
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		VariableTimeseriesEngine.modifyPoints(id, timestampsToModify, 
				dataPointsToModify);
		
		Object val = VariableTimeseriesEngine.getAll(id)
				.get("2010-01-01T12:00:00.000+05:30");
		
		assertEquals(val, 2);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_modifyPoints_invalidTimestamp() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
			add(3);
		}};
		
		List<String> timestampsToModify = new ArrayList<String>() {{
			add("asdf2010-01-01T12:00:00.000+05:30");
		}};
		
		List<Object> dataPointsToModify = new ArrayList<Object>() {{
			add(3);
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		VariableTimeseriesEngine.modifyPoints(id, timestampsToModify, 
				dataPointsToModify);
		
		Object val = VariableTimeseriesEngine.getAll(id)
				.get("2010-01-01T12:00:00.000+05:30");
		
		assertEquals(val, 2);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_modifyPoints_unequal() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
			add(3);
		}};
		
		List<String> timestampsToModify = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<Object> dataPointsToModify = new ArrayList<Object>() {{
			add(2);
			add(3);
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		VariableTimeseriesEngine.modifyPoints(id, timestampsToModify, 
				dataPointsToModify);
		
		Object val = VariableTimeseriesEngine.getAll(id)
				.get("2010-01-01T12:00:00.000+05:30");
		
		assertEquals(val, 2);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_modifyPoints_null() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
			add(3);
		}};
		
		List<String> timestampsToModify = null;
		List<Object> dataPointsToModify = null;
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		VariableTimeseriesEngine.modifyPoints(id, timestampsToModify, 
				dataPointsToModify);
		
		Object val = VariableTimeseriesEngine.getAll(id)
				.get("2010-01-01T12:00:00.000+05:30");
		
		assertEquals(val, 2);
		
	}
	
	public void test_modifyPoints_ok() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
			add(3);
		}};
		
		List<String> timestampsToModify = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<Object> dataPointsToModify = new ArrayList<Object>() {{
			add(2);
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		VariableTimeseriesEngine.modifyPoints(id, timestampsToModify, 
				dataPointsToModify);
		
		Object val = VariableTimeseriesEngine.getAll(id)
				.get("2010-01-01T12:00:00.000+05:30");
		
		assertEquals(val, 2);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_removePoints_timeseriesNoExist() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
			add(3);
			add(4);
		}};
		
		List<String> timestampsToRemove = new ArrayList<String>() {{
			add("2010-01-03T12:00:00.000+05:30");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		int sizeBeforeRemove = VariableTimeseriesEngine.size(id);
		VariableTimeseriesEngine.removePoints(id, timestampsToRemove);
		int sizeAfterRemove = VariableTimeseriesEngine.size(id);
		
		assertEquals(sizeBeforeRemove-1, sizeAfterRemove);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_removePoints_invalidTimeseries() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
			add(3);
			add(4);
		}};
		
		List<String> timestampsToRemove = new ArrayList<String>() {{
			add("asdf2010-01-02T12:00:00.000+05:30");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		int sizeBeforeRemove = VariableTimeseriesEngine.size(id);
		VariableTimeseriesEngine.removePoints(id, timestampsToRemove);
		int sizeAfterRemove = VariableTimeseriesEngine.size(id);
		
		assertEquals(sizeBeforeRemove-1, sizeAfterRemove);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_removePoints_nulls() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
			add(3);
			add(4);
		}};
		
		List<String> timestampsToRemove = null;
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		int sizeBeforeRemove = VariableTimeseriesEngine.size(id);
		VariableTimeseriesEngine.removePoints(id, timestampsToRemove);
		int sizeAfterRemove = VariableTimeseriesEngine.size(id);
		
		assertEquals(sizeBeforeRemove-1, sizeAfterRemove);
		
	}
	
	@Test
	public void test_removePoints_ok() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
			add(3);
			add(4);
		}};
		
		List<String> timestampsToRemove = new ArrayList<String>() {{
			add("2010-01-02T12:00:00.000+05:30");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		int sizeBeforeRemove = VariableTimeseriesEngine.size(id);
		VariableTimeseriesEngine.removePoints(id, timestampsToRemove);
		int sizeAfterRemove = VariableTimeseriesEngine.size(id);
		
		assertEquals(sizeBeforeRemove-1, sizeAfterRemove);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_getAll_wrongId() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
			add(3);
			add(4);
		}};
		
		Map<Long, Object> timeseriesExpected = new HashMap<Long, Object>() {{
			put(1262327400000L,3);
			put(1262413800000L,4);
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		Map<Long, Object> timeseriesActual = VariableTimeseriesEngine
				.getAll(12341L);
		
		assertEquals(timeseriesExpected, timeseriesActual);
		
	}
	
	@Test
	public void test_getAll_ok() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
		}};
		
		List<Object> dataPoints = new ArrayList<Object>() {{
			add(3);
			add(4);
		}};
		
		Map<Long, Object> timeseriesExpected = new HashMap<Long, Object>() {{
			put(1262327400000L,3);
			put(1262413800000L,4);
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		Map<Long, Object> timeseriesActual = VariableTimeseriesEngine.getAll(id);
		
		assertEquals(timeseriesExpected, timeseriesActual);
		
	}
}
