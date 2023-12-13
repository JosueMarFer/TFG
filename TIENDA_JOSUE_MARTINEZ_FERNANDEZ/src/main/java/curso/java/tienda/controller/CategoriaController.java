package curso.java.tienda.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import curso.java.tienda.model.Categoria;
import curso.java.tienda.model.OpcionMenu;
import curso.java.tienda.model.Usuario;
import curso.java.tienda.service.CategoriaService;
import curso.java.tienda.service.OpcionMenuService;
import curso.java.tienda.utils.LoggerTienda;

@Controller
public class CategoriaController {

	@Autowired
	CategoriaService categoriaService;
	@Autowired
	OpcionMenuService opcionMenuService;
	LoggerTienda logger = LoggerTienda.getLoggerTienda();

	@GetMapping("/goCategoriaAdmin")
	public String goCategoriaAdmin(@ModelAttribute Categoria categoria, HttpSession session, Model model) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		List<OpcionMenu> listaOpcionMenu = opcionMenuService.obtenerPorRolId(usuario.getRolid());
		session.setAttribute("listaOpcionMenu", listaOpcionMenu);
		List<Categoria> listaCategorias = categoriaService.obtenerTodos();
		model.addAttribute("listaCategorias", listaCategorias);
		model.addAttribute("modificar", false);
		return "categoriaAdmin";
	}

	@PostMapping("/categoriaAdminCrear")
	public String categoriaAdmin(@Valid @ModelAttribute Categoria categoria, BindingResult bindingResult,
			HttpSession session, Model model) {
		List<Categoria> listaCategorias = categoriaService.obtenerTodos();
		model.addAttribute("listaCategorias", listaCategorias);
		model.addAttribute("modificar", false);
		if (bindingResult.hasErrors()) {
			return "categoriaAdmin";
		}

		categoriaService.crear(categoria);
		logger.logInfo("Categoria creado");
		return "redirect:goCategoriaAdmin";
	}

	@PostMapping("/categoriaAdminGoModificar")
	public String categoriaAdminGoModificar(@RequestParam("id") int id, HttpSession session, Model model) {
		List<Categoria> listaCategorias = categoriaService.obtenerTodos();
		model.addAttribute("listaCategorias", listaCategorias);
		Categoria categoria = categoriaService.obtenerPorId(id);
		model.addAttribute("categoria", categoria);
		model.addAttribute("modificar", true);
		return "categoriaAdmin";
	}

	@PostMapping("/categoriaAdminModificar")
	public String categoriaAdminModificar(@Valid @ModelAttribute Categoria categoria, BindingResult bindingResult,
			HttpSession session, Model model) {
		List<Categoria> listaCategorias = categoriaService.obtenerTodos();
		model.addAttribute("listaCategorias", listaCategorias);
		model.addAttribute("modificar", true);
		if (bindingResult.hasErrors()) {
			return "categoriaAdmin";
		}

		categoriaService.modificar(categoria);
		logger.logInfo("Categoria modificado");
		return "redirect:goCategoriaAdmin";
	}

	@GetMapping("/goCategoriaEmpleado")
	public String goCategoriaEmpleado(@ModelAttribute Categoria categoria, HttpSession session, Model model) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		List<OpcionMenu> listaOpcionMenu = opcionMenuService.obtenerPorRolId(usuario.getRolid());
		session.setAttribute("listaOpcionMenu", listaOpcionMenu);
		List<Categoria> listaCategorias = categoriaService.obtenerTodos();
		model.addAttribute("listaCategorias", listaCategorias);
		model.addAttribute("modificar", false);
		return "categoriaEmpleado";
	}

	@PostMapping("/categoriaEmpleadoCrear")
	public String categoriaEmpleado(@Valid @ModelAttribute Categoria categoria, BindingResult bindingResult,
			HttpSession session, Model model) {
		List<Categoria> listaCategorias = categoriaService.obtenerTodos();
		model.addAttribute("listaCategorias", listaCategorias);
		model.addAttribute("modificar", false);
		if (bindingResult.hasErrors()) {
			return "categoriaEmpleado";
		}

		categoriaService.crear(categoria);
		logger.logInfo("Categoria creado");
		return "redirect:goCategoriaEmpleado";
	}

	@PostMapping("/categoriaEmpleadoGoModificar")
	public String categoriaEmpleadoGoModificar(@RequestParam("id") int id, HttpSession session, Model model) {
		List<Categoria> listaCategorias = categoriaService.obtenerTodos();
		model.addAttribute("listaCategorias", listaCategorias);
		Categoria categoria = categoriaService.obtenerPorId(id);
		model.addAttribute("categoria", categoria);
		model.addAttribute("modificar", true);
		return "categoriaEmpleado";
	}

	@PostMapping("/categoriaEmpleadoModificar")
	public String categoriaEmpleadoModificar(@Valid @ModelAttribute Categoria categoria, BindingResult bindingResult,
			HttpSession session, Model model) {
		List<Categoria> listaCategorias = categoriaService.obtenerTodos();
		model.addAttribute("listaCategorias", listaCategorias);
		model.addAttribute("modificar", true);
		if (bindingResult.hasErrors()) {
			return "categoriaEmpleado";
		}

		categoriaService.modificar(categoria);
		logger.logInfo("Categoria modificado");
		return "redirect:goCategoriaEmpleado";
	}

	@PostMapping("/categoriaAdminBaja")
	public String categoriaAdminBaja(@RequestParam("id") int id, @Valid @ModelAttribute Categoria categoria,
			BindingResult bindingResult, HttpSession session, Model model) {
		categoria = categoriaService.obtenerPorId(id);
		categoria.setBaja(true);
		categoriaService.crear(categoria);
		logger.logInfo("Categoria dado de baja");
		return "redirect:goCategoriaAdmin";
	}

	@PostMapping("/categoriaAdminAlta")
	public String categoriaAdminAlta(@RequestParam("id") int id, @Valid @ModelAttribute Categoria categoria,
			BindingResult bindingResult, HttpSession session, Model model) {
		categoria = categoriaService.obtenerPorId(id);
		categoria.setBaja(false);
		categoriaService.crear(categoria);
		logger.logInfo("Categoria dado de baja");
		return "redirect:goCategoriaAdmin";
	}
}
