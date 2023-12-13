package curso.java.tienda.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import curso.java.tienda.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {
	Optional<Rol> findByRol(String rol);

	@Query("SELECT r FROM Rol r WHERE r.rol IN ('cliente', 'empleado')")
	List<Rol> findByClientesYEmpleados();

	@Query("SELECT r FROM Rol r WHERE r.rol IN ('cliente', 'empleado', 'administrador')")
	List<Rol> findByClientesYEmpleadosYAdministradores();
}
