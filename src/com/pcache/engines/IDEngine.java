package com.pcache.engines;

public class IDEngine
{

	private static long _lastGeneratedID = 0;
	
	public static long generateID() {
		return _lastGeneratedID++;
	}
}
