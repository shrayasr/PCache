package main.com.pcache.DO;

import main.com.pcache.exceptions.PCacheException;
import main.com.pcache.utils.Commons;

public class PCacheTimestamp implements Comparable<PCacheTimestamp>
{
	long _timestampMilis;
	String _timestamp;
	
	public PCacheTimestamp(String timestamp) throws PCacheException {
		
		try {
			this._timestamp = timestamp;
			this._timestampMilis = Commons.convertISO8601toMilis(timestamp);
		} 
		
		catch (PCacheException e) {
			throw new PCacheException("Invalid Timestamp",e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return (int) this._timestampMilis;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		return (this._timestampMilis == ((PCacheTimestamp) obj)._timestampMilis);
	}

	@Override
	public int compareTo(PCacheTimestamp o)
	{
		
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;
		
		PCacheTimestamp comparingObject = (PCacheTimestamp) o;
		
		if (this._timestampMilis < comparingObject._timestampMilis) {
			return BEFORE;
		}
		
		else if (this._timestampMilis > comparingObject._timestampMilis) {
			return AFTER;
		}
		
		else {
			return EQUAL;
		}

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return this._timestamp;
	}
	
	
}
