package curso.java.tienda.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import curso.java.tienda.model.Categoria;
import curso.java.tienda.model.Producto;
import curso.java.tienda.repository.ProductoRepository;

@Service
public class ProductoService {

	@Autowired
	private ProductoRepository productoRepository;

	public List<Producto> obtenerTodos() {
		return productoRepository.findAll();
	}

	public List<Producto> obtenerTodosSinBaja() {
		return productoRepository.buscarProductosSinBaja();
	}

	public Producto obtenerPorId(int id) {
		return productoRepository.findById(id).orElse(null);
	}

	public Producto modificar(Producto producto) {
		return productoRepository.save(producto);
	}

	public Producto crear(Producto producto) {
		return productoRepository.save(producto);
	}

	public void eliminar(int id) {
		productoRepository.deleteById(id);
	}

	public int contarProductosMasUno() {
		return (int) productoRepository.count() + 1;
	}

	public void addProductoToCarrito(String id, String cantidadProducto, Map<String, Integer> carrito) {

		int cantidadProductoInt = Integer.parseInt(cantidadProducto);

		if (carrito.containsKey(id)) {
			int cantidadAnterior = carrito.get(id);
			carrito.put(id, cantidadProductoInt + cantidadAnterior);
		} else {
			carrito.put(id, cantidadProductoInt);
		}
	}

	public List<Producto> returnProductosToCarrito(Map<String, Integer> carrito) {

		List<Producto> listaProductos = new ArrayList<Producto>();

		for (Entry<String, Integer> producto : carrito.entrySet()) {
			listaProductos.add(obtenerPorId(Integer.parseInt(producto.getKey())));
		}
		return listaProductos;
	}

	public void modProductoToCarrito(String id, String cantidadProducto, HashMap<String, Integer> carrito) {

		int cantidadProductoInt = Integer.parseInt(cantidadProducto);

		if (carrito.containsKey(id)) {
			carrito.put(id, cantidadProductoInt);
		}
	}

	public void deleteProductoToCarrito(String id, HashMap<String, Integer> carrito) {

		if (carrito.containsKey(id)) {
			carrito.remove(id);
		}
	}

	public static boolean comprobarStock(HashMap<String, Integer> carrito, List<Producto> listaProductos, Model model) {

		boolean errores = false;

		for (Producto producto : listaProductos) {
			if (carrito.get(Integer.toString(producto.getId())) > producto.getStock()) {
				model.addAttribute("error" + producto.getId(), "error" + producto.getId());
				carrito.put(Integer.toString(producto.getId()), producto.getStock());
				errores = true;
			}
		}
		return errores;
	}

	public static void comprobarTotalPrecioAddModelo(HashMap<String, Integer> carrito, List<Producto> listaProductos,
			Model model) {

		double total = 0;
		double totaliva = 0;

		for (Producto producto : listaProductos) {
			total += producto.getPrecio() * carrito.get(Integer.toString(producto.getId()));
			totaliva += (producto.getPrecio() * carrito.get(Integer.toString(producto.getId())) * producto.getImpuesto()
					/ 100);
		}
		model.addAttribute("total", total);
		model.addAttribute("totaliva", totaliva);

	}

	public List<Producto> buscarProductosConFiltros(Double precioMin, Double precioMax, Categoria categoria,
			int stockMin) {
		return productoRepository.buscarProductosConFiltros(precioMin, precioMax, categoria, stockMin);
	}
}
