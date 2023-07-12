package me.seeking.ui.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

/**
 * This file is a part of Seeking Client.
 */
public class ClickGui extends GuiScreen {
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        Gui.drawRect(0 ,0, 345.5, 262, new Color(0, 0, 0, 156).getRGB());
    }
}
