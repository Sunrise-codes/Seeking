package me.seeking.ui.clickgui;

import me.seeking.module.Module;
import me.seeking.ui.font.FontLoaders;
import me.seeking.value.*;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import java.awt.Color;

public class ValueButton
{
    public Value value;
    public String name;
    public boolean custom;
    public boolean change;
    public int x;
    public int y;
    public double opacity;
    public Module.ModuleType category;
    
    public ValueButton(final Module.ModuleType category, final Value value, final int x, final int y) {
        this.category = category;
        this.custom = false;
        this.opacity = 0.0;
        this.value = value;
        this.x = x;
        this.y = y;
        this.name = "";
        if (this.value instanceof Option) {
            this.change = (boolean)((Option)this.value).getValue();
        }
        else if (this.value instanceof Mode) {
            this.name = new StringBuilder().append(((Mode)this.value).getValue()).toString();
        }
        else if (value instanceof Numbers) {
            final Numbers v = (Numbers)value;
            this.name = String.valueOf(this.name) + (v.isInteger() ? ((Number)v.getValue()).intValue() : ((Number)v.getValue()).doubleValue());
        }
        this.opacity = 0.0;
    }
    
    public void render(final int mouseX, final int mouseY, final Limitation limitation) {
        if (!this.custom) {
            if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + FontLoaders.font18.getHeight() + 6) {
                if (this.opacity + 10.0 < 200.0) {
                    this.opacity += 10.0;
                }
                else {
                    this.opacity = 200.0;
                }
            }
            else if (this.opacity - 6.0 > 0.0) {
                this.opacity -= 6.0;
            }
            else {
                this.opacity = 0.0;
            }
            if (this.value instanceof Option) {
                this.change = (boolean)((Option)this.value).getValue();
            }
            else if (this.value instanceof Mode) {
                this.name = new StringBuilder().append(((Mode)this.value).getValue()).toString();
            }
            else if (this.value instanceof Numbers) {
                final Numbers v = (Numbers)this.value;
                this.name = new StringBuilder().append(v.isInteger() ? ((Number)v.getValue()).intValue() : ((Number)v.getValue()).doubleValue()).toString();
                if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y + FontLoaders.font14.getHeight() - 10 && mouseY < this.y + FontLoaders.font14.getHeight() + 2 && Mouse.isButtonDown(0)) {
                    final double min = v.getMinimum().doubleValue();
                    final double max = v.getMaximum().doubleValue();
                    final double inc = v.getIncrement().doubleValue();
                    final double valAbs = mouseX - (this.x + 1.0);
                    double perc = valAbs / 68.0;
                    perc = Math.min(Math.max(0.0, perc), 1.0);
                    final double valRel = (max - min) * perc;
                    double val = min + valRel;
                    val = Math.round(val * (1.0 / inc)) / (1.0 / inc);
                    v.setValue(val);
                }
            }
            int staticColor;
            if (this.category.name().equals("Combat")) {
                staticColor = new Color(231, 76, 60).getRGB();
            }
            else if (this.category.name().equals("Render")) {
                staticColor = new Color(54, 1, 205).getRGB();
            }
            else if (this.category.name().equals("Movement")) {
                staticColor = new Color(45, 203, 113).getRGB();
            }
            else if (this.category.name().equals("Player")) {
                staticColor = new Color(141, 68, 173).getRGB();
            }
            else if (this.category.name().equals("World")) {
                staticColor = new Color(38, 154, 255).getRGB();
            }
            else {
                staticColor = new Color(38, 154, 255).getRGB();
            }
            GL11.glEnable(3089);
            limitation.cut();
            Gui.drawRect(this.x - 10, this.y - 4, this.x + 80, this.y + 11, new Color(39, 39, 39).getRGB());
            if (this.value instanceof Option) {
                FontLoaders.font14.drawString(this.value.getName(), this.x - 7, this.y + 2, ((boolean)((Option)this.value).getValue()) ? new Color(255, 255, 255).getRGB() : new Color(108, 108, 108).getRGB());
            }
            final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
            if (this.value instanceof Mode) {
                FontLoaders.font14.drawString(this.value.getName(), this.x - 7, this.y + 3, new Color(255, 255, 255).getRGB());
                FontLoaders.font14.drawString(this.name, this.x + 77 - FontLoaders.font14.getStringWidth(this.name), this.y + 3, new Color(182, 182, 182).getRGB());
            }
            if (this.value instanceof Numbers) {
                final Numbers v2 = (Numbers)this.value;
                final double render = 82.0f * (((Number)v2.getValue()).floatValue() - v2.getMinimum().floatValue()) / (v2.getMaximum().floatValue() - v2.getMinimum().floatValue());
                Gui.drawRect(this.x - 8, this.y + FontLoaders.font14.getHeight() + 2, this.x + 78, this.y + FontLoaders.font14.getHeight() - 9, new Color(50, 50, 50, 180).getRGB());
                Gui.drawRect(this.x - 8, this.y + FontLoaders.font14.getHeight() + 2, (int)(this.x - 4 + render), this.y + FontLoaders.font14.getHeight() - 9, staticColor);
            }
            if (this.value instanceof Numbers) {
                FontLoaders.font14.drawString(this.value.getName(), this.x - 7, this.y, new Color(255, 255, 255).getRGB());
                FontLoaders.font14.drawString(this.name, this.x + FontLoaders.font14.getStringWidth(this.value.getName()), this.y, -1);
            }
            GL11.glDisable(3089);
        }
    }

    public void key(final char typedChar, final int keyCode) {
    }
    
    public void click(final int mouseX, final int mouseY, final int button) {
        if (!this.custom && mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + FontLoaders.font18.getHeight()) {
            if (this.value instanceof Option) {
                final Option v = (Option)this.value;
                v.setValue(!(boolean)v.getValue());
                return;
            }
            if (this.value instanceof Mode) {
                Mode mode = (Mode)value;
                String current = mode.getValue();
                int next;
                for (next = 0;next < mode.getModes().length;) {
                    if (mode.getModes()[next].equals(current)) {
                        next++;
                        break;
                    }
                    next++;
                }
                value.setValue(mode.getModes()[next==(mode.getModes().length) ? 0 : next]);
            }
        }
    }
}
