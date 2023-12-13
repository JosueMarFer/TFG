package curso.java.tienda.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import curso.java.tienda.dto.LoginDTO;
import curso.java.tienda.dto.PassAdminDTO;
import curso.java.tienda.dto.PassDTO;
import curso.java.tienda.dto.PerfilDTO;
import curso.java.tienda.dto.RegisterDTO;
import curso.java.tienda.model.OpcionMenu;
import curso.java.tienda.model.Rol;
import curso.java.tienda.model.Usuario;
import curso.java.tienda.service.ConfiguracionService;
import curso.java.tienda.service.GeneralService;
import curso.java.tienda.service.OpcionMenuService;
import curso.java.tienda.service.RolService;
import curso.java.tienda.service.UsuarioService;
import curso.java.tienda.utils.EncriptadorTienda;
import curso.java.tienda.utils.LoggerTienda;

@Controller
public class UsuarioController {

	@Autowired
	UsuarioService usuarioService;
	@Autowired
	OpcionMenuService opcionMenuService;
	@Autowired
	RolService rolService;
	@Autowired
	ConfiguracionService configuracionService;
	LoggerTienda logger = LoggerTienda.getLoggerTienda();

	@PostConstruct
	public void crearSuperAdmin() {
		if (!usuarioService.comprabarSiSuperAdmin()) {
			Usuario superadmin = new Usuario(0, rolService.obtenerPorRol("superadministrador"), "sadmin@sadmin",
					EncriptadorTienda.encriptar("admin"), "superadmin", "superadmin superadmin", true);
			usuarioService.crear(superadmin);
		}
	}

	@GetMapping("/cerrarSesion")
	public String cerrarSession(HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
		logger.logInfo("Session cerrada");
		return "redirect:";
	}

	@GetMapping("/cerrarSesionAdmin")
	public String cerrarSessionAdmin(HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
		logger.logInfo("Session cerrada");
		return "redirect:/admin";
	}

	@GetMapping("/borrarCarrito")
	public String borrarCarrito(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		if (GeneralService.recuperarCookie(request, "carrito") != null) {
			GeneralService.eliminarCookie("carrito", request, response);
		}
		session.removeAttribute("carrito");
		logger.logInfo("Carro eliminado");
		return "redirect:";
	}

	@GetMapping("/goRegister")
	public String goRegister(@ModelAttribute RegisterDTO registerDTO) {
		return "register";
	}

	@GetMapping("/goPerfil")
	public String goPerfil(@ModelAttribute PerfilDTO perfilDTO, @ModelAttribute PassDTO passDTO, Model model,
			HttpSession session, Usuario usuario) {
		model.addAttribute("activo", "perfil");
		model.addAttribute("tipo", "perfil");
		usuario = (Usuario) session.getAttribute("usuario");
		perfilDTO.setEmail(usuario.getEmail());
		perfilDTO.setNombre(usuario.getNombre());
		perfilDTO.setApellidos(usuario.getApellidos());
		return "perfil";
	}

	@GetMapping("/goAdminPerfil")
	public String goAdminPerfil(@ModelAttribute PerfilDTO perfilDTO, @ModelAttribute PassDTO passDTO, Model model,
			HttpSession session, Usuario usuario) {
		model.addAttribute("activo", "perfil");
		model.addAttribute("tipo", "perfil");
		usuario = (Usuario) session.getAttribute("usuario");
		perfilDTO.setEmail(usuario.getEmail());
		perfilDTO.setNombre(usuario.getNombre());
		perfilDTO.setApellidos(usuario.getApellidos());
		return "perfilAdmin";
	}

