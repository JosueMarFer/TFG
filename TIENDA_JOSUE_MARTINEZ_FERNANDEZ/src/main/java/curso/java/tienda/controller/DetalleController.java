package curso.java.tienda.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import curso.java.tienda.model.Detalle;
import curso.java.tienda.model.Pedido;
import curso.java.tienda.model.Producto;
import curso.java.tienda.service.DetalleService;
import curso.java.tienda.service.PedidoService;
import curso.java.tienda.service.ProductoService;

@Controller
public class DetalleController {

	@Autowired
	ProductoService productoService;
	@Autowired
	DetalleService detalleService;
	@Autowired
	PedidoService pedidoService;

}
