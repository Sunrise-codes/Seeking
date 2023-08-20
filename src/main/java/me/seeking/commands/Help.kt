package me.seeking.commands

import me.seeking.Seeking
import me.seeking.managers.Command
import me.seeking.utils.PlayerUtil.tellPlayer

class Help : Command {
    override fun run(args: Array<String>): Boolean {
        tellPlayer("Here are the list of commands:")
        for (c in Seeking.instance.commandManager!!.getCommands().values) {
            tellPlayer(c.usage()!!)
        }
        return true
    }

    override fun usage(): String? {
        return "USAGE: -help"
    }
}
