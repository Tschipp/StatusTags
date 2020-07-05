
package tschipp.statustags.common;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import tschipp.statustags.StatusTags;
import tschipp.statustags.network.StatusUpdateChangeClient;
import tschipp.statustags.network.StatusUpdateChangeServer;

public class CommonProxy
{
	
	private long ticks = 0;

	public void preInit(FMLPreInitializationEvent event)
	{
		StatusTags.network = NetworkRegistry.INSTANCE.newSimpleChannel("StatusTags");
		
		StatusTags.network.registerMessage(StatusUpdateChangeClient.class, StatusUpdateChangeClient.class, 0, Side.CLIENT);
		StatusTags.network.registerMessage(StatusUpdateChangeServer.class, StatusUpdateChangeServer.class, 1, Side.SERVER);

		StatusTags.MANAGER.regStatusUpdates();
	}

	public void init(FMLInitializationEvent event)
	{
		
	}

	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
	
	public long getTicks()
	{
		return ticks;
	}
	
	public void resetTicks()
	{
		ticks = 0;
	}
	
	public void incTicks()
	{
		ticks++;
	}

}
