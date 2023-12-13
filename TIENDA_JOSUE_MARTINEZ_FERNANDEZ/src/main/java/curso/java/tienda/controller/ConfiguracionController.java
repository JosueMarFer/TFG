package curso.java.tienda.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import curso.java.tienda.model.Categoria;
import curso.java.tienda.model.Configuracion;
import curso.java.tienda.model.Detalle;
import curso.java.tienda.model.OpcionMenu;
import curso.java.tienda.model.Pedido;
import curso.java.tienda.model.Producto;
import curso.java.tienda.model.Rol;
import curso.java.tienda.model.Usuario;
import curso.java.tienda.service.CategoriaService;
import curso.java.tienda.service.ConfiguracionService;
import curso.java.tienda.service.DetalleService;
import curso.java.tienda.service.OpcionMenuService;
import curso.java.tienda.service.PedidoService;
import curso.java.tienda.service.ProductoService;
import curso.java.tienda.service.RolService;
import curso.java.tienda.service.UsuarioService;
import curso.java.tienda.utils.EncriptadorTienda;
import curso.java.tienda.utils.LoggerTienda;

@Controller
public class ConfiguracionController {

	@PostConstruct
	private void ConfiguracionsPrueba() {
		Configuracion configuracion = new Configuracion(0, "E123456789",
				"calle fortaleza N21:benavente:zamora:castilla y leon:españa:49600", "White Snake SL", 0, true);
		configuracionService.crear(configuracion);

		Categoria categoria1 = new Categoria(0, "Verano", "Ropa ligera para el verano", false);
		Categoria categoria2 = new Categoria(0, "Entretiempo", "Ropa de grosor medio para entretiempo", false);
		Categoria categoria3 = new Categoria(0, "Invierno", "Ropa gruesa para el invierno", false);
		categoriaService.crear(categoria1);
		categoriaService.crear(categoria2);
		categoriaService.crear(categoria3);

		Rol cliente = new Rol(0, "cliente");
		Rol empleado = new Rol(0, "empleado");
		Rol administrador = new Rol(0, "administrador");
		Rol superadministrador = new Rol(0, "superadministrador");
		rolService.crear(cliente);
		rolService.crear(empleado);
		rolService.crear(administrador);
		rolService.crear(superadministrador);

		OpcionMenu opcionMenuCliente1 = new OpcionMenu(0, cliente, "perfil", "goPerfil");
		OpcionMenu opcionMenuCliente2 = new OpcionMenu(0, cliente, "pedidos", "obtenerPedidos");
		OpcionMenu opcionMenuEmpleado1 = new OpcionMenu(0, empleado, "Gestion de productos", "goProductoEmpleado");
		OpcionMenu opcionMenuEmpleado2 = new OpcionMenu(0, empleado, "Gestion de usuarios", "goUsuarioEmpleado");
		OpcionMenu opcionMenuEmpleado3 = new OpcionMenu(0, empleado, "Gestion de categorias", "goCategoriaEmpleado");
		OpcionMenu opcionMenuEmpleado4 = new OpcionMenu(0, empleado, "Gestion de pedidos", "goPedidoEmpleado");
		OpcionMenu opcionMenuEmpleado5 = new OpcionMenu(0, empleado, "Gestion de perfil", "goAdminPerfil");
		OpcionMenu opcionMenuAdministrador1 = new OpcionMenu(0, administrador, "Gestion de productos",
				"goProductoAdmin");
		OpcionMenu opcionMenuAdministrador2 = new OpcionMenu(0, administrador, "Gestion de usuarios", "goUsuarioAdmin");
		OpcionMenu opcionMenuAdministrador3 = new OpcionMenu(0, administrador, "Gestion de categorias",
				"goCategoriaAdmin");
		OpcionMenu opcionMenuAdministrador4 = new OpcionMenu(0, administrador, "Gestion de pedidos", "goPedidoAdmin");
		OpcionMenu opcionMenuAdministrador5 = new OpcionMenu(0, administrador, "Gestion de perfil", "goAdminPerfil");
		OpcionMenu opcionMenuSuperAdministrador1 = new OpcionMenu(0, superadministrador, "Gestion de productos",
				"goProductoAdmin");
		OpcionMenu opcionMenuSuperAdministrador2 = new OpcionMenu(0, superadministrador, "Gestion de usuarios",
				"goUsuarioSuperAdmin");
		OpcionMenu opcionMenuSuperAdministrador3 = new OpcionMenu(0, superadministrador, "Gestion de categorias",
				"goCategoriaAdmin");
		OpcionMenu opcionMenuSuperAdministrador4 = new OpcionMenu(0, superadministrador, "Gestion de pedidos",
				"goPedidoAdmin");
		OpcionMenu opcionMenuSuperAdministrador5 = new OpcionMenu(0, superadministrador, "Gestion de perfil",
				"goAdminPerfil");
		opcionMenuService.crear(opcionMenuCliente1);
		opcionMenuService.crear(opcionMenuCliente2);
		opcionMenuService.crear(opcionMenuEmpleado1);
		opcionMenuService.crear(opcionMenuEmpleado2);
		opcionMenuService.crear(opcionMenuEmpleado3);
		opcionMenuService.crear(opcionMenuEmpleado4);
		opcionMenuService.crear(opcionMenuEmpleado5);
		opcionMenuService.crear(opcionMenuAdministrador1);
		opcionMenuService.crear(opcionMenuAdministrador2);
		opcionMenuService.crear(opcionMenuAdministrador3);
		opcionMenuService.crear(opcionMenuAdministrador4);
		opcionMenuService.crear(opcionMenuAdministrador5);
		opcionMenuService.crear(opcionMenuSuperAdministrador1);
		opcionMenuService.crear(opcionMenuSuperAdministrador2);
		opcionMenuService.crear(opcionMenuSuperAdministrador3);
		opcionMenuService.crear(opcionMenuSuperAdministrador4);
		opcionMenuService.crear(opcionMenuSuperAdministrador5);

		Producto producto1 = new Producto(0, categoria1, "Camiseta", "Camiseta negra con logo White Snake en blanco",
				20, 21, 30, "p1", false);
		Producto producto2 = new Producto(0, categoria2, "Sudadera", "Sudadera negra con logo White Snake en blanco",
				30, 21, 20, "p2", false);
		Producto producto3 = new Producto(0, categoria3, "Cazadora", "Cazadora negra con logo White Snake en blanco",
				50, 21, 10, "p3", false);
		Producto producto4 = new Producto(0, categoria1, "Camisa", "Camisa negra con logo White Snake en blanco", 20,
				21, 20, "p4", false);
		productoService.crear(producto1);
		productoService.crear(producto2);
		productoService.crear(producto3);
		productoService.crear(producto4);

		Usuario usuario1 = new Usuario(0, cliente, "josue@josue", EncriptadorTienda.encriptar("Josuejosue1"), "josue",
				"martinez fernandez", false);
		Usuario usuario2 = new Usuario(0, cliente, "alex@alex", EncriptadorTienda.encriptar("Alexalex1"), "alex",
				"otalvaro marulanda", false);
		Usuario usuario3 = new Usuario(0, empleado, "emple@emple", EncriptadorTienda.encriptar("Empleemple1"), "emple",
				"empleado empleado", false);
		Usuario usuario4 = new Usuario(0, administrador, "admin@admin", EncriptadorTienda.encriptar("Adminadmin1"),
				"admin", "administrador administrador", false);
		usuarioService.crear(usuario1);
		usuarioService.crear(usuario2);
		usuarioService.crear(usuario3);
		usuarioService.crear(usuario4);

		Detalle detalle1 = new Detalle();
		detalle1.setImpuesto(producto1.getImpuesto());
		detalle1.setPreciounidad(producto1.getPrecio());
		detalle1.setUnidades(1);
		detalle1.setProducto(producto1);
		detalle1.setTotal(detalle1.getUnidades() * detalle1.getPreciounidad());
		Detalle detalle2 = new Detalle();
		detalle2.setImpuesto(producto2.getImpuesto());
		detalle2.setPreciounidad(producto2.getPrecio());
		detalle2.setUnidades(2);
		detalle2.setProducto(producto2);
		detalle2.setTotal(detalle2.getUnidades() * detalle2.getPreciounidad());
		Detalle detalle3 = new Detalle();
		detalle3.setImpuesto(producto3.getImpuesto());
		detalle3.setPreciounidad(producto3.getPrecio());
		detalle3.setUnidades(3);
		detalle3.setProducto(producto3);
		detalle3.setTotal(detalle3.getUnidades() * detalle3.getPreciounidad());
		Detalle detalle4 = new Detalle();
		detalle4.setImpuesto(producto4.getImpuesto());
		detalle4.setPreciounidad(producto4.getPrecio());
		detalle4.setUnidades(4);
		detalle4.setProducto(producto4);
		detalle4.setTotal(detalle4.getUnidades() * detalle4.getPreciounidad());

		LocalDateTime date1 = LocalDateTime.now();
		LocalDateTime date2 = LocalDateTime.of(2023, 5, 16, 22, 5, 26);
		Timestamp fecha1 = Timestamp.valueOf(date1);
		Timestamp fecha2 = Timestamp.valueOf(date2);

		Pedido pedido1 = new Pedido();
		pedido1.setDireccionenvio("avenida ferial N32:benavente:zamora:castilla y leon:españa:49600");
		pedido1.setMetodopago("paypal");
		pedido1.setFecha(fecha1);
		pedido1.setUsuario(usuario1);
		pedido1.setEstado("PE");

		Pedido pedido2 = new Pedido();
		pedido2.setDireccionenvio("avenida ferial N32:benavente:zamora:castilla y leon:españa:49600");
		pedido2.setMetodopago("paypal");
		pedido2.setFecha(fecha1);
		pedido2.setUsuario(usuario2);
		pedido2.setEstado("PE");

		Pedido pedido3 = new Pedido();
		pedido3.setDireccionenvio("avenida ferial N32:benavente:zamora:castilla y leon:españa:49600");
		pedido3.setMetodopago("tarjeta");
		pedido3.setFecha(fecha2);
		pedido3.setUsuario(usuario1);
		pedido3.setEstado("PE");

		Pedido pedido4 = new Pedido();
		pedido4.setDireccionenvio("avenida ferial N32:benavente:zamora:castilla y leon:españa:49600");
		pedido4.setMetodopago("tarjeta");
		pedido4.setFecha(fecha2);
		pedido4.setUsuario(usuario2);
		pedido4.setEstado("PE");

		pedidoService.crear(pedido1);
		pedidoService.crear(pedido2);
		pedidoService.crear(pedido3);
		pedidoService.crear(pedido4);

		detalle1.setPedido(pedido1);
		detalle2.setPedido(pedido2);
		detalle3.setPedido(pedido3);
		detalle4.setPedido(pedido4);

		detalleService.crear(detalle1);
		detalleService.crear(detalle2);
		detalleService.crear(detalle3);
		detalleService.crear(detalle4);
	}

	@Autowired
	ConfiguracionService configuracionService;
	@Autowired
	DetalleService detalleService;
	@Autowired
	PedidoService pedidoService;
	@Autowired
	UsuarioService usuarioService;
	@Autowired
	ProductoService productoService;
	@Autowired
	CategoriaService categoriaService;
	@Autowired
	RolService rolService;
	@Autowired
	OpcionMenuService opcionMenuService;
	LoggerTienda logger = LoggerTienda.getLoggerTienda();
}
