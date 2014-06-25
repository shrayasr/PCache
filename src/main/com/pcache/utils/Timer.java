package main.com.pcache.utils;

import java.util.HashMap;
import java.util.Map;

import main.com.pcache.exceptions.PCacheException;

public class Timer
{

	private static Map<String, Long> _tagTimes;
	
	static {
		_tagTimes = new HashMap<String, Long>();
	}
	
	public static void start(String tag) throws PCacheException {
		
		if (_tagTimes.containsKey(tag)) {
			throw new PCacheException("Can't start. Already tracking that tag");
		}
		
		_tagTimes.put(tag, System.currentTimeMillis());
	}
	
	public static void lap(String tag) {
		
		try {
			start(tag);
		}
		
		catch (PCacheException ex) {
			return;
		}
		
	}
	
	public static void end(String tag) throws PCacheException {
		
		if (!_tagTimes.containsKey(tag)) {
			throw new PCacheException("Can't stop. Not tracking that tag");
		}
		
		long startTime = _tagTimes.get(tag);
		_tagTimes.put(tag, (long) ((System.currentTimeMillis() - startTime)/1000.00));
		
	}
	
	public static long get(String tag) throws PCacheException {
		
		if (!_tagTimes.containsKey(tag)) {
			throw new PCacheException("Tag not tracked");
		}
		
		return _tagTimes.get(tag);
		
	}
	
}
