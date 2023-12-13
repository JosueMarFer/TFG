package curso.java.tienda.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import curso.java.tienda.model.Pedido;
import curso.java.tienda.model.Usuario;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

	@Query("SELECT p FROM Pedido p WHERE (:fecha is null OR p.fecha >= :fecha) AND (:usuario is null OR p.usuario = :usuario)")
	List<Pedido> buscarPedidosConFiltros(@Param("fecha") Timestamp fecha, @Param("usuario") Usuario usuario);

	@Query("SELECT p FROM Pedido p WHERE (:usuario is null OR p.usuario = :usuario)")
	List<Pedido> buscarPedidosSinFiltros(@Param("usuario") Usuario usuario);

	@Query("SELECT p FROM Pedido p ORDER BY CASE WHEN p.estado = 'PC' THEN 1 WHEN p.estado = 'PE' THEN 2 ELSE 3 END")
	List<Pedido> findAllByCustomOrderAdmin();

	@Query("SELECT p FROM Pedido p ORDER BY CASE WHEN p.estado = 'PE' THEN 1 ELSE 2 END")
	List<Pedido> findAllByCustomOrderEmpleado();

	@Query("SELECT p FROM Pedido p WHERE p.estado = 'PE'")
	List<Pedido> obtenerPedidosPendientesDeEnviar();
}
