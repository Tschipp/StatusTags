package tschipp.statustags.common.status;

import java.awt.Color;

import tschipp.statustags.api.StatusPriority;
import tschipp.statustags.common.config.StatusTagsConfig;

public class StatusAttacking extends BaseStatusUpdate
{

	public StatusAttacking()
	{
		super("attacking", 60);
	}
 
	@Override
	public StatusPriority getPriority()
	{
		return StatusPriority.HIGH;
	}
	
	@Override
	public Color getTagColor()
	{
		return new Color(StatusTagsConfig.settings.dangerColor);
	}
}
