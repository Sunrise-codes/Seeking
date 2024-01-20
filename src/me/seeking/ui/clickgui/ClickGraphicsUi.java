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
        //Background
        RenderUtil.drawRoundedRect(x, y, x+width, height+y, new Color(49, 49, 49).getRGB(), new Color(49, 49, 49).getRGB());

        //Logo
        FontLoaders.fontBig18.drawString("SEEKING", (float) (x+10), (float) y+15, new Color(0, 164, 255).getRGB());
        FontLoaders.fontBig18.drawString("SEEKING", (float) (x+9), (float) y+14, -1);

        //Categorys(Text & Icon)
        for (Module.ModuleType m : Module.ModuleType.values()){
            count+=20;
            counterMap.put(m, count);
            if(m.equals(defaultType))
                RenderUtil.drawRoundedRect(x+2, y+28+counterMap.get(defaultType), 40+x+8, 12+y+28+counterMap.get(defaultType), new Color(0, 157, 255).getRGB(), new Color(0, 157, 255).getRGB());
            FontLoaders.font16.drawString(m.name(), x+13, y+30+count, -1);
        }
        if(count > Module.ModuleType.values().length){
            count = Module.ModuleType.values().length;
        }

    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
