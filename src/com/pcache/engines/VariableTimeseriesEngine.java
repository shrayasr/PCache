package com.pcache.engines;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pcache.DO.timeseries.VariableTimeseries;
import com.pcache.exceptions.PCacheException;

/**
 * Class to hold the list of variable time series' in the system. 
 *
 */
public class VariableTimeseriesEngine
{
	private static Map<Long, VariableTimeseries> _idVarTsMap;

	static {
		_idVarTsMap = new HashMap<>();
	}

	public static long allocate(List<String> timestamps, 
			List<Object> dataPoints) throws PCacheException {

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		long id = IDEngine.generateID();

		_idVarTsMap.put(id, ts);
		return id;
	}

	public static void addPoints(long id, 
			List<String> timestamps, List<Object> dataPoints) 
					throws PCacheException {

		// Sanity checks
		_exceptIfInvalidId(id);

		_idVarTsMap.get(id).addPoints(timestamps, dataPoints);

	}

	public static void modifyPoints(long id,
			List<String> timestampsToModify, List<Object> newDataPoints) 
					throws PCacheException {

		// Sanity checks
		_exceptIfInvalidId(id);

		_idVarTsMap.get(id).updatePoints(timestampsToModify, newDataPoints);

	}

	public static void removePoints(long id, List<String> timestampsToRemove) 
			throws PCacheException {

		// Sanity checks
		_exceptIfInvalidId(id);

		_idVarTsMap.get(id).removePoints(timestampsToRemove);
	}

	public static  Map<Long, Object> getAll(long id) throws PCacheException {

		// Sanity checks
		_exceptIfInvalidId(id);

		return _idVarTsMap.get(id).getAll();
	}

	public static Map<Long, Object> getFrom(long id, String timestampFrom) 
			throws PCacheException {

		// Sanity checks
		_exceptIfInvalidId(id);

		return _idVarTsMap.get(id).getRangeFrom(timestampFrom);
	}

	public static Map<Long, Object> getTo(long id, String timestampTo) 
			throws PCacheException {
		
		// Sanity checks
		_exceptIfInvalidId(id);
		
		return _idVarTsMap.get(id).getRangeTo(timestampTo);
	}

	public static int size(long id) throws PCacheException {

		// Sanity checks
		_exceptIfInvalidId(id);

		return _idVarTsMap.get(id).size();
	}

	private static void _exceptIfInvalidId(long id) throws PCacheException {

		if (_idVarTsMap.get(id) == null) {
			throw new PCacheException("That ID doesn't exist anymore in the " +
					"list of timeseries'. Maybe it is already deleted?");
		}

	}
}
