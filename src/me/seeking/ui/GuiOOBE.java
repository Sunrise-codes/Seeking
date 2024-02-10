package me.seeking.ui;

import me.seeking.Seeking;
import me.seeking.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.Display;

import java.io.IOException;

public class GuiOOBE extends GuiScreen {
    public void initGui(){
        buttonList.add(new GuiButton(0,width / 2 -50,height / 2 + 50,100, 25, "Continue"));
        buttonList.add(new GuiButton(1,width / 2-50,height / 2 + 77, 100, 25, "Skip"));

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //BG
        GlStateManager.disableAlpha();
        Seeking.instance.si.renderShader(width, height);
        GlStateManager.enableAlpha();
        mc.fontRendererObj.drawStringWithShadow("DEBUG:"+ Minecraft.getDebugFPS()+" ", 0, 0, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == 1) {
            mc.displayGuiScreen(new GuiMainMenu());
        }
    }
}
