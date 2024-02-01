package me.seeking.ui.clickgui;

import me.seeking.module.Module;
import me.seeking.ui.font.FontLoaders;
import me.seeking.utils.RenderUtil;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This file is a part of Seeking Client.
 */
public class ClickGraphicsUi extends GuiScreen {
    private Map<Module.ModuleType, Integer> counterMap = new HashMap<>();
    private int x, y, width, height, count;
    private Module.ModuleType defaultType = Module.ModuleType.Render;

    public void initGui(){
        width = 390;
        height = 200;
        count = 0;
    }
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
