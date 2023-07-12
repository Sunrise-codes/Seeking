package me.seeking.commands;

import me.seeking.managers.Command;
import me.seeking.utils.PlayerUtil;
import org.lwjgl.Sys;

public class GetLWJGLCommand implements Command {

    @Override
    public boolean run(String[] args) {
        PlayerUtil.tellPlayer("Here is the version of LWJGL:"+ Sys.getVersion());
        return true;
    }

    @Override
    public String usage() {
        return "USAGE: -getLWJGL";
    }

}