package curso.java.tienda.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

import curso.java.tienda.dto.PagoDTO;
import curso.java.tienda.dto.PedidosDTO;
import curso.java.tienda.model.Detalle;
import curso.java.tienda.model.OpcionMenu;
import curso.java.tienda.model.Pedido;
import curso.java.tienda.model.Producto;
import curso.java.tienda.model.Usuario;
import curso.java.tienda.service.DetalleService;
import curso.java.tienda.service.OpcionMenuService;
import curso.java.tienda.service.PedidoService;
import curso.java.tienda.service.ProductoService;
import curso.java.tienda.service.UsuarioService;
import curso.java.tienda.utils.LoggerTienda;

@Controller
public class PedidoController {

	@Autowired
	ProductoService productoService;
	@Autowired
	DetalleService detalleService;
	@Autowired
	PedidoService pedidoService;
	@Autowired
	UsuarioService usuarioService;
	@Autowired
	OpcionMenuService opcionMenuService;
	LoggerTienda logger = LoggerTienda.getLoggerTienda();

	@PostMapping("/finalizarCompra")
	public String finalizarCompra(@Valid @ModelAttribute PagoDTO pagoDTO, BindingResult bindingResult,
			HttpSession session, Model model) {
		if (session.getAttribute("usuario") == null) {
			return "redirect:/goLogin";
		}
		HashMap<String, Integer> carrito = (HashMap<String, Integer>) session.getAttribute("carrito");
		List<Producto> listaProductos = productoService.returnProductosToCarrito(carrito);

		if (pedidoService.comprobarSiErroresComunes(bindingResult)) {
			return "pago";
		}

		if (pagoDTO.getMetodopago().equals("paypal")) {
			if (pedidoService.comprobarSiErroresPaypal(bindingResult)) {
				return "pago";
			}
		} else if (pagoDTO.getMetodopago().equals("tarjeta")) {
			if (pedidoService.comprobarSiErroresTarjeta(bindingResult)) {
				return "pago";
			}
		} else {
			FieldError error = new FieldError("pagoDTO", "metodopago", "El metodo de pago debe ser paypal o tarjeta");
			bindingResult.addError(error);
			return "pago";
		}

		if (productoService.comprobarStock(carrito, listaProductos, model)) {
			return "redirect:/carro";
		}

		List<Detalle> listaDetalles = detalleService.crearDetallesFromCarrito(carrito);
		Pedido pedido = pedidoService.crearPedidoFromCarrito(session, listaDetalles, pagoDTO);
		detalleService.modificarPedidoFromDetallesAndCrear(listaDetalles, pedido);
		logger.logInfo("Pedido creado");
		return "redirect:/borrarCarrito";
	}

	@GetMapping("/obtenerPedidos")
	public String inicio(HttpServletRequest request, HttpSession session, Model model,
			@Valid @ModelAttribute PedidosDTO pedidosDTO, BindingResult bindingResult) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		if (bindingResult.hasErrors() || model.getAttribute("pasadoPorPost") == null) {
			List<Pedido> listaPedidos = pedidoService.obtenerTodosDeUsuario(usuario);
			model.addAttribute("listaPedidos", listaPedidos);
		} else {
			List<Pedido> listaPedidos = pedidoService.buscarPedidosConFiltros(pedidosDTO.getFecha(), usuario);
			model.addAttribute("listaPedidos", listaPedidos);
		}
		model.addAttribute("activo", "pedidos");
		return "pedidos";
	}

	@PostMapping("/solicitarCancelacionPedido")
	public String solicitarCancelacionPedidos(@RequestParam int id, Pedido pedido) {
		pedido = pedidoService.obtenerPorId(id);
		pedido.setEstado("PC");
		pedidoService.modificar(pedido);
		logger.logInfo("Solicitud de cancelacion de pedido");
		return "redirect:/obtenerPedidos";
	}

	@PostMapping("/obtenerPedidos")
	public String postInicio(HttpServletRequest request, HttpSession session, Model model,
			@Valid @ModelAttribute PedidosDTO pedidosDTO, BindingResult bindingResult) {
		model.addAttribute("pasadoPorPost", "pasadoPorPost");
		inicio(request, session, model, pedidosDTO, bindingResult);
		return "pedidos";
	}

	@GetMapping("/goPedidoAdmin")
	public String goPedidoAdmin(@ModelAttribute Pedido pedido, HttpSession session, Model model) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		List<OpcionMenu> listaOpcionMenu = opcionMenuService.obtenerPorRolId(usuario.getRolid());
		session.setAttribute("listaOpcionMenu", listaOpcionMenu);
		List<Pedido> listaPedidos = pedidoService.obtenerTodosAdmin();
		model.addAttribute("listaPedidos", listaPedidos);
		session.setAttribute("vuelta", "admin");
		return "pedidoAdmin";
	}

	@GetMapping("/goPedidoEmpleado")
	public String goPedidoEmpleado(@ModelAttribute Pedido pedido, HttpSession session, Model model) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		List<OpcionMenu> listaOpcionMenu = opcionMenuService.obtenerPorRolId(usuario.getRolid());
		session.setAttribute("listaOpcionMenu", listaOpcionMenu);
		List<Pedido> listaPedidos = pedidoService.obtenerTodosEmpleado();
		model.addAttribute("listaPedidos", listaPedidos);
		session.setAttribute("vuelta", "empleado");
		return "pedidoEmpleado";
	}

	@PostMapping("/procesarPedido")
	public String procesarPedido(@RequestParam("id") int id, @Valid @ModelAttribute Pedido pedido,
			BindingResult bindingResult, HttpSession session, Model model) {
		pedido = pedidoService.obtenerPorId(id);
		pedido.setEstado("E");
		pedidoService.generarNumFacturaToPedido(pedido);
		pedidoService.generarTotal(pedido);
		pedidoService.crear(pedido);
		logger.logInfo("Pedido Enviado");
		if (session.getAttribute("vuelta").equals("admin")) {
			session.removeAttribute("vuelta");
			return "redirect:goPedidoAdmin";
		} else {
			session.removeAttribute("vuelta");
			return "redirect:goPedidoEmpleado";
		}
	}

	@PostMapping("/cancelarPedido")
	public String cancelarPedido(@RequestParam("id") int id, @Valid @ModelAttribute Pedido pedido,
			BindingResult bindingResult, HttpSession session, Model model) {
		pedido = pedidoService.obtenerPorId(id);
		pedido.setEstado("C");
		pedidoService.crear(pedido);
		logger.logInfo("Pedido cancelado");
		if (session.getAttribute("vuelta").equals("admin")) {
			session.removeAttribute("vuelta");
			return "redirect:goPedidoAdmin";
		} else {
			session.removeAttribute("vuelta");
			return "redirect:goPedidoEmpleado";
		}
	}
}
