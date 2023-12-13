package curso.java.tienda.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GeneralController {

	@PostMapping("/headerButtons")
	public String headerButtons(HttpSession session, Model model,
			@RequestParam(name = "btnLogin", defaultValue = "empty") String btnLogin,
			@RequestParam(name = "btnCarro", defaultValue = "empty") String btnCarro,
			@RequestParam(name = "btnCerrarSesion", defaultValue = "empty") String btnCerrarSesion) {
		if (btnLogin != null && !btnLogin.equals("empty")) {
			return "redirect:/goLogin";
		} else if (btnCarro != null && !btnCarro.equals("empty")) {
			return "redirect:/carro";
		} else if (btnCerrarSesion != null && !btnCerrarSesion.equals("empty")) {
			return "redirect:/cerrarSesion";
		}
		return "redirect:";
	}
}
