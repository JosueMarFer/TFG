package curso.java.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import curso.java.tienda.model.Detalle;

@Repository
public interface DetalleRepository extends JpaRepository<Detalle, Integer> {

}
