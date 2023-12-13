package curso.java.tienda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import curso.java.tienda.model.OpcionMenu;
import curso.java.tienda.model.Rol;
import curso.java.tienda.repository.OpcionMenuRepository;

@Service
public class OpcionMenuService {
	@Autowired
	private OpcionMenuRepository opcionMenuRepository;

	public List<OpcionMenu> obtenerTodos() {
		return opcionMenuRepository.findAll();
	}

	public List<OpcionMenu> obtenerPorRolId(Rol rol) {
		return opcionMenuRepository.findByRolid(rol);
	}

	public OpcionMenu obtenerPorId(int id) {
		return opcionMenuRepository.findById(id).orElse(null);
	}

	public OpcionMenu crear(OpcionMenu opcionMenu) {
		return opcionMenuRepository.save(opcionMenu);
	}

	public void eliminar(int id) {
		opcionMenuRepository.deleteById(id);
	}
}