	@PostMapping("/registro")
	public String registrarUsuario(@Valid @ModelAttribute RegisterDTO registerDTO, BindingResult bindingResult,
			HttpSession session, Model model) {

		if (bindingResult.hasErrors()) {
			return "register";
		}

		if (!usuarioService.comprobarPassIguales(registerDTO.getPass(), registerDTO.getPassTwo())) {
			FieldError error = new FieldError("registerDTO", "passTwo", "las contraseñas no coinciden");
			bindingResult.addError(error);
			return "register";
		}

		if (usuarioService.obtenerPorEmail(registerDTO.getEmail()) != null) {
			FieldError error = new FieldError("registerDTO", "email", "El email ya existe por favor elija otro");
			bindingResult.addError(error);
			return "register";
		} else {
			Usuario usuario = usuarioService.registerDTOToUser(registerDTO);
			usuario = usuarioService.crear(usuario);
			session.setAttribute("usuario", usuario);
			logger.logInfo("Usuario Registrado");
			model.addAttribute("activo", "inicio");
			return "redirect:";
		}
	}

	@PostMapping("/modificarPerfil")
	public String modificarPerfil(@Valid @ModelAttribute PerfilDTO perfilDTO, BindingResult bindingResulta,
			HttpSession session, Model model, Usuario usuario, @Valid @ModelAttribute PassDTO passDTO,
			BindingResult bindingResultb) {

		model.addAttribute("tipo", "perfil");

		if (bindingResulta.hasErrors()) {
			return "perfil";
		}
		usuario = (Usuario) session.getAttribute("usuario");
		usuario.setEmail(perfilDTO.getEmail());
		usuario.setNombre(perfilDTO.getNombre());
		usuario.setApellidos(perfilDTO.getApellidos());
		usuario = usuarioService.modificar(usuario);
		session.setAttribute("usuario", usuario);
		logger.logInfo("Usuario modificado");
		return "redirect:";
	}

	@PostMapping("/modificarPass")
	public String modificarPass(@Valid @ModelAttribute PassDTO passDTO, BindingResult bindingResulta,
			HttpSession session, Model model, Usuario usuario, @Valid @ModelAttribute PerfilDTO perfilDTO,
			BindingResult bindingResultb) {

		model.addAttribute("tipo", "pass");

		if (bindingResulta.hasErrors()) {
			return "perfil";
		}

		if (!usuarioService.comprobarPassIguales(passDTO.getPass(), passDTO.getPassTwo())) {
			FieldError error = new FieldError("passDTO", "passTwo", "las contraseñas no coinciden");
			bindingResulta.addError(error);
			return "perfil";
		}

		if (usuarioService.comprobarPassAnteriorIgual(passDTO.getPassLast(), passDTO.getPass())) {
			FieldError error = new FieldError("passDTO", "pass", "la contraseña no puede ser igual que la enterior");
			bindingResulta.addError(error);
			return "perfil";
		}

		usuario = (Usuario) session.getAttribute("usuario");

		if (!usuarioService.comprobarPassAnteriorCorrecta(passDTO.getPassLast(), usuario.getPass())) {
			FieldError error = new FieldError("passDTO", "passLast",
					"la contraseña no coincide con la contraseña almacenada en la base de datos");
			bindingResulta.addError(error);
			logger.logInfo("Contraseña del usuario modificada");
			return "perfil";
		}

		usuario.setPass(EncriptadorTienda.encriptar(passDTO.getPass()));
		usuario = usuarioService.modificar(usuario);
		session.setAttribute("usuario", usuario);
		return "redirect:";
	}

	@GetMapping("/goLogin")
	public String goLogin(@ModelAttribute LoginDTO loginDTO) {
		return "login";
	}

	@PostMapping("/login")
	public String LoguearUsuario(@Valid @ModelAttribute LoginDTO loginDTO, BindingResult bindingResult,
			HttpSession session, Model model) {

		if (bindingResult.hasErrors()) {
			return "login";
		}

		if (usuarioService.comprobarLogin(loginDTO.getEmail(), loginDTO.getPass())) {
			Usuario usuario = usuarioService.obtenerPorEmail(loginDTO.getEmail());
			session.setAttribute("usuario", usuario);
			List<OpcionMenu> listaOpcionMenu = opcionMenuService.obtenerPorRolId(usuario.getRolid());
			session.setAttribute("listaOpcionMenu", listaOpcionMenu);
			logger.logInfo("Usuario Logeado");
			model.addAttribute("activo", "inicio");
			return "redirect:";
		} else {
			FieldError error = new FieldError("loginDTO", "pass", "El email o la contraseña no son correctos");
			bindingResult.addError(error);
			return "login";
		}
	}

