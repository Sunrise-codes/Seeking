package me.seeking.module.render;

import me.seeking.event.EventTarget;
import me.seeking.event.events.EventClickMouse;
import me.seeking.event.events.EventRender2D;
import me.seeking.event.events.EventUpdate;
import me.seeking.module.Module;
import me.seeking.ui.font.FontLoaders;
import me.seeking.utils.RenderUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This file is a part of Seeking Client.
 * @author MiLiBlue
 */
public class CPSDisplay extends Module {
    private transient List delays = new ArrayList();
    public CPSDisplay(){
        super("CPSDisplay", ModuleType.Render);
        setX(170);
        setY(0);
        setWidth(62);
        setHeight(20);
    }

    @EventTarget
    public void render2D(EventRender2D e){
        //RenderUtil.drawShadow((float) getX(), (float) getY(), (float) getWidth(), (float) getHeight());
        RenderUtil.drawRoundedRect((float) getX(), (float) getY(), (float) (getX()+getWidth()), (float) (getY()+getHeight()),   new Color(0, 0, 0, 155).getRGB(), new Color(0, 0, 0, 155).getRGB());
        //mc.fontRendererObj.drawStringWithShadow(getCPS()+" CPS", (float) (getX()+13), (float) (getY()+6), -1);
        FontLoaders.font16.drawString(getCPS()+" CPS", (float) (getX()+16), (float) (getY()+6), -1);
    }
    @EventTarget
    public void onUpdate(EventUpdate e){
        List<Long> list = new ArrayList();
        Iterator iterator = this.delays.iterator();

        while(iterator.hasNext()) {
            long iq = ((Long)iterator.next()).longValue();
            if(System.currentTimeMillis() - iq < 1000L) {
                list.add(Long.valueOf(iq));
            }
        }

        this.delays = list;
    }

    @EventTarget
    public void onClickMouse(EventClickMouse e){
        this.delays.add(Long.valueOf(System.currentTimeMillis()));
    }

    public int getCPS() {
        return this.delays.size();
    }
}
