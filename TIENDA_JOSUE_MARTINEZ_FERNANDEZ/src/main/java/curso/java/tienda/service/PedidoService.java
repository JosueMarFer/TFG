package curso.java.tienda.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import curso.java.tienda.dto.PagoDTO;
import curso.java.tienda.model.Configuracion;
import curso.java.tienda.model.Detalle;
import curso.java.tienda.model.Pedido;
import curso.java.tienda.model.Usuario;
import curso.java.tienda.repository.PedidoRepository;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private ConfiguracionService configuracionService;

	public List<Pedido> obtenerTodos() {
		return pedidoRepository.findAll();
	}

	public List<Pedido> obtenerTodosAdmin() {
		return pedidoRepository.findAllByCustomOrderAdmin();
	}

	public List<Pedido> obtenerTodosEmpleado() {
		return pedidoRepository.findAllByCustomOrderEmpleado();
	}

	public Pedido obtenerPorId(int id) {
		return pedidoRepository.findById(id).orElse(null);
	}

	public Pedido modificar(Pedido pedido) {
		return pedidoRepository.save(pedido);
	}

	public Pedido crear(Pedido pedido) {
		return pedidoRepository.save(pedido);
	}

	public void eliminar(int id) {
		pedidoRepository.deleteById(id);
	}

	public double calcularTotalPedido(List<Detalle> listaDetalles) {
		double total = 0;
		for (Detalle detalle : listaDetalles) {
			total += detalle.getTotal();
		}
		return total;
	}

	public Pedido crearPedidoFromCarrito(HttpSession session, List<Detalle> listaDetalles, PagoDTO pagoDTO) {

		Pedido pedido = new Pedido();

		pedido.setUsuario((Usuario) session.getAttribute("usuario"));
		pedido.setFecha(Timestamp.valueOf(LocalDateTime.now()));
		pedido.setMetodopago(pagoDTO.getMetodopago());
		pedido.setEstado("PE");
		pedido.setDireccionenvio(pagoDTO.getDireccion() + ":" + pagoDTO.getLocalidad() + ":" + pagoDTO.getProvincia()
				+ ":" + pagoDTO.getComunidadAutonoma() + ":" + pagoDTO.getPais() + ":" + pagoDTO.getCodigoPostal());
		return crear(pedido);
	}

	public boolean comprobarSiErroresTarjeta(BindingResult bindingResult) {
		if (bindingResult.hasFieldErrors("tarjetanumero") || bindingResult.hasFieldErrors("tarjetapropietario")
				|| bindingResult.hasFieldErrors("tarjetacvv") || bindingResult.hasFieldErrors("tarjetacaducidad")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean comprobarSiErroresPaypal(BindingResult bindingResult) {
		if (bindingResult.hasFieldErrors("paypalemail") || bindingResult.hasFieldErrors("paypalpass")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean comprobarSiErroresComunes(BindingResult bindingResult) {
		if (bindingResult.hasFieldErrors("direccion") || bindingResult.hasFieldErrors("codigoPostal")
				|| bindingResult.hasFieldErrors("pais") || bindingResult.hasFieldErrors("comunidadAutonoma")
				|| bindingResult.hasFieldErrors("provincia") || bindingResult.hasFieldErrors("localidad")
				|| bindingResult.hasFieldErrors("metodopago")) {
			return true;
		} else {
			return false;
		}
	}

	public Pedido generarNumFacturaToPedido(Pedido pedido) {
		Configuracion configuracion = configuracionService.obtenerPorId(1);
		configuracion.setNumFactura(configuracion.getNumFactura() + 1);
		pedido.setNumfactura(Integer.toString(configuracion.getNumFactura()));
		configuracionService.crear(configuracion);
		return pedido;
	}

	public Pedido generarTotal(Pedido pedido) {
		int total = 0;
		for (Detalle detalle : pedido.getListadetalles()) {
			total += (detalle.getTotal() + (detalle.getTotal() * detalle.getImpuesto() / 100));
		}
		pedido.setTotal(total);
		return pedido;
	}

	public List<Pedido> buscarPedidosConFiltros(Date fecha, Usuario usuario) {
		return pedidoRepository.buscarPedidosConFiltros(new Timestamp(fecha.getTime()), usuario);
	}

	public List<Pedido> obtenerTodosDeUsuario(Usuario usuario) {
		return pedidoRepository.buscarPedidosSinFiltros(usuario);
	}

	@Async
	@Scheduled(fixedRate = 3600000, initialDelay = 2600000)
	@Transactional
	public void hiloProcesarPedidos() {
		List<Pedido> listapedidos = pedidoRepository.obtenerPedidosPendientesDeEnviar();
		for (Pedido pedido : listapedidos) {
			generarNumFacturaToPedido(pedido);
			generarTotal(pedido);
			pedido.setEstado("E");
		}
		pedidoRepository.saveAll(listapedidos);
	}
}