	@GetMapping("/admin")
	public String goLoginAdmin(@ModelAttribute LoginDTO loginDTO) {
		return "loginAdmin";
	}

	@PostMapping("/loginAdmin")
	public String LoguearAdministradorEmpleado(@Valid @ModelAttribute LoginDTO loginDTO, BindingResult bindingResult,
			HttpSession session, Model model) {

		if (usuarioService.comprobarLoginAdmin(loginDTO.getEmail(), loginDTO.getPass())) {
			Usuario usuario = usuarioService.obtenerPorEmail(loginDTO.getEmail());
			if (usuario.getRolid().getRol().equals("superadministrador")
					&& configuracionService.comprobarSiConexion()) {
				session.setAttribute("usuario", usuario);
				return "redirect:/goCambiarPassword";
			}
			if (bindingResult.hasErrors()) {
				return "loginAdmin";
			}
			session.setAttribute("usuario", usuario);
			List<OpcionMenu> listaOpcionMenu = opcionMenuService.obtenerPorRolId(usuario.getRolid());
			session.setAttribute("listaOpcionMenu", listaOpcionMenu);
			logger.logInfo("empleado/administrador Logeado");
			model.addAttribute("activo", "inicio");
			if (usuario.getRolid().getRol().equals("empleado")) {
				return "redirect:/goProductoEmpleado";
			} else {
				return "redirect:/goProductoAdmin";
			}

		} else {
			FieldError error = new FieldError("loginDTO", "pass", "El email o la contraseña no son correctos");
			bindingResult.addError(error);
			return "loginAdmin";
		}
	}

	@GetMapping("/goCambiarPassword")
	public String goCambiarPassword(@ModelAttribute PassAdminDTO passAdminDTO, @ModelAttribute Usuario usuario,
			HttpSession session, Model model) {
		usuario = (Usuario) session.getAttribute("usuario");
		return "cambiarPassword";
	}

	@PostMapping("/cambiarPassword")
	public String cambiarPassword(@Valid @ModelAttribute PassAdminDTO passAdminDTO, BindingResult bindingResult,
			HttpSession session, Model model, @ModelAttribute Usuario usuario) {

		if (bindingResult.hasErrors()) {
			return "cambiarPassword";
		}

		if (!usuarioService.comprobarPassIguales(passAdminDTO.getPass(), passAdminDTO.getPassTwo())) {
			FieldError error = new FieldError("passDTO", "passTwo", "las contraseñas no coinciden");
			bindingResult.addError(error);
			return "cambiarPassword";
		}

		usuario = (Usuario) session.getAttribute("usuario");

		if (!usuarioService.comprobarPassAnteriorCorrecta(passAdminDTO.getPassLast(), usuario.getPass())) {
			FieldError error = new FieldError("passDTO", "passLast",
					"la contraseña no coincide con la contraseña almacenada en la base de datos");
			bindingResult.addError(error);
			logger.logInfo("Contraseña del superAdministrador modificada");
			return "cambiarPassword";
		}

		usuario.setPass(EncriptadorTienda.encriptar(passAdminDTO.getPass()));
		usuario = usuarioService.modificar(usuario);
		session.setAttribute("usuario", usuario);
		configuracionService.conexionToFalse();
		return "redirect:goProductoAdmin";
	}

	@GetMapping("/goUsuarioAdmin")
	public String goUsuarioAdmin(@ModelAttribute RegisterDTO registerDTO, @ModelAttribute Usuario usuario,
			HttpSession session, Model model) {
		usuario = (Usuario) session.getAttribute("usuario");
		List<Usuario> listaUsuarios = usuarioService.findClientesYEmpleados();
		model.addAttribute("listaUsuarios", listaUsuarios);
		List<Rol> listaRol = rolService.findByClientesYEmpleados();
		model.addAttribute("listaRol", listaRol);
		model.addAttribute("modificar", false);
		return "usuarioAdmin";
	}

