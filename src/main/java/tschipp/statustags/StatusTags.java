package tschipp.statustags;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import tschipp.statustags.api.StatusTagsAPI;
import tschipp.statustags.common.CommonProxy;
import tschipp.statustags.common.manager.StatusTagManager;

@EventBusSubscriber
@Mod(modid = StatusTags.MODID, name = StatusTags.NAME, version = StatusTags.VERSION, guiFactory = "tschipp.statustags.client.gui.GuiFactoryStatusTags", dependencies = StatusTags.DEPENDENCIES, acceptedMinecraftVersions = StatusTags.ACCEPTED_VERSIONS, certificateFingerprint = StatusTags.CERTIFICATE_FINGERPRINT)
public class StatusTags
{

	@SidedProxy(clientSide = "tschipp.statustags.client.ClientProxy", serverSide = "tschipp.statustags.common.CommonProxy")
	public static CommonProxy proxy;

	// Instance
	@Instance(StatusTags.MODID)
	public static StatusTags instance;

	public static final String MODID = "statustags";
	public static final String VERSION = "GRADLE:VERSION";
	public static final String NAME = "Status Tags";
	public static final String ACCEPTED_VERSIONS = "[1.12.2,1.13)";
	public static final Logger LOGGER = LogManager.getFormatterLogger("StatusTags");
	public static final String DEPENDENCIES = "required-after:forge@[13.20.1.2386,);after:gamestages;";
	public static final String CERTIFICATE_FINGERPRINT = "fd21553434f4905f2f73ea7838147ac4ea07bd88";
	public static File CONFIGURATION_FILE;

	public static boolean FINGERPRINT_VIOLATED = false;

	public static SimpleNetworkWrapper network;

	public static StatusTagManager MANAGER = new StatusTagManager();
	static
	{
		StatusTagsAPI.MANAGER = MANAGER;
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{	
		StatusTags.proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		StatusTags.proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		StatusTags.proxy.postInit(event);
	}

}
