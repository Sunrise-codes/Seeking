package me.seeking.ui.font

import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import java.awt.Font

object FontLoaders {
    var font14: CFontRender? = null
    var font16: CFontRender? = null
    var font18: CFontRender? = null
    var fontBig18: CFontRender? = null
    var iconFont18: CFontRender? = null
    fun getFont(size: Int): Font {
        var font: Font
        try {
            val `is` = Minecraft.getMinecraft().resourceManager
                .getResource(ResourceLocation("seeking/font/normal.ttf")).inputStream
            font = Font.createFont(0, `is`)
            font = font.deriveFont(0, size.toFloat())
        } catch (ex: Exception) {
            ex.printStackTrace()
            println("Error loading font")
            font = Font("default", 0, size)
        }
        return font
    }

    fun getBigFont(size: Int): Font {
        var font: Font
        try {
            val `is` = Minecraft.getMinecraft().resourceManager
                .getResource(ResourceLocation("seeking/font/big.otf")).inputStream
            font = Font.createFont(0, `is`)
            font = font.deriveFont(0, size.toFloat())
        } catch (ex: Exception) {
            ex.printStackTrace()
            println("Error loading font")
            font = Font("default", 0, size)
        }
        return font
    }

    fun getIconFont(size: Int): Font {
        var font: Font
        try {
            val `is` = Minecraft.getMinecraft().resourceManager
                .getResource(ResourceLocation("seeking/font/ico.ttf")).inputStream
            font = Font.createFont(0, `is`)
            font = font.deriveFont(0, size.toFloat())
        } catch (ex: Exception) {
            ex.printStackTrace()
            println("Error loading font")
            font = Font("default", 0, size)
        }
        return font
    }
}