	@PostMapping("/usuarioAdminCrear")
	public String usuarioAdmin(@Valid @ModelAttribute RegisterDTO registerDTO, BindingResult bindingResultDTO,
			@Valid @ModelAttribute Usuario usuario, BindingResult bindingResultUsuario, HttpSession session,
			Model model) {
		model.addAttribute("modificar", false);
		List<Usuario> listaUsuarios = usuarioService.findClientesYEmpleados();
		model.addAttribute("listaUsuarios", listaUsuarios);
		List<Rol> listaRol = rolService.findByClientesYEmpleados();
		model.addAttribute("listaRol", listaRol);
		if (usuarioService.obtenerPorEmail(usuario.getEmail()) != null) {
			FieldError error = new FieldError("registerDTO", "email", "El email ya existe por favor elija otro");
			bindingResultUsuario.addError(error);
		}
		if (bindingResultDTO.hasFieldErrors("pass") || bindingResultUsuario.hasErrors()) {
			return "usuarioAdmin";
		}
		usuario.setPass(EncriptadorTienda.encriptar(registerDTO.getPass()));
		usuarioService.crear(usuario);
		logger.logInfo("Usuario creado");
		return "redirect:goUsuarioAdmin";
	}

	@PostMapping("/usuarioAdminGoModificar")
	public String usuarioAdminGoModificar(@ModelAttribute RegisterDTO registerDTO, @RequestParam("id") int id,
			HttpSession session, Model model) {
		List<Usuario> listaUsuarios = usuarioService.findClientesYEmpleados();
		model.addAttribute("listaUsuarios", listaUsuarios);
		Usuario usuario = usuarioService.obtenerPorId(id);
		List<Rol> listaRol = rolService.findByClientesYEmpleados();
		model.addAttribute("listaRol", listaRol);
		model.addAttribute("usuario", usuario);
		model.addAttribute("registerDTO", registerDTO);
		model.addAttribute("modificar", true);
		session.setAttribute("emailActual", usuario.getEmail());
		return "usuarioAdmin";
	}

	@PostMapping("/usuarioAdminModificar")
	public String usuarioAdminModificar(@Valid @ModelAttribute RegisterDTO registerDTO, BindingResult bindingResultDTO,
			@Valid @ModelAttribute Usuario usuario, BindingResult bindingResultUsuario, HttpSession session,
			Model model) {
		model.addAttribute("modificar", true);
		List<Usuario> listaUsuarios = usuarioService.findClientesYEmpleados();
		model.addAttribute("listaUsuarios", listaUsuarios);
		List<Rol> listaRol = rolService.findByClientesYEmpleados();
		model.addAttribute("listaRol", listaRol);
		if (usuarioService.obtenerPorEmail(usuario.getEmail()) != null
				&& !usuario.getEmail().equals(session.getAttribute("emailActual"))) {
			FieldError error = new FieldError("registerDTO", "email", "El email ya existe por favor elija otro");
			bindingResultUsuario.addError(error);
		}
		if (bindingResultDTO.hasFieldErrors("pass") || bindingResultUsuario.hasErrors()) {
			return "usuarioAdmin";
		}
		usuario.setPass(EncriptadorTienda.encriptar(registerDTO.getPass()));
		session.removeAttribute("emailActual");
		usuarioService.modificar(usuario);
		logger.logInfo("Usuario modificado");
		return "redirect:goUsuarioAdmin";
	}

	@GetMapping("/goUsuarioEmpleado")
	public String goUsuarioEmpleado(@ModelAttribute RegisterDTO registerDTO, @ModelAttribute Usuario usuario,
			HttpSession session, Model model) {
		usuario = (Usuario) session.getAttribute("usuario");
		List<Usuario> listaUsuarios = usuarioService.findClientes();
		model.addAttribute("listaUsuarios", listaUsuarios);
		model.addAttribute("modificar", false);
		return "usuarioEmpleado";
	}

