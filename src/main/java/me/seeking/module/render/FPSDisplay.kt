package me.seeking.module.render

import me.seeking.event.EventTarget
import me.seeking.event.events.EventRender2D
import me.seeking.module.Module
import me.seeking.ui.font.FontLoaders
import me.seeking.utils.RenderUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import java.awt.Color

/**
 * This file is a part of Seeking Client.
 * @author MiLiBlue
 */
class FPSDisplay : Module("FPSDisplay", ModuleType.Render) {
    init {
        setX(85.0)
        setY(0.0)
        setWidth(50.0)
        setHeight(20.0)
    }

    @EventTarget
    fun render2D(e: EventRender2D?) {
        RenderUtil.drawShadow(getX().toFloat(), getY().toFloat(), getWidth().toFloat(), getHeight().toFloat())
        RenderUtil.drawRect(
            getX(),
            getY(),
            (getX() + getWidth()),
            (getY() + getHeight()),
            Color(0, 0, 0, 135).rgb
        )
        FontLoaders.font16!!.drawStringWithShadow(
            Minecraft.getDebugFPS().toString() + " FPS", (getX() + 10).toFloat().toDouble(), (getY() + 8).toFloat()
                .toDouble(), -1
        )
    }
}
