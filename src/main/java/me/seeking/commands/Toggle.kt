package me.seeking.commands

import me.seeking.Seeking
import me.seeking.managers.Command
import me.seeking.utils.PlayerUtil.tellPlayer

class Toggle : Command {
    override fun run(args: Array<String>): Boolean {
        if (args.size == 2) {
            val module = Seeking.instance.moduleManager?.getModuleByName(args[1])
            if (module == null) {
                tellPlayer("The module with the name " + args[1] + " does not exist.")
                return true
            }
            module.setEnable(!module.isEnable())
            return true
        }
        return false
    }

    override fun usage(): String? {
        return "USAGE: -toggle [module]"
    }
}
