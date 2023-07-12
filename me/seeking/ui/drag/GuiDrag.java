package me.seeking.ui.drag;

import me.seeking.Seeking;
import me.seeking.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;

/**
 * This file is a part of Seeking Client.
 */
public class GuiDrag extends GuiScreen {
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
        for (Module m : Seeking.instance.moduleManager.modules){
            m.mouseClick(mouseX, mouseY, mouseButton);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        for (Module m : Seeking.instance.moduleManager.modules){
            m.doGrag(mouseX, mouseY);
        }
    }
}
