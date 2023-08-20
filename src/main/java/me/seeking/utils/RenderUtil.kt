package me.seeking.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

class RenderUtil {
    companion object {//你说得对，但是伴生对象不是类的 static 方法，而是类的实例化对象。
        private val mc = Minecraft.getMinecraft()
        fun drawShadow(x: Float, y: Float, width: Float, height: Float) {
            drawTexturedRect(x - 9, y - 9, 9f, 9f, "paneltopleft")
            drawTexturedRect(x - 9, y + height, 9f, 9f, "panelbottomleft")
            drawTexturedRect(x + width, y + height, 9f, 9f, "panelbottomright")
            drawTexturedRect(x + width, y - 9, 9f, 9f, "paneltopright")
            drawTexturedRect(x - 9, y, 9f, height, "panelleft")
            drawTexturedRect(x + width, y, 9f, height, "panelright")
            drawTexturedRect(x, y - 9, width, 9f, "paneltop")
            drawTexturedRect(x, y + height, width, 9f, "panelbottom")
        }

        fun drawTexturedRect(x: Float, y: Float, width: Float, height: Float, image: String) {
            GL11.glPushMatrix()
            val enableBlend = GL11.glIsEnabled(GL11.GL_BLEND)
            val disableAlpha = !GL11.glIsEnabled(GL11.GL_ALPHA_TEST)
            if (!enableBlend) GL11.glEnable(GL11.GL_BLEND)
            if (!disableAlpha) GL11.glDisable(GL11.GL_ALPHA_TEST)
            mc.textureManager.bindTexture(ResourceLocation("seeking/shadow/$image.png"))
            GlStateManager.color(1f, 1f, 1f, 1f)
            drawModalRectWithCustomSizedTexture(x, y, 0f, 0f, width, height, width, height)
            if (!enableBlend) GL11.glDisable(GL11.GL_BLEND)
            if (!disableAlpha) GL11.glEnable(GL11.GL_ALPHA_TEST)
            GL11.glPopMatrix()
        }

        fun drawModalRectWithCustomSizedTexture(x: Float, y: Float, u: Float, v: Float, width: Float, height: Float, textureWidth: Float, textureHeight: Float) {
            val f = 1.0f / textureWidth
            val f1 = 1.0f / textureHeight
            val tessellator = Tessellator.getInstance()
            val worldrenderer = tessellator.worldRenderer
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX)
            worldrenderer.pos(x.toDouble(), (y + height).toDouble(), 0.0)
                .tex((u * f).toDouble(), ((v + height) * f1).toDouble()).endVertex()
            worldrenderer.pos((x + width).toDouble(), (y + height).toDouble(), 0.0)
                .tex(((u + width) * f).toDouble(), ((v + height) * f1).toDouble()).endVertex()
            worldrenderer.pos((x + width).toDouble(), y.toDouble(), 0.0)
                .tex(((u + width) * f).toDouble(), (v * f1).toDouble()).endVertex()
            worldrenderer.pos(x.toDouble(), y.toDouble(), 0.0).tex((u * f).toDouble(), (v * f1).toDouble()).endVertex()
            tessellator.draw()
        }

        fun drawRect(left: Double, top: Double, right: Double, bottom: Double, color: Int) {
            var left = left
            var top = top
            var right = right
            var bottom = bottom
            if (left < right) {
                val i = left
                left = right
                right = i
            }
            if (top < bottom) {
                val j = top
                top = bottom
                bottom = j
            }
            val f3 = (color shr 24 and 255).toFloat() / 255.0f
            val f = (color shr 16 and 255).toFloat() / 255.0f
            val f1 = (color shr 8 and 255).toFloat() / 255.0f
            val f2 = (color and 255).toFloat() / 255.0f
            val tessellator = Tessellator.getInstance()
            val worldrenderer = tessellator.worldRenderer
            GlStateManager.enableBlend()
            GlStateManager.disableTexture2D()
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
            GlStateManager.color(f, f1, f2, f3)
            worldrenderer.begin(7, DefaultVertexFormats.POSITION)
            worldrenderer.pos(left, bottom, 0.0).endVertex()
            worldrenderer.pos(right, bottom, 0.0).endVertex()
            worldrenderer.pos(right, top, 0.0).endVertex()
            worldrenderer.pos(left, top, 0.0).endVertex()
            tessellator.draw()
            GlStateManager.enableTexture2D()
            GlStateManager.disableBlend()
        }
    }
}