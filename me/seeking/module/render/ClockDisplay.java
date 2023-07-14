package me.seeking.module.render;

import me.seeking.event.EventTarget;
import me.seeking.event.events.EventRender2D;
import me.seeking.module.Module;
import me.seeking.ui.font.FontLoaders;
import me.seeking.utils.RenderUtils;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * This file is a part of Seeking Client.
 */
public class ClockDisplay extends Module {
    public ClockDisplay(){
        super("ClockDisplay", ModuleType.Render);
        setX(44);
        setY(33);
        setWidth(65);
        setHeight(20);
    }

    @EventTarget
    public void on2D(EventRender2D e){
        RenderUtils.drawShadow((float) getX(), (float) getY(), (float) getWidth(), (float) getHeight());
        Gui.drawRect(getX(), getY(), getX()+getWidth(), getY()+getHeight(), new Color(0, 0, 0, 125).getRGB());
        FontLoaders.font16.drawStringWithShadow(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), (float) (getX()+10), (float) (getY()+6), -1);
    }
}
