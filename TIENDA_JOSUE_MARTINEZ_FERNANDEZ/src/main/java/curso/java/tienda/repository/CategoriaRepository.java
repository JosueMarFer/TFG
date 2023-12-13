package curso.java.tienda.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import curso.java.tienda.model.Categoria;
import curso.java.tienda.model.Producto;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
	Optional<Categoria> findByNombre(String nombre);

	@Query("SELECT c FROM Categoria c WHERE c.baja = false")
	List<Categoria> buscarCategoriasSinBaja();
}
