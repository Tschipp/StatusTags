package tschipp.statustags.common.status;

import tschipp.statustags.StatusTags;
import tschipp.statustags.api.AbstractStatusUpdate;

public abstract class BaseStatusUpdate extends AbstractStatusUpdate
{

	public BaseStatusUpdate(String name, int duration)
	{
		super(StatusTags.MODID, name, duration);
	}

}
