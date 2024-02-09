package me.seeking.ui;

import me.seeking.Seeking;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.io.IOException;

/**
 * This file is a part of Seeking Client.
 */
public class GuiResetSession extends GuiScreen {
    public static String state = "请在打开的浏览器中继续操作...";

    public void initGui() {
        buttonList.add(new GuiButton(0,width / 2 - 100,height / 2 + 50,"Back"));
        new Thread(()-> {
            try {
                Seeking.instance.githubLogin.login();
            } catch (Exception e) {
                e.printStackTrace();
                state = e.getMessage();
            }
        }).start();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawCenteredString(mc.fontRendererObj, EnumChatFormatting.YELLOW + state, (int) (width / 2.0f), (int) (height / 2.0f - 5f),-1);
    }
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == 0) {
            mc.displayGuiScreen(new GuiMainMenu());
        }
    }

}
