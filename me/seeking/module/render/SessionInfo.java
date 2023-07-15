package me.seeking.module.render;

import me.seeking.event.EventTarget;
import me.seeking.event.events.EventJoinServer;
import me.seeking.event.events.EventRender2D;
import me.seeking.module.Module;
import me.seeking.ui.font.FontLoaders;
import me.seeking.utils.RenderUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * This file is a part of Seeking Client.
 */
public class SessionInfo extends Module {
    public static Date jointime;
    public SessionInfo(){
        super("SessionInfo", ModuleType.Render);
        setX(114);
        setY(114);
        setWidth(100);
        setHeight(32);
    }

    @EventTarget
    public void on2D(EventRender2D e){
        RenderUtil.drawShadow((float) getX(), (float) getY(), (float) getWidth(), (float) getHeight());
        Gui.drawRect(getX(), getY(), getX()+getWidth(), getY()+getHeight(),  new Color(0, 0, 0, 135).getRGB());
        FontLoaders.iconFont18.drawStringWithShadow("C", getX() + 3, getY()+6, -1);
        FontLoaders.font18.drawStringWithShadow("Session Info", getX() + 3 + FontLoaders.fontBig18.getStringWidth("C  "), getY()+5, -1);
        FontLoaders.iconFont18.drawStringWithShadow("B", getX() + 3, getY() + 6 + FontLoaders.fontBig18.getHeight()+FontLoaders.font18.getHeight(), -1);
        FontLoaders.font18.drawStringWithShadow("Play Time:"+getSpendTime(), getX() + 3 + FontLoaders.fontBig18.getStringWidth("B  "), getY() + 4.3 + FontLoaders.fontBig18.getHeight()+FontLoaders.font18.getHeight(), -1);
    }

    private String getSpendTime(){
        if(jointime != null){
            return sumSecondToTime(calLastedTime(jointime, new Date()));
        }
        return null;
    }

    private int calLastedTime(Date startDate, Date endDate) {
        long a = endDate.getTime();
        long b = startDate.getTime();
        int c = (int) ((a - b) / 1000);
        return c;
    }

    private String sumSecondToTime(int sumSecond) {
        if(sumSecond <= 0){
            return "00:00:00";
        }
        int h = sumSecond/3600;
        int m = (sumSecond-h*3600)/60;
        int s = sumSecond - h*3600-m*60;
        String time = "%02d:%02d:%02d";
        time = String.format(time,h,m,s);
        return time;
    }
}
