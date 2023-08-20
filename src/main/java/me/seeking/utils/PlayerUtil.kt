package me.seeking.utils

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText

/**
 * This file is a part of Seeking Client.
 */
object PlayerUtil {
    fun tellPlayer(message: String) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(ChatComponentText("\u00a7a[Seeking] \u00a7r$message"))
    }
}

