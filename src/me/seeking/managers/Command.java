package me.seeking.managers;

public interface Command {

	/**
	 * This method is called for every command if the user types it in, and if the
	 * command runs it returns TRUE else FALSE.
	 **/
	boolean run(String[] args);

	/** This method is used to so the user knows how to use that command. **/
	String usage();

	/** This method is used for Rintaro plugins. **/
	 default String[] name(){
		return new String[]{"Null"};
	}
}
