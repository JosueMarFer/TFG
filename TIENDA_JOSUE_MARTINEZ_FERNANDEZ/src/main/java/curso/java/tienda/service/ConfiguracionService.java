package curso.java.tienda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import curso.java.tienda.model.Configuracion;
import curso.java.tienda.repository.ConfiguracionRepository;

@Service
public class ConfiguracionService {

	@Autowired
	private ConfiguracionRepository configuracionRepository;

	public List<Configuracion> obtenerTodos() {
		return configuracionRepository.findAll();
	}

	public Configuracion obtenerPorId(int id) {
		return configuracionRepository.findById(id).orElse(null);
	}

	public Configuracion crear(Configuracion configuracion) {
		return configuracionRepository.save(configuracion);
	}

	public void eliminar(int id) {
		configuracionRepository.deleteById(id);
	}

	public boolean comprobarSiConexion() {
		Configuracion configuracion = obtenerPorId(1);
		if (configuracion.isPrimeraConexionSuperAdmin()) {
			return true;
		} else {
			return false;
		}
	}

	public void conexionToFalse() {
		Configuracion configuracion = obtenerPorId(1);
		configuracion.setPrimeraConexionSuperAdmin(false);
		crear(configuracion);
	}
}
