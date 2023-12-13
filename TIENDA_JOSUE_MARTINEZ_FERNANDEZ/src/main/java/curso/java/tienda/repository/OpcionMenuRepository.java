package curso.java.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import curso.java.tienda.model.OpcionMenu;
import curso.java.tienda.model.Rol;

@Repository
public interface OpcionMenuRepository extends JpaRepository<OpcionMenu, Integer> {
	List<OpcionMenu> findByRolid(Rol rol);
}
