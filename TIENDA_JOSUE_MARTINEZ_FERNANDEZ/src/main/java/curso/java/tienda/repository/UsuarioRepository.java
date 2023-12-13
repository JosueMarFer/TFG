package curso.java.tienda.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import curso.java.tienda.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	Optional<Usuario> findByEmail(String email);

	@Query("SELECT u FROM Usuario u WHERE u.rolid.rol = 'superadministrador'")
	Optional<Usuario> findSuperAdministrador();

	@Query("SELECT u FROM Usuario u WHERE u.rolid.rol IN ('cliente', 'empleado', 'administrador')")
	List<Usuario> findClientesYEmpleadosYAdmins();

	@Query("SELECT u FROM Usuario u WHERE u.rolid.rol IN ('cliente', 'empleado')")
	List<Usuario> findClientesYEmpleados();

	@Query("SELECT u FROM Usuario u WHERE u.rolid.rol = 'cliente'")
	List<Usuario> findClientes();
}
