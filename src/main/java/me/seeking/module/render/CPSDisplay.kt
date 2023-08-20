package me.seeking.module.render

import me.seeking.event.EventTarget
import me.seeking.event.events.EventClickMouse
import me.seeking.event.events.EventRender2D
import me.seeking.event.events.EventUpdate
import me.seeking.module.Module
import me.seeking.ui.font.FontLoaders
import me.seeking.utils.RenderUtil
import net.minecraft.client.gui.Gui
import java.awt.Color

class CPSDisplay : Module("CPSDisplay", ModuleType.Render) {
    @Transient
    private var delays: MutableList<Long?> = ArrayList<Long?>()

    init {
        setX(170.0)
        setY(0.0)
        setWidth(62.0)
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
            "$cps CPS", (getX() + 16).toFloat().toDouble(), (getY() + 6).toFloat()
                .toDouble(), -1
        )
    }

    @EventTarget
    fun onUpdate(e: EventUpdate?) {
        val list: ArrayList<Long?> = ArrayList<Long?>()
        val iterator: Iterator<*> = delays.iterator()
        while (iterator.hasNext()) {
            val iq = iterator.next() as Long
            if (System.currentTimeMillis() - iq < 1000L) {
                list.add(java.lang.Long.valueOf(iq))
            }
        }
        delays = list
    }

    @EventTarget
    fun onClickMouse(e: EventClickMouse?) {
        delays.add(java.lang.Long.valueOf(System.currentTimeMillis()))
    }

    val cps: Int
        get() = delays.size
}
