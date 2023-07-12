package me.seeking.managers;

import me.seeking.commands.BindCommand;
import me.seeking.commands.GetLWJGLCommand;
import me.seeking.commands.HelpCommand;
import me.seeking.commands.ToggleCommand;
import me.seeking.utils.PlayerUtil;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    /**
     * The HashMap that holds all the commands.
     **/
    private HashMap<String[], Command> commands;

    /**
     * The prefix that holds the prefix required for the commands.
     **/
    private String prefix;

    public CommandManager() {
        commands = new HashMap<String[], Command>();
        prefix = "-";
    }

    /**
     * Loads all the commands. These commands are stored in a hashmap.
     **/
    public void loadCommands() {
        commands.put(new String[]{"help", "h"}, new HelpCommand());
        commands.put(new String[]{"bind", "b"}, new BindCommand());
        commands.put(new String[]{"toggle", "t"}, new ToggleCommand());
        commands.put(new String[]{"getLWJGL", "getL"}, new GetLWJGLCommand());
    }

    public boolean processCommand(String rawMessage) {
        /**
         * Checks if the rawMessage starts if the prefix for the commands. If it does
         * not start with the prefix it does not process the command.
         **/
        if (!rawMessage.startsWith(prefix)) {
            return false;
        }

        /** Checks if the rawMessage has any text after the prefix. **/
        boolean safe = rawMessage.split(prefix).length > 1;

        /**
         * If the rawMessage has any text after the prefix it will continue to process
         * the command.
         **/
        if (safe) {
            /** Gets rid of the prefix from the rawMessage. **/
            String beheaded = rawMessage.split(prefix)[1];

            /**
             * Splits the beheaded message at empty spaces so it can be sent to the command.
             **/
            String[] args = beheaded.split(" ");

            /** Gets the command using the start of the array. **/
            Command command = getCommand(args[0]);

            /** If a command was found it runs the command. **/
            if (command != null) {
                /** If the command failed it tell the user how to use that command. **/
                if (!command.run(args)) {
                    PlayerUtil.tellPlayer(command.usage());
                }
            }
            /** If no command was found it tell the user to do the help command. **/
            else {
                PlayerUtil.tellPlayer("Try -help.");
            }
        }
        /**
         * If there is no text after the prefix, it tell the user to do the help
         * command.
         **/
        else {
            PlayerUtil.tellPlayer("Try -help.");
        }

        return true;
    }

    /**
     * Goes through all the entries in the HashMap and checks if the name provided
     * is a valid command name.
     **/
    private Command getCommand(String name) {
        for (Map.Entry entry : commands.entrySet()) {
            String[] key = (String[]) entry.getKey();
            for (String s : key) {
                if (s.equalsIgnoreCase(name)) {
                    return (Command) entry.getValue();
                }
            }

        }
        return null;
    }

    /**
     * Returns the HashMap that contains all the commands.
     **/
    public HashMap<String[], Command> getCommands() {
        return commands;
    }

}
