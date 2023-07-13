package me.seeking.module.render;

import me.seeking.event.EventTarget;
import me.seeking.event.events.EventRender2D;
import me.seeking.module.Module;
import me.seeking.ui.font.FontLoaders;
import me.seeking.utils.RenderUtil;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * This file is a part of Seeking Client.
 * @author MiLiBlue
 */
public class KeyDisplay extends Module {
    public KeyDisplay(){
        super("KeyDisplay", ModuleType.Render);
        setX(0);
        setY(40);
        setWidth(63);
        setHeight(41);
    }

    @EventTarget
    public void render2D(EventRender2D e){
        //Forward
        RenderUtil.drawShadow((float) (getX() + 20), (float) getY(), 20, 20);
        Gui.drawRect(getX() + 40, getY(), getX()+20, getY()+20, mc.gameSettings.keyBindForward.isKeyDown()? new Color(255, 255, 255, 135).getRGB() : new Color(0, 0, 0, 135).getRGB());
        FontLoaders.font16.drawStringWithShadow(Keyboard.getKeyName(mc.gameSettings.keyBindForward.getKeyCode()).toUpperCase(), (float) (getX() + 27), (float) (getY()+6), -1);
        //Right
        RenderUtil.drawShadow((float) (getX() + 42), (float) (getY()+21), 20, 20);
        Gui.drawRect(getX()+42, getY()+21, getX()+20+42, getY()+20+21, mc.gameSettings.keyBindRight.isKeyDown()? new Color(255, 255, 255, 135).getRGB() : new Color(0, 0, 0, 135).getRGB());
        FontLoaders.font16.drawStringWithShadow(Keyboard.getKeyName(mc.gameSettings.keyBindRight.getKeyCode()).toUpperCase(), (float) (getX() + 50), (float) (getY()+27), -1);
        //Left
        RenderUtil.drawShadow((float) getX(), (float) getY()+21, 20, 20);
        Gui.drawRect(getX(), getY()+21, getX()+20, getY()+20+21,  mc.gameSettings.keyBindLeft.isKeyDown()? new Color(255, 255, 255, 135).getRGB() : new Color(0, 0, 0, 135).getRGB());
        FontLoaders.font16.drawStringWithShadow(Keyboard.getKeyName(mc.gameSettings.keyBindLeft.getKeyCode()).toUpperCase(), (float) (getX() + 8), (float) (getY()+27), -1);
        //Back
        RenderUtil.drawShadow((float) (getX() + 21), (float) getY()+21, 20, 20);
        Gui.drawRect(getX()+21, getY()+21, getX()+20+21, getY()+20+21, mc.gameSettings.keyBindBack.isKeyDown()? new Color(255, 255, 255, 135).getRGB() : new Color(0, 0, 0, 135).getRGB());
        FontLoaders.font16.drawStringWithShadow(Keyboard.getKeyName(mc.gameSettings.keyBindBack.getKeyCode()).toUpperCase(), (float) (getX() + 29), (float) (getY()+27), -1);
    }
}
