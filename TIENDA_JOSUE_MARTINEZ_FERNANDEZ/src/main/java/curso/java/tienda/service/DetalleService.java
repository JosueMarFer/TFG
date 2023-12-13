package curso.java.tienda.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import curso.java.tienda.model.Detalle;
import curso.java.tienda.model.Pedido;
import curso.java.tienda.model.Producto;
import curso.java.tienda.repository.DetalleRepository;

@Service
public class DetalleService {

	@Autowired
	private DetalleRepository detalleRepository;
	@Autowired
	ProductoService productoService;

	public List<Detalle> obtenerTodos() {
		return detalleRepository.findAll();
	}

	public Detalle obtenerPorId(int id) {
		return detalleRepository.findById(id).orElse(null);
	}

	public Detalle crear(Detalle detalle) {
		return detalleRepository.save(detalle);
	}

	public void eliminar(int id) {
		detalleRepository.deleteById(id);
	}

	public List<Detalle> crearDetallesFromCarrito(HashMap<String, Integer> carrito) {

		List<Detalle> listadetalles = new ArrayList<Detalle>();

		for (Entry<String, Integer> elemento : carrito.entrySet()) {
			Producto producto = productoService.obtenerPorId(Integer.parseInt(elemento.getKey()));
			Detalle detalle = new Detalle();

			detalle.setId(0);
			detalle.setProducto(producto);
			detalle.setUnidades(elemento.getValue());
			producto.setStock(producto.getStock() - elemento.getValue());
			productoService.modificar(producto);
			detalle.setPreciounidad(producto.getPrecio());
			detalle.setImpuesto(producto.getImpuesto());
			detalle.setTotal(elemento.getValue() * detalle.getPreciounidad());
			listadetalles.add(detalle);
		}
		return listadetalles;
	}

	public void modificarPedidoFromDetallesAndCrear(List<Detalle> listaDetalles, Pedido pedido) {
		for (Detalle detalle : listaDetalles) {
			detalle.setPedido(pedido);
			crear(detalle);
		}
	}
}
