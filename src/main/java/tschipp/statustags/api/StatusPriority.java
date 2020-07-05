package tschipp.statustags.api;

public enum StatusPriority
{
	LOWEST(0),
	LOW(1),
	NORMAL(2),
	HIGH(3),
	HIGHEST(4);
	
	private int prio;
	
	private StatusPriority(int prio)
	{
		this.prio = prio;
	}
	
	/**
	 * Compares two priorities. Returns true if this priority is greater than or equal to other's priority.
	 */
	public boolean compare(StatusPriority other)
	{
		return this.prio >= other.prio;
	}
	
}
