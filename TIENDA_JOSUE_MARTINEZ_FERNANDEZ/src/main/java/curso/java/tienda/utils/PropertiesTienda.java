package curso.java.tienda.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class PropertiesTienda {
	private static Properties properties = null;
	private static LoggerTienda logger = LoggerTienda.getLoggerTienda();

	public static String getProperty(String propiedad) {
		if (properties == null) {
			crearProperties();
		}
		return properties.getProperty(propiedad);
	}

	private static void crearProperties() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL appResourceURL = loader.getResource("log4j.properties");
		String fichero = appResourceURL.getPath();
		properties = new Properties();
		try {
			properties.load(new FileInputStream(fichero));
		} catch (FileNotFoundException e) {
			logger.logError("Error al leer el fichero de propiedades");
		} catch (IOException e) {
			logger.logError("Error al leer el fichero de propiedades");
		}
	}
}
