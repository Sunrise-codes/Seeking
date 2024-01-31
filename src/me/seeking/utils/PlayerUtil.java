package me.seeking.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

/**
 * This file is a part of Seeking Client.
 */
public class PlayerUtil {
    public static void tellPlayer(String message) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("\247b[Seeking] \247r" + message));
    }
    public static void tellPlayerIrc(String message) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("\247b[Irc] \247r" + message));
    }
}

