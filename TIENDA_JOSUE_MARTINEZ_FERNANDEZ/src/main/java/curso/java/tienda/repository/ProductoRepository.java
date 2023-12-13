package curso.java.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import curso.java.tienda.model.Categoria;
import curso.java.tienda.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

	@Query("SELECT p FROM Producto p WHERE (:precioMin is null OR p.precio BETWEEN :precioMin AND :precioMax) "
			+ "AND (:categoria is null OR p.categoria = :categoria) "
			+ "AND (:stockMin is null OR p.stock >= :stockMin) " + "AND (p.baja = false)")
	List<Producto> buscarProductosConFiltros(@Param("precioMin") Double precioMin, @Param("precioMax") Double precioMax,
			@Param("categoria") Categoria categoria, @Param("stockMin") Integer stockMin);

	@Query("SELECT p FROM Producto p WHERE p.baja = false")
	List<Producto> buscarProductosSinBaja();
}
