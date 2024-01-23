package net.montoyo.mcef.client;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import me.seeking.Seeking;
import me.seeking.event.EventTarget;
import me.seeking.event.events.EventTick;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowserOsr;
import org.cef.browser.CefMessageRouter;
import org.cef.browser.CefMessageRouter.CefMessageRouterConfig;
import net.minecraft.client.Minecraft;
import net.montoyo.mcef.BaseProxy;
import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.IDisplayHandler;
import net.montoyo.mcef.api.IJSQueryHandler;
import net.montoyo.mcef.example.ExampleMod;
import net.montoyo.mcef.remote.RemoteConfig;
import net.montoyo.mcef.utilities.Log;
import net.montoyo.mcef.virtual.VirtualBrowser;

public class ClientProxy extends BaseProxy {
	
	public static String ROOT = ".";
	public static boolean VIRTUAL = false;
	
	private CefApp cefApp;
	private CefClient cefClient;
	private CefMessageRouter cefRouter;
	private boolean firstRouter = true;
	private ArrayList<CefBrowserOsr> browsers = new ArrayList<CefBrowserOsr>();
	private String updateStr;
	private Minecraft mc = Minecraft.getMinecraft();
	
	@Override
	public void onInit() {
		ROOT = mc.mcDataDir.getAbsolutePath().replaceAll("\\\\", "/");
		
		if(ROOT.endsWith("."))
			ROOT = ROOT.substring(0, ROOT.length() - 1);
		
		if(ROOT.endsWith("/"))
			ROOT = ROOT.substring(0, ROOT.length() - 1);
		
		UpdateFrame uf = new UpdateFrame();
		RemoteConfig cfg = new RemoteConfig();
		
		cfg.load();
		if(!cfg.downloadMissing(uf)) {
			Log.warning("Going in virtual mode; couldn't download resources.");
			VIRTUAL = true;
			return;
		}
		
		updateStr = cfg.getUpdateString();
		uf.dispose();
		
		if(VIRTUAL)
			return;
		
		Log.info("Now adding \"%s\" to java.library.path", ROOT);
		
		try {
			Field pathsField = ClassLoader.class.getDeclaredField("usr_paths");
			pathsField.setAccessible(true);
			
			String[] paths = (String[]) pathsField.get(null);
			String[] newList = new String[paths.length + 1];
			
			System.arraycopy(paths, 0, newList, 0, paths.length);
			newList[paths.length] = ROOT.replace('/', File.separatorChar);
			pathsField.set(null, newList);
		} catch(Exception e) {
			Log.error("Failed to do it! Entering virtual mode...");
			e.printStackTrace();
			
			VIRTUAL = true;
			return;
		}
		
		Log.info("Done without errors.");
		
		CefSettings settings = new CefSettings();
		settings.windowless_rendering_enabled = true;
		settings.background_color = settings.new ColorType(255, 255, 255, 255);
		settings.locales_dir_path = (new File(ROOT, "MCEFLocales")).getAbsolutePath();
		settings.cache_path = (new File(ROOT, "MCEFCache")).getAbsolutePath();
		
		try {
			cefApp = CefApp.getInstance(settings);
			cefApp.myLoc = ROOT.replace('/', File.separatorChar);
			
			CefApp.addAppHandler(new AppHandler());
			cefClient = cefApp.createClient();
		} catch(Throwable t) {
			Log.error("Going in virtual mode; couldn't initialize CEF.");
			t.printStackTrace();
			
			VIRTUAL = true;
			return;
		}
		
		Log.info(cefApp.getVersion().toString());
		cefRouter = CefMessageRouter.create(new CefMessageRouterConfig("mcefQuery", "mcefCancel"));
		cefClient.addMessageRouter(cefRouter);
		
		(new ShutdownThread(browsers)).start();
		//FMLCommonHandler.instance().bus().register(this);
		//Replace with Seeking Event
		Seeking.instance.eventManager.register(this);
		if(MCEF.ENABLE_EXAMPLE)
			(new ExampleMod()).onInit();
		
		Log.info("MCEF loaded successfuly.");
	}
	
	public CefApp getCefApp() {
		return cefApp;
	}
	
	@Override
	public IBrowser createBrowser(String url) {
		if(VIRTUAL)
			return new VirtualBrowser();
		
		CefBrowserOsr ret = (CefBrowserOsr) cefClient.createBrowser(url, true, false);
		browsers.add(ret);
		return ret;
	}
	
	@Override
	public void registerDisplayHandler(IDisplayHandler idh) {
		if(!VIRTUAL)
			cefClient.addDisplayHandler(new DisplayHandler(idh));
	}
	
	@Override
	public boolean isVirtual() {
		return VIRTUAL;
	}
	
	@Override
	public void openExampleBrowser(String url) {
		if(MCEF.ENABLE_EXAMPLE)
			ExampleMod.INSTANCE.showScreen(url);
	}
	
	@Override
	public void registerJSQueryHandler(IJSQueryHandler iqh) {
		if(!VIRTUAL)
			cefRouter.addHandler(new MessageRouter(iqh), firstRouter); //SwingUtilities.invokeLater() ?
		
		if(firstRouter)
			firstRouter = false;
	}
	
	@EventTarget
	public void onTick(EventTick ev) {
		//if(ev.side == Side.CLIENT && ev.phase == TickEvent.Phase.START && ev.type == TickEvent.Type.CLIENT) {
			mc.mcProfiler.startSection("MCEF");
			
			for(CefBrowserOsr b: browsers)
				b.mcefUpdate();
			
			mc.mcProfiler.endSection();
		//}
	}
	
	@EventTarget
//	public void onLogin(PlayerEvent.PlayerLoggedInEvent ev) {
//		if(updateStr == null)
//			return;
//		PlayerUtil.tellPlayer(updateStr);
//	}
	
	public void removeBrowser(CefBrowserOsr b) {
		browsers.remove(b);
	}

}
