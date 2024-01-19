package me.seeking.module.render;

import me.seeking.event.EventTarget;
import me.seeking.event.events.EventRender2D;
import me.seeking.module.Module;
import net.minecraft.client.gui.Gui;

import java.awt.*;

/**
 * This file is a part of Seeking
 */
public class MusicInfo extends Module {
    public MusicInfo(){
        super("MusicInfo", ModuleType.Render);
        setX(170);
        setY(0);
        setHeight(120);
        setWidth(240);
    }

    @EventTarget
    public void onRender(EventRender2D e){
        Gui.drawRect(getX(), getY(), getWidth(), getHeight(),  new Color(0, 0, 0, 135).getRGB());
    }
}
