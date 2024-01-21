package me.seeking;

import de.florianmichael.viamcp.ViaMCP;
import me.miliblue.rintaro.CutsomException;
import me.miliblue.rintaro.Loader;
import me.seeking.event.EventManager;
import me.seeking.event.EventTarget;
import me.seeking.event.events.EventKeyboard;
import me.seeking.leak.Leak;
import me.seeking.managers.CommandManager;
import me.seeking.managers.FileManager;
import me.seeking.managers.ModuleManager;
import me.seeking.module.Module;
import me.seeking.ui.ShaderInstance;
import me.seeking.ui.font.FontLoaders;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * This file is a part of Seeking Client.
 * @author MiLiBlue
 */
public class Seeking {
    public static Seeking instance = new Seeking();
    public ModuleManager moduleManager;
    public EventManager eventManager;
    public FileManager fileManager;
    public CommandManager commandManager;
    public ShaderInstance si;

    /**
     * 当方块人启动的时候会调用这个方法
     */
    public void start(){
        new Thread(() -> {
            try {
                FontLoaders.init();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            try {
                if(Leak.leak.isNotSeeking()){
                    Leak.leak.fucker();
                }
            } catch (ClassNotFoundException e) {
                try {
                    Leak.leak.fucker();
                } catch (NoSuchFieldException ex) {
                } catch (IllegalAccessException ex) {
                }
            } catch (NoSuchFieldException e) {
            } catch (IllegalAccessException e) {
            }
            try {
                ViaMCP.create();
                ViaMCP.INSTANCE.initAsyncSlider();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        try {
            Loader.getInstance().loadMods(Minecraft.getMinecraft().mcDataDir.getAbsolutePath().replace("\\.", "").replace("\\", "/")+"/Plugins");
        } catch (CutsomException | NoSuchMethodException | IOException | InvocationTargetException |
                 IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            throw new RuntimeException(e);
        }
        moduleManager = new ModuleManager();
        moduleManager.addModules();
        eventManager = new EventManager();
        eventManager.register(this);
        fileManager = new FileManager();
        commandManager = new CommandManager();
        commandManager.loadCommands();
        loadCFG();
        si = new ShaderInstance();
        //Set Title
        Display.setTitle("Seeking 0.2 - (Minecraft 1.8.9)");
    }

    /**
     * 当方块人关闭的时候会调用这个方法
     */
    public void stop(){
        saveCFG();
    }

    /**
     * 这个方法用来监听按键的，事件系统会自动调用这个几把方法
     */
    @EventTarget
    public void keyListener(EventKeyboard e){
        for(Module m : moduleManager.modules){
            if(e.getKey() == m.getKey()){
                m.setEnable(!m.isEnable());
            }
        }
    }

    /**
     * 这个方法用来保存配置的
     */
    public void saveCFG(){
        //Build Content
        StringBuilder sb = new StringBuilder();
        sb.append("[ENABLE]");
        for (Module m : moduleManager.getEnableModules()){
            sb.append(m.getName()+"|");
        }
        sb.append("\n");
        sb.append("[KEY]");
        for (Module m : moduleManager.modules){
            sb.append(m.getName()+":"+m.getKey()+"|");
        }
        sb.append("\n");
        sb.append("[X]");
        for (Module m : moduleManager.modules){
            sb.append(m.getName()+":"+m.getX()+"|");
        }
        sb.append("\n");
        sb.append("[Y]");
        for (Module m : moduleManager.modules){
            sb.append(m.getName()+":"+m.getY()+"|");
        }
        sb.append("\n");

        //Save CFG
        fileManager.saveFile("SeekingCFG.seeking", sb.toString());
    }

    /**
     * 这个方法用来加载配置的
     */
    public void loadCFG(){
        //Read CFG File
        List<String> list = fileManager.loadFile("SeekingCFG.seeking");
        if(list != null) {
            for (String s : list) {
                if (s.startsWith("[ENABLE]")) {
                    s = s.replace("[ENABLE]", "");
                    if (s != "") {
                        String[] iq = s.split("\\|");
                        for (String st : iq) {
                            if (st != null)
                                if(moduleManager.getModuleByName(st) != null && !st.equalsIgnoreCase("ClickGui"))
                                    moduleManager.getModuleByName(st).setEnable(true);
                        }
                    }
                }

                if (s.startsWith("[KEY]")) {
                    s = s.replace("[KEY]", "");
                    if (s != "") {
                        String[] iq = s.split("\\|");
                        for (String st : iq) {
                            String[] wow = st.split(":");
                            if(moduleManager.getModuleByName(wow[0]) != null)
                                moduleManager.getModuleByName(wow[0]).setKey(Integer.parseInt(wow[1]));
                        }
                    }
                }

                if (s.startsWith("[X]")) {
                    s = s.replace("[X]", "");
                    if (s != "") {
                        String[] iq = s.split("\\|");
                        for (String st : iq) {
                            String[] wow = st.split(":");
                            if(moduleManager.getModuleByName(wow[0]) != null)
                                moduleManager.getModuleByName(wow[0]).setX(Double.parseDouble(wow[1]));
                        }
                    }
                }

                if (s.startsWith("[Y]")) {
                    s = s.replace("[Y]", "");
                    if (s != "") {
                        String[] iq = s.split("\\|");
                        for (String st : iq) {
                            String[] wow = st.split(":");
                            if(moduleManager.getModuleByName(wow[0]) != null)
                                moduleManager.getModuleByName(wow[0]).setY(Double.parseDouble(wow[1]));
                        }
                    }
                }
            }
        }
    }
}
