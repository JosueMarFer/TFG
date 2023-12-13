package curso.java.tienda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import curso.java.tienda.model.Rol;
import curso.java.tienda.repository.RolRepository;

@Service
public class RolService {
	@Autowired
	private RolRepository rolRepository;

	public List<Rol> obtenerTodos() {
		return rolRepository.findAll();
	}

	public List<Rol> findByClientesYEmpleados() {
		return rolRepository.findByClientesYEmpleados();
	}

	public List<Rol> findByClientesYEmpleadosYAdministradores() {
		return rolRepository.findByClientesYEmpleadosYAdministradores();
	}

	public Rol obtenerPorRol(String rol) {
		return rolRepository.findByRol(rol).orElse(null);
	}

	public Rol obtenerPorId(int id) {
		return rolRepository.findById(id).orElse(null);
	}

	public Rol crear(Rol rol) {
		return rolRepository.save(rol);
	}

	public void eliminar(int id) {
		rolRepository.deleteById(id);
	}
}