	@PostMapping("/usuarioEmpleadoCrear")
	public String usuarioEmpleado(@Valid @ModelAttribute RegisterDTO registerDTO, BindingResult bindingResultDTO,
			@Valid @ModelAttribute Usuario usuario, BindingResult bindingResultUsuario, HttpSession session,
			Model model) {
		model.addAttribute("modificar", false);
		usuario.setRolid(rolService.obtenerPorRol("cliente"));
		List<Usuario> listaUsuarios = usuarioService.findClientes();
		model.addAttribute("listaUsuarios", listaUsuarios);
		if (usuarioService.obtenerPorEmail(usuario.getEmail()) != null) {
			FieldError error = new FieldError("registerDTO", "email", "El email ya existe por favor elija otro");
			bindingResultUsuario.addError(error);
		}
		if (bindingResultDTO.hasFieldErrors("pass") || bindingResultUsuario.hasErrors()) {
			return "usuarioEmpleado";
		}
		usuario.setPass(EncriptadorTienda.encriptar(registerDTO.getPass()));
		usuarioService.crear(usuario);
		logger.logInfo("Usuario creado");
		return "redirect:goUsuarioEmpleado";
	}

	@PostMapping("/usuarioEmpleadoGoModificar")
	public String usuarioEmpleadoGoModificar(@ModelAttribute RegisterDTO registerDTO, @RequestParam("id") int id,
			HttpSession session, Model model) {
		List<Usuario> listaUsuarios = usuarioService.findClientes();
		model.addAttribute("listaUsuarios", listaUsuarios);
		Usuario usuario = usuarioService.obtenerPorId(id);
		model.addAttribute("usuario", usuario);
		model.addAttribute("registerDTO", registerDTO);
		model.addAttribute("modificar", true);
		session.setAttribute("emailActual", usuario.getEmail());
		return "usuarioEmpleado";
	}

	@PostMapping("/usuarioEmpleadoModificar")
	public String usuarioEmpleadoModificar(@Valid @ModelAttribute RegisterDTO registerDTO,
			BindingResult bindingResultDTO, @Valid @ModelAttribute Usuario usuario, BindingResult bindingResultUsuario,
			HttpSession session, Model model) {
		model.addAttribute("modificar", true);
		usuario.setRolid(rolService.obtenerPorRol("cliente"));
		List<Usuario> listaUsuarios = usuarioService.findClientes();
		model.addAttribute("listaUsuarios", listaUsuarios);
		if (usuarioService.obtenerPorEmail(usuario.getEmail()) != null
				&& !usuario.getEmail().equals(session.getAttribute("emailActual"))) {
			FieldError error = new FieldError("registerDTO", "email", "El email ya existe por favor elija otro");
			bindingResultUsuario.addError(error);
		}
		if (bindingResultDTO.hasFieldErrors("pass") || bindingResultUsuario.hasErrors()) {
			return "usuarioEmpleado";
		}
		usuario.setPass(EncriptadorTienda.encriptar(registerDTO.getPass()));
		session.removeAttribute("emailActual");
		usuarioService.modificar(usuario);
		logger.logInfo("Usuario modificado");
		return "redirect:goUsuarioEmpleado";
	}

	@PostMapping("/usuarioAdminBaja")
	public String usuarioAdminBaja(@RequestParam("id") int id, @Valid @ModelAttribute Usuario usuario,
			BindingResult bindingResult, HttpSession session, Model model) {
		usuario = usuarioService.obtenerPorId(id);
		usuario.setBaja(true);
		usuarioService.crear(usuario);
		logger.logInfo("Usuario dado de baja");
		return "redirect:goUsuarioAdmin";
	}

	@PostMapping("/usuarioAdminAlta")
	public String usuarioAdminAlta(@RequestParam("id") int id, @Valid @ModelAttribute Usuario usuario,
			BindingResult bindingResult, HttpSession session, Model model) {
		usuario = usuarioService.obtenerPorId(id);
		usuario.setBaja(false);
		usuarioService.crear(usuario);
		logger.logInfo("Usuario dado de baja");
		return "redirect:goUsuarioAdmin";
	}

	@GetMapping("/goUsuarioSuperAdmin")
	public String goUsuarioSuperAdmin(@ModelAttribute RegisterDTO registerDTO, @ModelAttribute Usuario usuario,
			HttpSession session, Model model) {
		usuario = (Usuario) session.getAttribute("usuario");
		List<Usuario> listaUsuarios = usuarioService.findClientesYEmpleadosYAdmins();
		model.addAttribute("listaUsuarios", listaUsuarios);
		List<Rol> listaRol = rolService.findByClientesYEmpleadosYAdministradores();
		model.addAttribute("listaRol", listaRol);
		model.addAttribute("modificar", false);
		return "usuarioSuperAdmin";
	}

