package me.seeking.commands

import me.seeking.Seeking
import me.seeking.managers.Command
import me.seeking.utils.PlayerUtil.tellPlayer
import org.lwjgl.input.Keyboard

class Bind : Command {
    override fun run(args: Array<String>): Boolean {
        if (args.size == 3) {
            val m = Seeking.instance.moduleManager!!.getModuleByName(args[1]) ?: return false
            m.setKey(Keyboard.getKeyIndex(args[2].toUpperCase()))
            tellPlayer(m.getName() + " has been bound to " + args[2] + ".")
            return true
        }
        return false
    }

    override fun usage(): String? {
        return "USAGE: -bind [module] [key]"
    }
}
