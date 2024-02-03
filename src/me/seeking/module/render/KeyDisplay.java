package me.seeking.module.render;

import me.seeking.event.EventTarget;
import me.seeking.event.events.EventRender2D;
import me.seeking.module.Module;
import me.seeking.ui.font.FontLoaders;
import me.seeking.utils.BlurUtil;
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
        RenderUtil.drawShadow((float) (getX() + 21), (float) getY(), 20, 20);
        RenderUtil.drawRect((float) (getX()+21), (float) getY(), (float) (getX()+20+21), (float) (getY()+20),  mc.gameSettings.keyBindForward.isKeyDown()? new Color(255, 255, 255, 135).getRGB() : new Color(0, 0, 0, 135).getRGB());
        FontLoaders.font16.drawString(Keyboard.getKeyName(mc.gameSettings.keyBindForward.getKeyCode()).toUpperCase(), (float) (getX() + 26.5), (float) (getY()+6), -1);
        //BlurUtil.blurArea((float) (getX()+21), (float) getY(), 20, 20);
        //Right
        RenderUtil.drawShadow((float) (getX() + 42), (float) (getY()+21), 20, 20);
        RenderUtil.drawRect((float) (getX()+42), (float) (getY()+21), (float) (getX()+20+42), (float) (getY()+20+21),  mc.gameSettings.keyBindRight.isKeyDown()? new Color(255, 255, 255, 135).getRGB() : new Color(0, 0, 0, 135).getRGB());
        FontLoaders.font16.drawString(Keyboard.getKeyName(mc.gameSettings.keyBindRight.getKeyCode()).toUpperCase(), (float) (getX() + 49), (float) (getY()+27), -1);
        //BlurUtil.blurArea((float) (getX() + 42), (float) (getY()+21), 20, 20);
        //Left
        RenderUtil.drawShadow((float) getX(), (float) getY()+21, 20, 20);
        RenderUtil.drawRect((float) getX(), (float) (getY()+21), (float) (getX()+20), (float) (getY()+20+21), mc.gameSettings.keyBindLeft.isKeyDown()? new Color(255, 255, 255, 135).getRGB() : new Color(0, 0, 0, 135).getRGB());
        FontLoaders.font16.drawString(Keyboard.getKeyName(mc.gameSettings.keyBindLeft.getKeyCode()).toUpperCase(), (float) (getX() + 7), (float) (getY()+27), -1);
        //BlurUtil.blurArea((float) getX(), (float) getY()+21, 20, 20);
        //Back
        RenderUtil.drawShadow((float) (getX() + 21), (float) getY()+21, 20, 20);
        RenderUtil.drawRect((float) (getX()+21), (float) (getY()+21), (float) (getX()+20+21), (float) (getY()+20+21), mc.gameSettings.keyBindBack.isKeyDown()? new Color(255, 255, 255, 135).getRGB() : new Color(0, 0, 0, 135).getRGB());
        FontLoaders.font16.drawString(Keyboard.getKeyName(mc.gameSettings.keyBindBack.getKeyCode()).toUpperCase(), (float) (getX() + 28.5), (float) (getY()+27), -1);
        //BlurUtil.blurArea((float) (getX() + 21), (float) getY()+21, 20, 20);
    }
}
