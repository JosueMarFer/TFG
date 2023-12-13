package curso.java.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import curso.java.tienda.model.Configuracion;

@Repository
public interface ConfiguracionRepository extends JpaRepository<Configuracion, Integer> {

}
