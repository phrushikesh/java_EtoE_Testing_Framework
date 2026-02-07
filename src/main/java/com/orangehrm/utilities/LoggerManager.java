package com.orangehrm.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerManager {

	// returns a logger instance for provided class
	public static Logger getLoggerInstance(Class<?> cls) {
		return LogManager.getLogger(cls);
	}
}
