package tschipp.statustags.client;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tschipp.statustags.common.CommonProxy;

public class ClientProxy extends CommonProxy
{
	
	private long ticks = 0;
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		
		
	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
		
		
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
			
	}
	
	@Override
	public long getTicks()
	{
		return ticks;
	}
	
	@Override
	public void incTicks()
	{
		ticks++;
	}
	
	@Override
	public void resetTicks()
	{
		ticks = 0;
	}
}
