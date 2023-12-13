package curso.java.tienda.utils;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class EncriptadorTienda {

	private static StrongPasswordEncryptor encriptador = new StrongPasswordEncryptor();
	private static LoggerTienda logger = LoggerTienda.getLoggerTienda();

	public static String encriptar(String clave) {
		String claveEncriptada = null;
		try {
			claveEncriptada = encriptador.encryptPassword(clave);
		} catch (Exception e) {
			logger.logError("Error al encriptar");
		} finally {
			return claveEncriptada;
		}
	}

	public static boolean compararEncriptado(String clave, String claveEncriptada) {
		boolean comparacion = false;
		try {
			comparacion = encriptador.checkPassword(clave, claveEncriptada);
		} catch (Exception e) {
			logger.logError("Error al intentar comparar la encriptacion");
		} finally {
			return comparacion;
		}
	}
}
