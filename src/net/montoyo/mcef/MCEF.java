package net.montoyo.mcef;

import net.montoyo.mcef.client.ClientProxy;
import net.montoyo.mcef.utilities.Log;

public class MCEF {
	
	public static final String VERSION = "0.5";
	public static boolean ENABLE_EXAMPLE;

	public static MCEF INSTANCE = new MCEF();

	public static BaseProxy PROXY = new ClientProxy();

//	public void onPreInit() {
//		Log.info("Loading MCEF config...");
//
//		Configuration cfg = new Configuration(ev.getSuggestedConfigurationFile());
//		ENABLE_EXAMPLE = cfg.getBoolean("exampleBrowser", "main", true, "Set this to false if you don't want to enable the F10 browser.");
//		cfg.save();
//	}
//
	public void init() {
		Log.info("Now initializing MCEF v%s...", VERSION);
		PROXY.onInit();
	}

}
