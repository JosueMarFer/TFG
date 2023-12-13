package curso.java.tienda.controller;

import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import curso.java.tienda.model.Contacto;
import curso.java.tienda.service.ContactoService;
import curso.java.tienda.service.GeneralService;
import curso.java.tienda.utils.LoggerTienda;

@Controller
public class ContactoController {

	@Autowired
	ContactoService contactoService;
	LoggerTienda logger = LoggerTienda.getLoggerTienda();

	@GetMapping("/goContacto")
	public String goContacto(Model model, @ModelAttribute("contacto") Contacto contacto) {
		model.addAttribute("activo", "contacto");
		return "contacto";
	}

	@PostMapping("/contacto")
	public String RegistrarContacto(@Valid @ModelAttribute Contacto contacto, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			return "contacto";
		} else {
			contacto.setBaja(false);
			contacto = contactoService.crear(contacto);
			logger.logInfo("Contacto creado");
			model.addAttribute("activo", "contacto");
			return "redirect:";
		}
	}
}
