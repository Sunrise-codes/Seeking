package me.seeking.module.render

import me.seeking.event.EventTarget
import me.seeking.event.events.EventRender2D
import me.seeking.module.Module
import me.seeking.ui.font.FontLoaders
import me.seeking.utils.RenderUtil
import net.minecraft.client.gui.Gui
import org.lwjgl.input.Keyboard
import java.awt.Color

/**
 * This file is a part of Seeking Client.
 * @author MiLiBlue
 */
class KeyDisplay : Module("KeyDisplay", ModuleType.Render) {
    init {
        setX(0.0)
        setY(40.0)
        setWidth(63.0)
        setHeight(41.0)
    }

    @EventTarget
    fun render2D(e: EventRender2D?) {
        //Forward
        RenderUtil.drawShadow((getX() + 21).toFloat(), getY().toFloat(), 20F, 20F)
        RenderUtil.drawRect(
            (getX() + 21),
            getY(),
            (getX() + 20 + 21),
            (getY() + 20),
            if (mc.gameSettings.keyBindForward.isKeyDown) Color(255, 255, 255, 135).rgb else Color(0, 0, 0, 135).rgb
        )
        FontLoaders.font16!!.drawStringWithShadow(
            Keyboard.getKeyName(mc.gameSettings.keyBindForward.keyCode).toUpperCase(), (getX() + 26).toFloat()
                .toDouble(), (getY() + 7).toFloat().toDouble(), -1
        )
        //Right
        RenderUtil.drawShadow((getX() + 42).toFloat(), (getY() + 21).toFloat(), 20F, 20F)
        RenderUtil.drawRect(
            (getX() + 42),
            (getY() + 21),
            (getX() + 20 + 42),
            (getY() + 20 + 21),
            if (mc.gameSettings.keyBindRight.isKeyDown) Color(255, 255, 255, 135).rgb else Color(0, 0, 0, 135).rgb
        )
        FontLoaders.font16!!.drawStringWithShadow(
            Keyboard.getKeyName(mc.gameSettings.keyBindRight.keyCode).toUpperCase(), (getX() + 48).toFloat()
                .toDouble(), (getY() + 28).toFloat().toDouble(), -1
        )
        //Left
        RenderUtil.drawShadow(getX().toFloat(), getY().toFloat() + 21, 20F, 20F)
        RenderUtil.drawRect(
            getX(),
            (getY() + 21),
            (getX() + 20),
            (getY() + 20 + 21),
            if (mc.gameSettings.keyBindLeft.isKeyDown) Color(255, 255, 255, 135).rgb else Color(0, 0, 0, 135).rgb
        )
        FontLoaders.font16!!.drawStringWithShadow(
            Keyboard.getKeyName(mc.gameSettings.keyBindLeft.keyCode).toUpperCase(), (getX() + 7).toFloat()
                .toDouble(), (getY() + 28).toFloat().toDouble(), -1
        )
        //Back
        RenderUtil.drawShadow((getX() + 21).toFloat(), getY().toFloat() + 21, 20F, 20F)
        RenderUtil.drawRect(
            (getX() + 21),
            (getY() + 21),
            (getX() + 20 + 21),
            (getY() + 20 + 21),
            if (mc.gameSettings.keyBindBack.isKeyDown) Color(255, 255, 255, 135).rgb else Color(0, 0, 0, 135).rgb
        )
        FontLoaders.font16!!.drawStringWithShadow(
            Keyboard.getKeyName(mc.gameSettings.keyBindBack.keyCode).toUpperCase(), (getX() + 28).toFloat()
                .toDouble(), (getY() + 28).toFloat().toDouble(), -1
        )
    }
}
