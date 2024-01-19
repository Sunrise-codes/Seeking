package me.seeking.commands;

import me.seeking.Seeking;
import me.seeking.managers.Command;
import me.seeking.utils.PlayerUtil;

public class HelpCommand implements Command {

    @Override
    public boolean run(String[] args) {
        PlayerUtil.tellPlayer("Here are the list of commands:");
        for (Command c : Seeking.instance.commandManager.getCommands().values()) {
            PlayerUtil.tellPlayer(c.usage());
        }
        return true;
    }

    @Override
    public String usage() {
        return "USAGE: -help";
    }

}
