package me.seeking.module.render;

import me.seeking.event.EventTarget;
import me.seeking.event.events.EventRender2D;
import me.seeking.module.Module;
import me.seeking.ui.font.FontLoaders;
import me.seeking.utils.RenderUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * This file is a part of Seeking Client.
 */
public class InfoDisplay extends Module {
    public InfoDisplay(){
        super("InfoDisplay", ModuleType.Render);
        setX(44);
        setY(33);
        setHeight(15);
    }

    @EventTarget
    public void on2D(EventRender2D e){
        setWidth(8+FontLoaders.fontBig18.getStringWidth("SEEKING ")+FontLoaders.font18.getStringWidth("| "+mc.getSession().getUsername()+" | "+LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
//        RenderUtil.drawShadow((float) getX(), (float) getY(), (float) getWidth(), (float) getHeight());
//        RenderUtil.drawShadow((float) getX(), (float) getY(), (float) getWidth(), (float) getHeight());
        Gui.drawRect(getX(), getY(), getX()+getWidth(), getY()+getHeight(), new Color(0, 0, 0).getRGB());
        FontLoaders.fontBig18.drawString("SEEKING", (float) (getX()+4), (float) getY()+4, new Color(0, 164, 255).getRGB());
        FontLoaders.fontBig18.drawString("SEEKING", (float) (getX()+3), (float) getY()+3, -1);
        FontLoaders.font18.drawStringWithShadow("| "+mc.getSession().getUsername()+" | "+LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), getX()+4+FontLoaders.fontBig18.getStringWidth("SEEKING "), getY()+4, -1);
    }
}
