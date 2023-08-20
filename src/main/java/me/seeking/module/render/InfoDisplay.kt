package me.seeking.module.render

import me.seeking.event.EventTarget
import me.seeking.event.events.EventRender2D
import me.seeking.module.Module
import me.seeking.ui.font.FontLoaders
import me.seeking.utils.RenderUtil
import net.minecraft.client.gui.Gui
import java.awt.Color
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class InfoDisplay:Module {
    constructor():super("InfoDisplay", ModuleType.Render){
        setX(44.0)
        setY(33.0)
        setHeight(15.0)
    }
    @EventTarget
    fun onRender2D(e:EventRender2D){
        setWidth(
            (8 + (FontLoaders.fontBig18?.getStringWidth("SEEKING ")!!) + FontLoaders.font18!!.getStringWidth(
                "| " + mc.session.username + " | " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            )).toDouble()
        )
        RenderUtil.drawShadow(getX().toFloat(), getY().toFloat(), getWidth().toFloat(), getHeight().toFloat())
        RenderUtil.drawShadow(getX().toFloat(), getY().toFloat(), getWidth().toFloat(), getHeight().toFloat())
        RenderUtil.drawRect(
            getX(),
            getY(),
            (getX() + getWidth()),
            (getY() + getHeight()),
            Color(0, 0, 0).rgb
        )
        FontLoaders.fontBig18!!.drawString(
            "SEEKING",
            (getX() + 4).toFloat(),
            getY().toFloat() + 4,
            Color(0, 164, 255).rgb
        )
        FontLoaders.fontBig18!!.drawString("SEEKING", (getX() + 3).toFloat(), getY().toFloat() + 3, -1)
        FontLoaders.font18!!.drawStringWithShadow(
            "| " + mc.session.username + " | " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            getX() + 4 + FontLoaders.fontBig18!!.getStringWidth("SEEKING "),
            getY() + 4,
            -1
        )
    }
}