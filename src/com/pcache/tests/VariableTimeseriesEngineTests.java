package com.pcache.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.pcache.DO.timeseries.VariableTimeseries;
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("1");
		}};
		
		long id1 = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
	}
	
	@Test (expected=PCacheException.class)
	public void test_allocateNewVarTs_nulls() throws PCacheException
	{
		List<String> timestamps = null;
		List<String> dataPoints = null;
		
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("3");

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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("3");
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("3");
		}};
		
		List<String> timestampsToAdd = new ArrayList<String>() {{
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
		}};
		
		List<String> dataPointsToAdd = new ArrayList<String>() {{
			add("3");
			add("1");
			add("2");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		VariableTimeseriesEngine.addPoints(id, timestampsToAdd, dataPointsToAdd);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_addPoints_nulls() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("3");
		}};
		
		List<String> timestampsToAdd = null;
		List<String> dataPointsToAdd = null;
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		VariableTimeseriesEngine.addPoints(id, timestampsToAdd, dataPointsToAdd);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_addPoints_invalidTimestamp() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("3");
		}};
		
		List<String> timestampsToAdd = new ArrayList<String>() {{
			add("2010-01-02T12:00:00.000+05:30");
			add("2asdf010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
		}};
		
		List<String> dataPointsToAdd = new ArrayList<String>() {{
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("3");
		}};
		
		List<String> timestampsToAdd = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<String> dataPointsToAdd = new ArrayList<String>() {{
			add("1");
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("3");
		}};
		
		List<String> timestampsToAdd = new ArrayList<String>() {{
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
		}};
		
		List<String> dataPointsToAdd = new ArrayList<String>() {{
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("3");
		}};
		
		List<String> timestampsToModify = new ArrayList<String>() {{
			add("2012-01-01T12:00:00.000+05:30");
		}};
		
		List<String> dataPointsToModify = new ArrayList<String>() {{
			add("3");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		VariableTimeseriesEngine.modifyPoints(id, timestampsToModify, 
				dataPointsToModify);
		
		String val = VariableTimeseriesEngine.getAll(id)
				.getOne("2010-01-01T12:00:00.000+05:30");
		
		assertEquals(val, 2);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_modifyPoints_invalidTimestamp() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("3");
		}};
		
		List<String> timestampsToModify = new ArrayList<String>() {{
			add("asdf2010-01-01T12:00:00.000+05:30");
		}};
		
		List<String> dataPointsToModify = new ArrayList<String>() {{
			add("3");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		VariableTimeseriesEngine.modifyPoints(id, timestampsToModify, 
				dataPointsToModify);
		
		String val = VariableTimeseriesEngine.getAll(id)
				.getOne("2010-01-01T12:00:00.000+05:30");
		
		assertEquals(val, 2);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_modifyPoints_unequal() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("3");
		}};
		
		List<String> timestampsToModify = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<String> dataPointsToModify = new ArrayList<String>() {{
			add("2");
			add("3");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		VariableTimeseriesEngine.modifyPoints(id, timestampsToModify, 
				dataPointsToModify);
		
		String val = VariableTimeseriesEngine.getAll(id)
				.getOne("2010-01-01T12:00:00.000+05:30");
		
		assertEquals(val, 2);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_modifyPoints_null() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("3");
		}};
		
		List<String> timestampsToModify = null;
		List<String> dataPointsToModify = null;
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		VariableTimeseriesEngine.modifyPoints(id, timestampsToModify, 
				dataPointsToModify);
		
		String val = VariableTimeseriesEngine.getAll(id)
				.getOne("2010-01-01T12:00:00.000+05:30");
		
		assertEquals(val, 2);
		
	}
	
	public void test_modifyPoints_ok() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("3");
		}};
		
		List<String> timestampsToModify = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
		}};
		
		List<String> dataPointsToModify = new ArrayList<String>() {{
			add("2");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		VariableTimeseriesEngine.modifyPoints(id, timestampsToModify, 
				dataPointsToModify);
		
		String val = VariableTimeseriesEngine.getAll(id)
				.getOne("2010-01-01T12:00:00.000+05:30");
		
		assertEquals(val, 2);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_removePoints_timeseriesNoExist() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
		}};
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("3");
			add("4");
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("3");
			add("4");
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("3");
			add("4");
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("3");
			add("4");
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("3");
			add("4");
		}};
		
		Map<Long, String> timeseriesExpectedPoints = new HashMap<Long, String>() {{
			put(1262327400000L,"3");
			put(1262413800000L,"4");
		}};
		
		VariableTimeseries timeseriesExpected = new VariableTimeseries(
				timeseriesExpectedPoints);
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		VariableTimeseries timeseriesActual = VariableTimeseriesEngine
				.getAll(12341L);
		
		assertEquals(timeseriesExpected, timeseriesActual);
		
	}
	
	@Test
	public void test_getAll_ok() throws PCacheException {
		
		List<String> timestamps = new ArrayList<String>() {{
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
		}};
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("3");
			add("4");
		}};
		
		Map<Long, String> timeseriesExpectedPoints = new HashMap<Long, String>() {{
			put(1262327400000L,"3");
			put(1262413800000L,"4");
		}};
		
		VariableTimeseries timeseriesExpected = new VariableTimeseries(
				timeseriesExpectedPoints);
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		VariableTimeseries timeseriesActual = VariableTimeseriesEngine.getAll(id);
		
		assertEquals(timeseriesExpected.toJson(), timeseriesActual.toJson());
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_getFrom_invalidId() throws PCacheException
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("3");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		String timestampFrom="2010-01-09T12:00:00.000+05:30";
		VariableTimeseries actualTimeseries = VariableTimeseriesEngine
				.getFrom(1121L, timestampFrom);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_getFrom_invalidTimestamp() throws PCacheException
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("3");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		Map<Long, String> expectedTimeseriesPoints = new HashMap<Long, String>() {{
			
			put(1263018600000L, "1");
			put(1263105000000L, "2");
			put(1263191400000L, "3");
			put(1263277800000L, "3");
			
		}};
		
		VariableTimeseries expectedTimeseries = new VariableTimeseries(
				expectedTimeseriesPoints);
		
		String timestampFrom="asdf2010-01-09T12:00:00.000+05:30";
		
		VariableTimeseries actualTimeseries = VariableTimeseriesEngine
				.getFrom(id, timestampFrom);
		
		assertEquals(expectedTimeseries.toJson(), actualTimeseries.toJson());
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_getFrom_null() throws PCacheException
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("3");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		String timestampFrom=null;
		VariableTimeseries actualTimeseries = VariableTimeseriesEngine
				.getFrom(id, timestampFrom);
		
	}
	
	@Test (expected = PCacheException.class)
	public void test_getFrom_timestampGreater() throws PCacheException
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("3");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		String timestampFrom="2010-01-13T12:00:00.000+05:30";
		VariableTimeseries actualTimeseries = VariableTimeseriesEngine
				.getFrom(id, timestampFrom);
		
	}
	
	@Test
	public void test_getFrom_ok() throws PCacheException
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("3");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		Map<Long, String> expectedTimeseriesPoints = new HashMap<Long, String>() {{
			
			put(1263018600000L, "1");
			put(1263105000000L, "2");
			put(1263191400000L, "3");
			put(1263277800000L, "3");
			
		}};
		
		VariableTimeseries expectedTimeseries = new VariableTimeseries(
				expectedTimeseriesPoints);
		
		String timestampFrom="2010-01-09T12:00:00.000+05:30";
		VariableTimeseries actualTimeseries = VariableTimeseriesEngine
				.getFrom(id, timestampFrom);
		
		assertEquals(expectedTimeseries.toJson(), actualTimeseries.toJson());
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_getTo_invalidId() throws PCacheException
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("3");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		String timestampFrom="2010-01-04T12:00:00.000+05:30";
		VariableTimeseries actualTimeseries = VariableTimeseriesEngine
				.getTo(121L, timestampFrom);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_getTo_invalidTimestamp() throws PCacheException
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("3");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		String timestampFrom="asdf2010-01-04T12:00:00.000+05:30";
		VariableTimeseries actualTimeseries = VariableTimeseriesEngine
				.getTo(id, timestampFrom);
		
	}
	
	@Test (expected=PCacheException.class)
	public void test_getTo_null() throws PCacheException
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("3");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		String timestampFrom=null;
		VariableTimeseries actualTimeseries = VariableTimeseriesEngine
				.getTo(id, timestampFrom);
		
	}
	
	@Test (expected = PCacheException.class)
	public void test_getTo_timestampLesser() throws PCacheException
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("3");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		String timestampTo="2009-01-04T12:00:00.000+05:30";
		VariableTimeseries actualTimeseries = VariableTimeseriesEngine
				.getTo(id, timestampTo);
		
	}
	
	@Test
	public void test_getTo_ok() throws PCacheException
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
		
		List<String> dataPoints = new ArrayList<String>() {{
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("1");
			add("2");
			add("3");
			add("3");
		}};
		
		long id = VariableTimeseriesEngine.allocate(timestamps, dataPoints);
		
		Map<Long, String> expectedTimeseriesPoints = new HashMap<Long, String>() {{
			put(1262327400000L, "1");
			put(1262413800000L, "2");
			put(1262500200000L, "3");
			put(1262586600000L, "1");
			
		}};
		
		VariableTimeseries expectedTimeseries = new VariableTimeseries(
				expectedTimeseriesPoints);
		
		String timestampFrom="2010-01-04T12:00:00.000+05:30";
		VariableTimeseries actualTimeseries = VariableTimeseriesEngine
				.getTo(id, timestampFrom);
		
		assertEquals(expectedTimeseries.toJson(), actualTimeseries.toJson());
		
	}
}
