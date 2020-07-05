package tschipp.statustags.common.status;

import java.awt.Color;

import tschipp.statustags.api.StatusPriority;
import tschipp.statustags.common.config.StatusTagsConfig;

public class StatusDamaged extends BaseStatusUpdate
{

	public StatusDamaged()
	{
		super("damaged", 20);
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
