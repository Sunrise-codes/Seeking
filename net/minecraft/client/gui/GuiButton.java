package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import me.seeking.module.client.CustomColor;
import me.seeking.ui.font.FontLoaders;
import me.seeking.utils.RenderUtils;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class GuiButton extends Gui {
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");

    /**
     * Button width in pixels
     */
    protected int width;

    /**
     * Button height in pixels
     */
    protected int height;

    /**
     * The x position of this control.
     */
    public int xPosition;

    /**
     * The y position of this control.
     */
    public int yPosition;

    /**
     * The string displayed on this control.
     */
    public String displayString;
    public int id;

    /**
     * True if this control is enabled, false to disable.
     */
    public boolean enabled;

    /**
     * Hides the button completely if false.
     */
    public boolean visible;
    protected boolean hovered;
    float red = 0;
    float green = 0;
    float blue = 0;
    float alpha = 0;

    public GuiButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver) {
        int i = 1;

        if (!this.enabled) {
            i = 0;
        } else if (mouseOver) {
            i = 2;
        }

        return i;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            if (this.hovered) {
                red = RenderUtils.toanim(red, CustomColor.getColor().getRed(), 4, 0.1f);
                green = RenderUtils.toanim(green, CustomColor.getColor().getGreen(), 4, 0.1f);
                blue = RenderUtils.toanim(blue, CustomColor.getColor().getBlue(), 4, 0.1f);
                alpha = RenderUtils.toanim(alpha, 128, 4, 0.1f);
            } else {
                red = RenderUtils.toanim(red, 0, 4, 0.1f);
                green = RenderUtils.toanim(green, 0, 4, 0.1f);
                blue = RenderUtils.toanim(blue, 0, 4, 0.1f);
                alpha = RenderUtils.toanim(alpha, 64, 4, 0.1f);
            }

                if(mc.thePlayer != null && mc.theWorld != null){

                }else {
                    mc.getFramebuffer().bindFramebuffer(false);
                    GuiIngame.checkSetupFBO(mc.getFramebuffer());
                    glClear(GL_STENCIL_BUFFER_BIT);
                    glEnable(GL_STENCIL_TEST);

                    glStencilFunc(GL_ALWAYS, 1, 1);
                    glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE);
                    glColorMask(false, false, false, false);
                    RenderUtils.drawRoundRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 4, new Color((int) red, (int) green, (int) blue, (int) alpha));

                    glColorMask(true, true, true, true);
                    glStencilFunc(GL_EQUAL, 1, 1);
                    glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
                    glDisable(GL_STENCIL_TEST);
                }


            RenderUtils.drawRoundRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 4, new Color((int) red, (int) green, (int) blue, (int) alpha));

            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (!this.enabled) {
                j = new Color(224, 224, 224).getRGB();
            } else if (this.hovered) {
                j = new Color(255, 255, 160).getRGB();
            }

            FontLoaders.font16.drawCenteredString(this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 4) / 2, j);
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY) {
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
}