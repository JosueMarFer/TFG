package curso.java.tienda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import curso.java.tienda.dto.RegisterDTO;
import curso.java.tienda.model.Usuario;
import curso.java.tienda.repository.UsuarioRepository;
import curso.java.tienda.utils.EncriptadorTienda;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	RolService rolService;

	public List<Usuario> obtenerTodos() {
		return usuarioRepository.findAll();
	}

	public List<Usuario> findClientesYEmpleados() {
		return usuarioRepository.findClientesYEmpleados();
	}

	public List<Usuario> findClientesYEmpleadosYAdmins() {
		return usuarioRepository.findClientesYEmpleadosYAdmins();
	}

	public List<Usuario> findClientes() {
		return usuarioRepository.findClientes();
	}

	public boolean comprabarSiSuperAdmin() {
		Usuario superadmin = usuarioRepository.findSuperAdministrador().orElse(null);
		if (superadmin != null) {
			return true;
		} else {
			return false;
		}
	}

	public Usuario obtenerPorId(int id) {
		return usuarioRepository.findById(id).orElse(null);
	}

	public Usuario obtenerPorEmail(String email) {
		return usuarioRepository.findByEmail(email).orElse(null);
	}

	public Usuario modificar(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	public Usuario crear(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	public void eliminar(int id) {
		usuarioRepository.deleteById(id);
	}

	public boolean comprobarLogin(String email, String passAComprobar) {
		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
		if (usuario != null && usuario.getRolid().getRol().equals("cliente")) {
			return EncriptadorTienda.compararEncriptado(passAComprobar, usuario.getPass());
		} else {
			return false;
		}
	}

	public boolean comprobarLoginAdmin(String email, String passAComprobar) {
		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
		if (usuario != null && !usuario.getRolid().getRol().equals("cliente")) {
			return EncriptadorTienda.compararEncriptado(passAComprobar, usuario.getPass());
		} else {
			return false;
		}
	}

	public boolean comprobarPassIguales(String pass, String passtwo) {
		if (pass.equals(passtwo)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean comprobarPassAnteriorIgual(String passLast, String pass) {
		if (passLast.equals(pass)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean comprobarPassAnteriorCorrecta(String passLast, String passUser) {
		if (EncriptadorTienda.compararEncriptado(passLast, passUser)) {
			return true;
		} else {
			return false;
		}
	}

	public Usuario registerDTOToUser(RegisterDTO registerDTO) {
		Usuario usuario = new Usuario();
		usuario.setEmail(registerDTO.getEmail());
		usuario.setNombre(registerDTO.getNombre());
		usuario.setApellidos(registerDTO.getApellidos());
		usuario.setPass(EncriptadorTienda.encriptar(registerDTO.getPass()));
		usuario.setRolid(rolService.obtenerPorRol("cliente"));
		return usuario;
	}
}
