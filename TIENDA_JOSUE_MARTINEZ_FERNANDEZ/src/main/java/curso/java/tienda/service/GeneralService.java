package curso.java.tienda.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class GeneralService {

	public static void recuperarCarrito(HttpServletRequest request, HttpSession session) {
		HttpSession sesion = request.getSession(false);
		HashMap<String, Integer> carrito = null;
		Cookie cookie = null;

		if (sesion == null) {
			sesion = request.getSession(true);
		}

		if (sesion.getAttribute("carrito") == null) {
			carrito = new HashMap<String, Integer>();
			cookie = recuperarCookie(request, "carrito");
			if (cookie != null) {
				carrito = new Gson().fromJson(cookie.getValue(), new TypeToken<HashMap<String, Integer>>() {
				}.getType());
			}
			sesion.setAttribute("carrito", carrito);
		}
	}

	public static Cookie recuperarCookie(HttpServletRequest request, String nombreCookie) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(nombreCookie)) {
					cookie.setValue(cookie.getValue().replaceAll("comillas", "\""));
					cookie.setValue(cookie.getValue().replaceAll("coma", ","));
					return cookie;
				}
			}
		}
		return null;
	}

	public static void setCookie(String nombreCookie, String valorCookie, HttpServletRequest request,
			HttpServletResponse response) {
		valorCookie = valorCookie.replaceAll("\"", "comillas");
		valorCookie = valorCookie.replaceAll(",", "coma");
		Cookie cookieAntigua = GeneralService.recuperarCookie(request, nombreCookie);
		if (cookieAntigua != null) {
			cookieAntigua.setValue(valorCookie);
			cookieAntigua.setMaxAge(60);
		} else {
			cookieAntigua = new Cookie(nombreCookie, valorCookie);
		}
		response.addCookie(cookieAntigua);
	}

	public static void eliminarCookie(String nombreCookie, HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(nombreCookie)) {
					cookie.setMaxAge(0);
					cookie.setValue("0");
					response.addCookie(cookie);
				}
			}
		}
	}

	public static HashMap<String, String> errorVacios(Map<String, String> listaCampos) {
		String nombreCampo;
		String valorCampo;
		HashMap<String, String> mapaErrores = new HashMap<String, String>();

		for (Entry<String, String> campo : listaCampos.entrySet()) {
			nombreCampo = campo.getKey();
			valorCampo = campo.getValue();
			if (valorCampo.equals("")) {
				mapaErrores.put("error".concat(nombreCampo), "El campo no puede estar vacio");
			}
		}
		if (mapaErrores.size() > 0) {
			return mapaErrores;
		} else {
			return null;
		}
	}

}