	@PostMapping("/usuarioSuperAdminCrear")
	public String usuarioSuperAdmin(@Valid @ModelAttribute RegisterDTO registerDTO, BindingResult bindingResultDTO,
			@Valid @ModelAttribute Usuario usuario, BindingResult bindingResultUsuario, HttpSession session,
			Model model) {
		model.addAttribute("modificar", false);
		List<Usuario> listaUsuarios = usuarioService.findClientesYEmpleadosYAdmins();
		model.addAttribute("listaUsuarios", listaUsuarios);
		List<Rol> listaRol = rolService.findByClientesYEmpleadosYAdministradores();
		model.addAttribute("listaRol", listaRol);
		if (usuarioService.obtenerPorEmail(usuario.getEmail()) != null) {
			FieldError error = new FieldError("registerDTO", "email", "El email ya existe por favor elija otro");
			bindingResultUsuario.addError(error);
		}
		if (bindingResultDTO.hasFieldErrors("pass") || bindingResultUsuario.hasErrors()) {
			return "usuarioSuperAdmin";
		}
		usuario.setPass(EncriptadorTienda.encriptar(registerDTO.getPass()));
		usuarioService.crear(usuario);
		logger.logInfo("Usuario creado");
		return "redirect:goUsuarioSuperAdmin";
	}

	@PostMapping("/usuarioSuperAdminGoModificar")
	public String usuarioSuperAdminGoModificar(@ModelAttribute RegisterDTO registerDTO, @RequestParam("id") int id,
			HttpSession session, Model model) {
		List<Usuario> listaUsuarios = usuarioService.findClientesYEmpleadosYAdmins();
		model.addAttribute("listaUsuarios", listaUsuarios);
		Usuario usuario = usuarioService.obtenerPorId(id);
		List<Rol> listaRol = rolService.findByClientesYEmpleadosYAdministradores();
		model.addAttribute("listaRol", listaRol);
		model.addAttribute("usuario", usuario);
		model.addAttribute("registerDTO", registerDTO);
		model.addAttribute("modificar", true);
		session.setAttribute("emailActual", usuario.getEmail());
		return "usuarioSuperAdmin";
	}

	@PostMapping("/usuarioSuperAdminModificar")
	public String usuarioSuperAdminModificar(@Valid @ModelAttribute RegisterDTO registerDTO,
			BindingResult bindingResultDTO, @Valid @ModelAttribute Usuario usuario, BindingResult bindingResultUsuario,
			HttpSession session, Model model) {
		model.addAttribute("modificar", true);
		List<Usuario> listaUsuarios = usuarioService.findClientesYEmpleados();
		model.addAttribute("listaUsuarios", listaUsuarios);
		List<Rol> listaRol = rolService.findByClientesYEmpleadosYAdministradores();
		model.addAttribute("listaRol", listaRol);
		if (usuarioService.obtenerPorEmail(usuario.getEmail()) != null
				&& !usuario.getEmail().equals(session.getAttribute("emailActual"))) {
			FieldError error = new FieldError("registerDTO", "email", "El email ya existe por favor elija otro");
			bindingResultUsuario.addError(error);
		}
		if (bindingResultDTO.hasFieldErrors("pass") || bindingResultUsuario.hasErrors()) {
			return "usuarioSuperAdmin";
		}
		usuario.setPass(EncriptadorTienda.encriptar(registerDTO.getPass()));
		session.removeAttribute("emailActual");
		usuarioService.modificar(usuario);
		logger.logInfo("Usuario modificado");
		return "redirect:goUsuarioSuperAdmin";
	}

