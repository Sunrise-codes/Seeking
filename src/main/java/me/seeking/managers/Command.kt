package me.seeking.managers

interface Command {
    /**
     * This method is called for every command if the user types it in, and if the
     * command runs it returns TRUE else FALSE.
     */
    fun run(args: Array<String>): Boolean

    /** This method is used to so the user knows how to use that command.  */
    fun usage(): String?
}
