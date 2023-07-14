package me.seeking.module.render;

import me.seeking.event.EventTarget;
import me.seeking.event.events.EventRender2D;
import me.seeking.module.Module;
import me.seeking.ui.font.FontLoaders;
import me.seeking.utils.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.server.MinecraftServer;

import java.awt.*;

public class PingDisplay extends Module {
    public PingDisplay(){
        super("PingDisplay", ModuleType.Render);
        setX(55);
        setY(0);
        setWidth(57);
        setHeight(20);
    }
    @EventTarget
    public void render2D(EventRender2D e){
        if (MinecraftServer.getServer().isSinglePlayer()) {
            RenderUtil.drawShadow((float) getX(), (float) getY(), (float) getWidth(), (float) getHeight());
            Gui.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), new Color(0, 0, 0, 135).getRGB());
            FontLoaders.font16.drawStringWithShadow(mc.getMinecraft().getNetHandler().getPlayerInfo(mc.getMinecraft().thePlayer.getUniqueID()).getResponseTime() + " Ping", (float) (getX() + 10), (float) (getY() + 6), -1);
        }
    }
}
