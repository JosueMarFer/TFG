package curso.java.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import curso.java.tienda.model.Contacto;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Integer> {

}
