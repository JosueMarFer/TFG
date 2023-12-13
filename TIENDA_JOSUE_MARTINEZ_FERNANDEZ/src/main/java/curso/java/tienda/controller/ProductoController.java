package curso.java.tienda.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import curso.java.tienda.dto.CatalogoDTO;
import curso.java.tienda.dto.ImagenDTO;
import curso.java.tienda.dto.PagoDTO;
import curso.java.tienda.model.Categoria;
import curso.java.tienda.model.OpcionMenu;
import curso.java.tienda.model.Producto;
import curso.java.tienda.model.Usuario;
import curso.java.tienda.service.CategoriaService;
import curso.java.tienda.service.GeneralService;
import curso.java.tienda.service.OpcionMenuService;
import curso.java.tienda.service.ProductoService;
import curso.java.tienda.utils.LoggerTienda;

@Controller
@RequestMapping("")
public class ProductoController {

	@Autowired
	ProductoService productoService;
	@Autowired
	CategoriaService categoriaService;
	@Autowired
	OpcionMenuService opcionMenuService;
	LoggerTienda logger = LoggerTienda.getLoggerTienda();

	@GetMapping("")
	public String inicio(HttpServletRequest request, HttpSession session, Model model,
			@Valid @ModelAttribute CatalogoDTO catalogoDTO, BindingResult bindingResult) {
		List<Categoria> listaCategorias = categoriaService.obtenerTodosSinBaja();
		model.addAttribute("listaCategorias", listaCategorias);
		if (bindingResult.hasErrors() || model.getAttribute("pasadoPorPost") == null) {
			List<Producto> listaProductos = productoService.obtenerTodosSinBaja();
			model.addAttribute("listaProductos", listaProductos);
		} else {
			List<Producto> listaProductos = productoService.buscarProductosConFiltros(catalogoDTO.getPrecioMin(),
					catalogoDTO.getPrecioMax(), categoriaService.obtenerPorNombre(catalogoDTO.getCategoria()),
					catalogoDTO.getStockMin());
			model.addAttribute("listaProductos", listaProductos);
		}

		GeneralService.recuperarCarrito(request, session);
		if (session.getAttribute("usuario") != null) {
			Usuario usuario = (Usuario) session.getAttribute("usuario");
			List<OpcionMenu> listaOpcionMenu = opcionMenuService.obtenerPorRolId(usuario.getRolid());
			session.setAttribute("listaOpcionMenu", listaOpcionMenu);
		}

		model.addAttribute("activo", "inicio");
		return "inicio";
	}

	@PostMapping("/")
	public String postInicio(HttpServletRequest request, HttpSession session, Model model,
			@Valid @ModelAttribute CatalogoDTO catalogoDTO, BindingResult bindingResult) {
		model.addAttribute("pasadoPorPost", "pasadoPorPost");
		inicio(request, session, model, catalogoDTO, bindingResult);
		return "inicio";
	}

	@PostMapping("/addProductoToCarrito")
	public String inicio(@RequestParam String id, @RequestParam String cantidadProducto, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Integer> carrito = (HashMap<String, Integer>) session.getAttribute("carrito");
		if (cantidadProducto != null && !cantidadProducto.equals("0")) {
			productoService.addProductoToCarrito(id, cantidadProducto, carrito);
			session.setAttribute("carrito", carrito);
			String carritojson = new Gson().toJson(carrito);
			GeneralService.setCookie("carrito", carritojson, request, response);
			logger.logInfo("Articulo añadido al carrito y a la cookie");
		}
		return "redirect:";
	}

	@GetMapping("/carro")
	public String toCarrito(Model model, HttpSession session) {
		HashMap<String, Integer> carrito = (HashMap<String, Integer>) session.getAttribute("carrito");
		List<Producto> listaProductos = productoService.returnProductosToCarrito(carrito);
		model.addAttribute("listaProductos", listaProductos);
		if (listaProductos != null) {
			productoService.comprobarStock(carrito, listaProductos, model);
			productoService.comprobarTotalPrecioAddModelo(carrito, listaProductos, model);
		}
		return "carrito";
	}

