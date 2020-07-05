package tschipp.statustags.common.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

public class Configs {
	
	public static class Settings
	{
		@Comment("If only players on the same team should have a status tag")
		public boolean onlyShowSameTeam = false;
		
		@Config.RangeInt(min = 0, max = 0xFFFFFF)
		@Comment("Default color of the status tags")
		public int defaultColor = 0x1FB736;
		
		@Config.RangeInt(min = 0, max = 0xFFFFFF)
		@Comment("Default color of the danger status tags")
		public int dangerColor = 0xAD001C;
		
	}

}
