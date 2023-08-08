package me.seeking.managers;

import me.seeking.module.Module;
import me.seeking.module.fun.Derp;
import me.seeking.module.player.AutoGG;
import me.seeking.module.player.PacketFix;
import me.seeking.module.render.*;
import me.seeking.module.player.Sprint;
import paulscode.sound.libraries.ChannelJavaSound;

import java.util.ArrayList;

/**
 * This file is a part of Seeking Client.
 */
public class ModuleManager {
    public ArrayList<Module> modules = new ArrayList<>();

    /**
     * 这个方法用来初始化Module List的
     */
    public void addModules(){
        new Thread(() -> {
            modules.add(new Sprint());
            modules.add(new ClickGui());
            modules.add(new FPSDisplay());
            modules.add(new CPSDisplay());
            modules.add(new KeyDisplay());
            modules.add(new InfoDisplay());
            modules.add(new AutoGG());
            modules.add(new Fullbright());
            modules.add(new PingDisplay());
            modules.add(new SessionInfo());
            modules.add(new PacketFix());
            modules.add(new Derp());
            modules.add(new MusicInfo());
            modules.add(new ChinaHat());
        }).start();
    }

    /**
     * 这个方法用来返回Module实例的
     * @param name
     */
    public Module getModuleByName(String name){
        for (Module m : modules) {
            if (!m.getName().equalsIgnoreCase(name)) continue;
            return m;
        }
        return null;
    }

    /**
     * 这个方法用来返回开启的Module的
     * @return 开启的module
     */
    public ArrayList<Module> getEnableModules(){
        ArrayList<Module> temp = new ArrayList<>();
        for (Module m : modules){
            if(m.isEnable()){
                temp.add(m);
            }
        }
        return temp;
    }
}
