package me.seeking.commands;

import me.seeking.Seeking;
import me.seeking.managers.Command;
import me.seeking.module.Module;
import me.seeking.utils.PlayerUtil;

public class ToggleCommand implements Command {

    @Override
    public boolean run(String[] args) {

        if (args.length == 2) {

            Module module = Seeking.instance.moduleManager.getModuleByName(args[1]);

            if (module == null) {
                PlayerUtil.tellPlayer("The module with the name " + args[1] + " does not exist.");
                return true;
            }

            module.setEnable(!module.isEnable());

            return true;
        }


        return false;
    }

    @Override
    public String usage() {
        return "USAGE: -toggle [module]";
    }
}
