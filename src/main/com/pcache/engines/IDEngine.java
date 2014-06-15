package main.com.pcache.engines;

public class IDEngine
{

	private static long _lastGeneratedID = 1;
	
	public static long generateID() {
		return _lastGeneratedID++;
	}
}
