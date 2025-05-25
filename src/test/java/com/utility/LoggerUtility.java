package com.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class LoggerUtility {
	

	private LoggerUtility(){
	}

	public static Logger getLogger(Class<?> clazz) {
		Logger logger = null;
		if (logger == null) {
			logger = (Logger) LogManager.getLogger(clazz);
		}
		return logger;
	}
}