	@PostMapping("/modificarCarrito")
	public String modificarCarrito(@RequestParam String id, @RequestParam String cantidadProducto, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Integer> carrito = (HashMap<String, Integer>) session.getAttribute("carrito");
		if (cantidadProducto != null) {
			if (!cantidadProducto.equals("0")) {
				productoService.modProductoToCarrito(id, cantidadProducto, carrito);
				session.setAttribute("carrito", carrito);
				String carritojson = new Gson().toJson(carrito);
				GeneralService.setCookie("carrito", carritojson, request, response);
				logger.logInfo("Cantidad del articulo modificada en el carrito y la cookie");
			} else {
				productoService.deleteProductoToCarrito(id, carrito);
				session.setAttribute("carrito", carrito);
				String carritojson = new Gson().toJson(carrito);
				GeneralService.setCookie("carrito", carritojson, request, response);
				logger.logInfo("Articulo eliminado en el carrito y la cookie");
			}
		}
		return "redirect:carro";
	}

	@PostMapping("/toPay")
	public String toPay(@ModelAttribute PagoDTO pagoDTO, HttpSession session) {
		if (session.getAttribute("usuario") == null) {
			return "redirect:/goLogin";
		} else {
			return "pago";
		}
	}

	@GetMapping("/goProductoAdmin")
	public String goProductoAdmin(@ModelAttribute Producto producto, HttpSession session, Model model,
			@ModelAttribute ImagenDTO imagenDTO) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		List<OpcionMenu> listaOpcionMenu = opcionMenuService.obtenerPorRolId(usuario.getRolid());
		session.setAttribute("listaOpcionMenu", listaOpcionMenu);
		List<Producto> listaProductos = productoService.obtenerTodos();
		model.addAttribute("listaProductos", listaProductos);
		List<Categoria> listaCategorias = categoriaService.obtenerTodos();
		model.addAttribute("listaCategorias", listaCategorias);
		model.addAttribute("modificar", false);
		return "productoAdmin";
	}

	@PostMapping("/productoAdminCrear")
	public String productoAdmin(@Valid @ModelAttribute Producto producto, BindingResult bindingResult,
			HttpSession session, Model model, @Valid @ModelAttribute ImagenDTO imagenDTO,
			BindingResult bindingResultImagen, HttpServletRequest request) {
		List<Categoria> listaCategorias = categoriaService.obtenerTodos();
		model.addAttribute("listaCategorias", listaCategorias);
		List<Producto> listaProductos = productoService.obtenerTodos();
		model.addAttribute("listaProductos", listaProductos);
		model.addAttribute("modificar", false);
		if (imagenDTO.getImagen().isEmpty()) {
			FieldError error = new FieldError("imagenDTO", "imagen", "la imagen no puede estar vacia");
			bindingResultImagen.addError(error);
		}
		if (bindingResult.hasErrors() || bindingResultImagen.hasErrors()) {
			return "productoAdmin";
		}
		byte[] bytes;
		try {
			bytes = imagenDTO.getImagen().getBytes();
			Path rutaDestino = Paths.get(
					"C:/workspace-spring-tool-suite-4-4.3.2.RELEASE/TIENDA_JOSUE_MARTINEZ_FERNANDEZ/src/main/resources/"
							+ "static/images/" + "p" + Integer.toString(productoService.contarProductosMasUno())
							+ ".jpg");
			Files.write(rutaDestino, bytes);
		} catch (IOException e) {
			logger.logError("Problema al subir la imagen");
		}
		producto.setImagenname("p" + Integer.toString(productoService.contarProductosMasUno()));
		productoService.crear(producto);
		logger.logInfo("Producto creado");
		return "redirect:goProductoAdmin";
	}

	@PostMapping("/productoAdminGoModificar")
	public String productoAdminGoModificar(@RequestParam("id") int id, HttpSession session, Model model,
			@ModelAttribute ImagenDTO imagenDTO) {
		List<Categoria> listaCategorias = categoriaService.obtenerTodos();
		model.addAttribute("listaCategorias", listaCategorias);
		List<Producto> listaProductos = productoService.obtenerTodos();
		model.addAttribute("listaProductos", listaProductos);
		Producto producto = productoService.obtenerPorId(id);
		model.addAttribute("producto", producto);
		model.addAttribute("modificar", true);
		return "productoAdmin";
	}

	@PostMapping("/productoAdminModificar")
	public String productoAdminModificar(@Valid @ModelAttribute Producto producto, BindingResult bindingResult,
			HttpSession session, Model model, @Valid @ModelAttribute ImagenDTO imagenDTO,
			BindingResult bindingResultImagen, HttpServletRequest request) {
		List<Categoria> listaCategorias = categoriaService.obtenerTodos();
		model.addAttribute("listaCategorias", listaCategorias);
		List<Producto> listaProductos = productoService.obtenerTodos();
		model.addAttribute("listaProductos", listaProductos);
		model.addAttribute("modificar", true);
		if (imagenDTO.getImagen().isEmpty()) {
			FieldError error = new FieldError("imagenDTO", "imagen", "la imagen no puede estar vacia");
			bindingResultImagen.addError(error);
		}
		if (bindingResult.hasErrors() || bindingResultImagen.hasErrors()) {
			return "productoAdmin";
		}
		byte[] bytes;
		try {
			bytes = imagenDTO.getImagen().getBytes();
			Path rutaDestino = Paths.get(
					"C:/workspace-spring-tool-suite-4-4.3.2.RELEASE/TIENDA_JOSUE_MARTINEZ_FERNANDEZ/src/main/resources/"
							+ "static/images/" + producto.getImagenname() + ".jpg");
			Files.write(rutaDestino, bytes);
		} catch (IOException e) {
			logger.logError("Problema al subir la imagen");
		}
		productoService.modificar(producto);
		logger.logInfo("Producto modificado");
		return "redirect:goProductoAdmin";
	}

	@GetMapping("/goProductoEmpleado")
	public String goProductoEmpleado(@ModelAttribute Producto producto, HttpSession session, Model model,
			@ModelAttribute ImagenDTO imagenDTO) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		List<OpcionMenu> listaOpcionMenu = opcionMenuService.obtenerPorRolId(usuario.getRolid());
		session.setAttribute("listaOpcionMenu", listaOpcionMenu);
		List<Producto> listaProductos = productoService.obtenerTodos();
		model.addAttribute("listaProductos", listaProductos);
		List<Categoria> listaCategorias = categoriaService.obtenerTodos();
		model.addAttribute("listaCategorias", listaCategorias);
		model.addAttribute("modificar", false);
		return "productoEmpleado";
	}

	@PostMapping("/productoEmpleadoCrear")
	public String productoEmpleado(@Valid @ModelAttribute Producto producto, BindingResult bindingResult,
			HttpSession session, Model model, @Valid @ModelAttribute ImagenDTO imagenDTO,
			BindingResult bindingResultImagen, HttpServletRequest request) {
		List<Categoria> listaCategorias = categoriaService.obtenerTodos();
		model.addAttribute("listaCategorias", listaCategorias);
		List<Producto> listaProductos = productoService.obtenerTodos();
		model.addAttribute("listaProductos", listaProductos);
		model.addAttribute("modificar", false);
		if (imagenDTO.getImagen().isEmpty()) {
			FieldError error = new FieldError("imagenDTO", "imagen", "la imagen no puede estar vacia");
			bindingResultImagen.addError(error);
		}
		if (bindingResult.hasErrors() || bindingResultImagen.hasErrors()) {
			return "productoEmpleado";
		}

		byte[] bytes;
		try {
			bytes = imagenDTO.getImagen().getBytes();
			Path rutaDestino = Paths.get(
					"C:/workspace-spring-tool-suite-4-4.3.2.RELEASE/TIENDA_JOSUE_MARTINEZ_FERNANDEZ/src/main/resources/"
							+ "static/images/" + "p" + Integer.toString(productoService.contarProductosMasUno())
							+ ".jpg");
			Files.write(rutaDestino, bytes);
		} catch (IOException e) {
			logger.logError("Problema al subir la imagen");
		}

		producto.setImagenname("p" + Integer.toString(productoService.contarProductosMasUno()));
		productoService.crear(producto);
		logger.logInfo("Producto creado");
		return "redirect:goProductoEmpleado";
	}

	@PostMapping("/productoEmpleadoGoModificar")
	public String productoEmpleadoGoModificar(@RequestParam("id") int id, HttpSession session, Model model,
			@ModelAttribute ImagenDTO imagenDTO) {
		List<Categoria> listaCategorias = categoriaService.obtenerTodos();
		model.addAttribute("listaCategorias", listaCategorias);
		List<Producto> listaProductos = productoService.obtenerTodos();
		model.addAttribute("listaProductos", listaProductos);
		Producto producto = productoService.obtenerPorId(id);
		model.addAttribute("producto", producto);
		model.addAttribute("modificar", true);
		return "productoEmpleado";
	}

	@PostMapping("/productoEmpleadoModificar")
	public String productoEmpleadoModificar(@Valid @ModelAttribute Producto producto, BindingResult bindingResult,
			HttpSession session, Model model, @Valid @ModelAttribute ImagenDTO imagenDTO,
			BindingResult bindingResultImagen, HttpServletRequest request) {
		List<Categoria> listaCategorias = categoriaService.obtenerTodos();
		model.addAttribute("listaCategorias", listaCategorias);
		List<Producto> listaProductos = productoService.obtenerTodos();
		model.addAttribute("listaProductos", listaProductos);
		model.addAttribute("modificar", true);
		if (imagenDTO.getImagen().isEmpty()) {
			FieldError error = new FieldError("imagenDTO", "imagen", "la imagen no puede estar vacia");
			bindingResultImagen.addError(error);
		}
		if (bindingResult.hasErrors() || bindingResultImagen.hasErrors()) {
			return "productoEmpleado";
		}
		byte[] bytes;
		try {
			bytes = imagenDTO.getImagen().getBytes();
			Path rutaDestino = Paths.get(
					"C:/workspace-spring-tool-suite-4-4.3.2.RELEASE/TIENDA_JOSUE_MARTINEZ_FERNANDEZ/src/main/resources/"
							+ "static/images/" + producto.getImagenname() + ".jpg");
			Files.write(rutaDestino, bytes);
		} catch (IOException e) {
			logger.logError("Problema al subir la imagen");
		}
		productoService.modificar(producto);
		logger.logInfo("Producto modificado");
		return "redirect:goProductoEmpleado";
	}

	@PostMapping("/productoAdminBaja")
	public String productoAdminBaja(@RequestParam("id") int id, @Valid @ModelAttribute Producto producto,
			BindingResult bindingResult, HttpSession session, Model model) {
		producto = productoService.obtenerPorId(id);
		producto.setBaja(true);
		productoService.crear(producto);
		logger.logInfo("Producto dado de baja");
		return "redirect:goProductoAdmin";
	}

	@PostMapping("/productoAdminAlta")
	public String productoAdminAlta(@RequestParam("id") int id, @Valid @ModelAttribute Producto producto,
			BindingResult bindingResult, HttpSession session, Model model) {
		producto = productoService.obtenerPorId(id);
		producto.setBaja(false);
		productoService.crear(producto);
		logger.logInfo("Producto dado de baja");
		return "redirect:goProductoAdmin";
	}
}
