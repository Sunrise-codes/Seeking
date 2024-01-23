package net.montoyo.mcef.utilities;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * A set of functions to log messages into the MCEF log channel.
 * @author montoyo
 *
 */
public class Log {
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static void info(String what, Object ... data) {
		//FMLLog.log("MCEF", Level.INFO, what, data);
		LOGGER.log(Level.INFO, "[MCEF] "+what+ Arrays.toString(data));
	}
	
	public static void warning(String what, Object ... data) {
		//FMLLog.log("MCEF", Level.WARN, what, data);
		LOGGER.log(Level.WARN, "[MCEF] "+what+ Arrays.toString(data));
	}
	
	public static void error(String what, Object ... data) {
		//FMLLog.log("MCEF", Level.ERROR, what, data);
		LOGGER.log(Level.ERROR, "[MCEF] "+what+ Arrays.toString(data));
	}

}
