package me.seeking.ui.clickgui;

import java.awt.Color;

import me.seeking.ui.font.FontLoaders;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import me.seeking.module.Module;

public class KeyBindButton extends ValueButton
{
    public Module cheat;
    public double opacity;
    public boolean bind;
    public Module.ModuleType category;
    
    public KeyBindButton(final Module.ModuleType category, final Module cheat, final int x, final int y) {
        super(category, null, x, y);
        this.opacity = 0.0;
        this.category = category;
        this.custom = true;
        this.bind = false;
        this.cheat = cheat;
    }
    
    @Override
    public void render(final int mouseX, final int mouseY, final Limitation limitation) {
        GL11.glEnable(3089);
        limitation.cut();
        Gui.drawRect(0, 0, 0, 0, 0);
        Gui.drawRect(this.x - 10, this.y - 4, this.x + 80, this.y + 11, new Color(39, 39, 39).getRGB());
        FontLoaders.font14.drawString("Bind", this.x - 7, this.y + 2, new Color(108, 108, 108).getRGB());
        FontLoaders.font14.drawString(String.valueOf(this.bind ? "" : "") + Keyboard.getKeyName(this.cheat.getKey()), this.x + 77 - FontLoaders.font14.getStringWidth(Keyboard.getKeyName(this.cheat.getKey())), this.y + 2, new Color(108, 108, 108).getRGB());
        GL11.glDisable(3089);
    }
    
    @Override
    public void key(final char typedChar, final int keyCode) {
        if (this.bind) {
            this.cheat.setKey(keyCode);
            if (keyCode == 1) {
                this.cheat.setKey(0);
            }
            ClickUi.binding = false;
            this.bind = false;
        }
        super.key(typedChar, keyCode);
    }
    
    @Override
    public void click(final int mouseX, final int mouseY, final int button) {
        if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + FontLoaders.font18.getStringHeight(this.cheat.getName()) + 5 && button == 0) {
            final boolean b = !this.bind;
            this.bind = b;
            ClickUi.binding = b;
        }
        super.click(mouseX, mouseY, button);
    }
}
