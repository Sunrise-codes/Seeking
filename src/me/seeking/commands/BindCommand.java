package me.seeking.commands;

import me.seeking.Seeking;
import me.seeking.managers.Command;
import me.seeking.module.Module;
import me.seeking.utils.PlayerUtil;
import org.lwjgl.input.Keyboard;

public class BindCommand implements Command {

    @Override
    public boolean run(String[] args) {
        if (args.length == 3) {

            Module m = Seeking.instance.moduleManager.getModuleByName(args[1]);

            if (m == null)
                return false;

            m.setKey(Keyboard.getKeyIndex(args[2].toUpperCase()));
            PlayerUtil.tellPlayer(m.getName() + " has been bound to " + args[2] + ".");
            return true;
        }
        return false;
    }

    @Override
    public String usage() {
        return "USAGE: -bind [module] [key]";
    }

}
