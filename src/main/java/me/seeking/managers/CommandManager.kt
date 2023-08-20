package me.seeking.managers

import me.seeking.commands.Bind
import me.seeking.commands.Help
import me.seeking.commands.Toggle
import me.seeking.utils.PlayerUtil

class CommandManager {
    /**
     * The HashMap that holds all the commands.
     */
    private val commands: HashMap<Array<String>, Command>

    /**
     * The prefix that holds the prefix required for the commands.
     */
    private val prefix = "-"

    init {
        commands = HashMap<Array<String>, Command>()
    }

    /**
     * Loads all the commands. These commands are stored in a hashmap.
     */
    fun loadCommands() {
        commands[arrayOf("help", "h")] = Help()
        commands[arrayOf("bind", "b")] = Bind()
        commands[arrayOf("toggle", "t")] = Toggle()
//        commands[arrayOf("getLWJGL", "getL")] = GetLWJGLCommand()
//        commands[arrayOf("qq", "esu")] = ESUCommand()
//        commands[arrayOf("music", "wyy")] = MusicCommand()
    }

    fun processCommand(rawMessage: String): Boolean {
        /**
         * Checks if the rawMessage starts if the prefix for the commands. If it does
         * not start with the prefix it does not process the command.
         */
        if (!rawMessage.startsWith(prefix)) {
            return false
        }
        /** Checks if the rawMessage has any text after the prefix.  */
        val safe = rawMessage.split(prefix.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size > 1
        /**
         * If the rawMessage has any text after the prefix it will continue to process
         * the command.
         */
        if (safe) {
            /** Gets rid of the prefix from the rawMessage.  */
            val beheaded = rawMessage.split(prefix.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

            /**
             * Splits the beheaded message at empty spaces so it can be sent to the command.
             */
            val args = beheaded.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            /** Gets the command using the start of the array.  */
            val command: Command? = getCommand(args[0])
            /** If a command was found it runs the command.  */
            if (command != null) {
                /** If the command failed it tell the user how to use that command.  */
                if (!command.run(args)) {
                    command.usage()?.let { PlayerUtil.tellPlayer(it) }
                }
            } else {
                PlayerUtil.tellPlayer("Try -help.")
            }
        } else {
            PlayerUtil.tellPlayer("Try -help.")
        }
        return true
    }

    /**
     * Goes through all the entries in the HashMap and checks if the name provided
     * is a valid command name.
     */
    private fun getCommand(name: String): Command? {
        for ((key, value) in commands) {
            val keyArray = key as Array<String>
            for (s in keyArray) {
                if (s.equals(name, ignoreCase = true)) {
                    return value as Command
                }
            }
        }
        return null
    }

    /**
     * Returns the HashMap that contains all the commands.
     */
    fun getCommands(): HashMap<Array<String>, Command> {
        return commands
    }
}
