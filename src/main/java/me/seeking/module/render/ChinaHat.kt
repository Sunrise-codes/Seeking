package me.seeking.module.render

import me.seeking.event.EventTarget
import me.seeking.event.events.EventRenderWorld
import me.seeking.mixin.interfaces.IRenderManager
import me.seeking.module.Module
import me.seeking.value.Numbers
import me.seeking.value.Option
import net.minecraft.entity.EntityLivingBase
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import org.lwjgl.util.glu.Cylinder
import java.awt.Color

/**
 * This file is a part of Seeking Client.
 */
class ChinaHat : Module("ChinaHat", ModuleType.Render) {
    var renderInFirstPerson = Option("ShowInFirstPerson", false)
    var side: Numbers<Float> = Numbers("Side", 45.0f, 30.0f, 50.0f, 1.0f)
    var stack: Numbers<Float> = Numbers("Stacks", 50.0f, 45.0f, 200.0f, 5.0f)

    init {
        addValues(side, stack, renderInFirstPerson)
        setKey(Keyboard.KEY_L)
    }

    @EventTarget
    fun onRenderW0rld(evt: EventRenderWorld) {
        if (mc.gameSettings.thirdPersonView == 0 && !renderInFirstPerson.getValue()) {
            return
        }
        drawChinaHat(mc.thePlayer, evt)
    }

    private fun drawChinaHat(entity: EntityLivingBase, evt: EventRenderWorld) {
        val renderManager: IRenderManager = mc.renderManager as IRenderManager
        val x =
            entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * evt.partialTicks.toDouble() - renderManager.renderPosX()
        val y =
            entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * evt.partialTicks.toDouble() - renderManager.renderPosY()
        val z =
            entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * evt.partialTicks.toDouble() - renderManager.renderPosZ()
        val side = side.intValue() as Int
        val stack = stack.intValue() as Int
        GL11.glPushMatrix()
        GL11.glTranslated(x, y + if (mc.thePlayer.isSneaking) 2.0 else 2.2, z)
        GL11.glRotatef(-entity.width, 0.0f, 1.0f, 0.0f)
        val col = Color(-1)
        GL11.glColor4f(col.red / 255f, col.green / 255f, col.blue / 255f, 0.3f)
        GL11.glDisable(GL11.GL_ALPHA_TEST)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glDepthMask(false)
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST)
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST)
        GL11.glLineWidth(1.0f)
        val c = Cylinder()
        GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f)
        c.drawStyle = 100011
        c.draw(0.0f, 0.8f, 0.4f, side, stack)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glEnable(GL11.GL_ALPHA_TEST)
        GL11.glDepthMask(true)
        GL11.glCullFace(GL11.GL_BACK)
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE)
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE)
        GL11.glPopMatrix()
    }
}