	@PostMapping("/usuarioSuperAdminBaja")
	public String usuarioSuperAdminBaja(@RequestParam("id") int id, @Valid @ModelAttribute Usuario usuario,
			BindingResult bindingResult, HttpSession session, Model model) {
		usuario = usuarioService.obtenerPorId(id);
		usuario.setBaja(true);
		usuarioService.crear(usuario);
		logger.logInfo("Usuario dado de baja");
		return "redirect:goUsuarioSuperAdmin";
	}

	@PostMapping("/usuarioSuperAdminAlta")
	public String usuarioSuperAdminAlta(@RequestParam("id") int id, @Valid @ModelAttribute Usuario usuario,
			BindingResult bindingResult, HttpSession session, Model model) {
		usuario = usuarioService.obtenerPorId(id);
		usuario.setBaja(false);
		usuarioService.crear(usuario);
		logger.logInfo("Usuario dado de baja");
		return "redirect:goUsuarioSuperAdmin";
	}

	@PostMapping("/modificarAdminPerfil")
	public String modificarAdminPerfil(@Valid @ModelAttribute PerfilDTO perfilDTO, BindingResult bindingResulta,
			HttpSession session, Model model, Usuario usuario, @Valid @ModelAttribute PassDTO passDTO,
			BindingResult bindingResultb) {

		model.addAttribute("tipo", "perfil");

		if (bindingResulta.hasErrors()) {
			return "perfilAdmin";
		}
		usuario = (Usuario) session.getAttribute("usuario");
		usuario.setEmail(perfilDTO.getEmail());
		usuario.setNombre(perfilDTO.getNombre());
		usuario.setApellidos(perfilDTO.getApellidos());
		usuario = usuarioService.modificar(usuario);
		session.setAttribute("usuario", usuario);
		logger.logInfo("Usuario modificado");
		if (usuario.getRolid().getRol().equals("empleado")) {
			return "redirect:/goProductoEmpleado";
		} else if (usuario.getRolid().getRol().equals("administrador")) {
			return "redirect:/goProductoAdmin";
		} else if (usuario.getRolid().getRol().equals("superadministrador")) {
			return "redirect:/goProductoAdmin";
		}
		return "redirect:/goAdminPerfil";
	}

	@PostMapping("/modificarAdminPass")
	public String modificarAdminPass(@Valid @ModelAttribute PassDTO passDTO, BindingResult bindingResulta,
			HttpSession session, Model model, Usuario usuario, @Valid @ModelAttribute PerfilDTO perfilDTO,
			BindingResult bindingResultb) {

		model.addAttribute("tipo", "pass");

		if (bindingResulta.hasErrors()) {
			return "perfilAdmin";
		}

		if (!usuarioService.comprobarPassIguales(passDTO.getPass(), passDTO.getPassTwo())) {
			FieldError error = new FieldError("passDTO", "passTwo", "las contraseñas no coinciden");
			bindingResulta.addError(error);
			return "perfilAdmin";
		}

		if (usuarioService.comprobarPassAnteriorIgual(passDTO.getPassLast(), passDTO.getPass())) {
			FieldError error = new FieldError("passDTO", "pass", "la contraseña no puede ser igual que la enterior");
			bindingResulta.addError(error);
			return "perfilAdmin";
		}

		usuario = (Usuario) session.getAttribute("usuario");

		if (!usuarioService.comprobarPassAnteriorCorrecta(passDTO.getPassLast(), usuario.getPass())) {
			FieldError error = new FieldError("passDTO", "passLast",
					"la contraseña no coincide con la contraseña almacenada en la base de datos");
			bindingResulta.addError(error);
			logger.logInfo("Contraseña del usuario modificada");
			return "perfilAdmin";
		}

		usuario.setPass(EncriptadorTienda.encriptar(passDTO.getPass()));
		usuario = usuarioService.modificar(usuario);
		session.setAttribute("usuario", usuario);
		if (usuario.getRolid().getRol().equals("empleado")) {
			return "redirect:/goProductoEmpleado";
		} else if (usuario.getRolid().getRol().equals("administrador")) {
			return "redirect:/goProductoAdmin";
		} else if (usuario.getRolid().getRol().equals("superadministrador")) {
			return "redirect:/goProductoAdmin";
		}
		return "redirect:/goAdminPerfil";
	}

}
