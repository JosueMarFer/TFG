package curso.java.tienda.utils;

import java.net.URL;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.stereotype.Service;

public class LoggerTienda {
	private static LoggerTienda instance = null;
	private static final Logger logger = Logger.getLogger(LoggerTienda.class);

	private LoggerTienda() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL appResourceURL = loader.getResource("log4j.properties");
		String fichero = appResourceURL.getPath();
		PropertyConfigurator.configure(fichero);
	}

	public static LoggerTienda getLoggerTienda() {
		if (instance == null) {
			instance = new LoggerTienda();
		}
		return instance;
	}

	public void logDebug(String message) {
		logger.debug(message);
	}

	public void logInfo(String message) {
		logger.info(message);
	}

	public void logWarning(String message) {
		logger.warn(message);
	}

	public void logError(String message) {
		logger.error(message);
	}

	public void logFatal(String message) {
		logger.fatal(message);
	}
}
