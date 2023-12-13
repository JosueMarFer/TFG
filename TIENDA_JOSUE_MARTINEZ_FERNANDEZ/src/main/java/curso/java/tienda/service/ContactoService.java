package curso.java.tienda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import curso.java.tienda.model.Contacto;
import curso.java.tienda.repository.ContactoRepository;

@Service
public class ContactoService {

	@Autowired
	private ContactoRepository contactoRepository;

	public List<Contacto> obtenerTodos() {
		return contactoRepository.findAll();
	}

	public Contacto obtenerPorId(int id) {
		return contactoRepository.findById(id).orElse(null);
	}

	public Contacto crear(Contacto contacto) {
		return contactoRepository.save(contacto);
	}

	public void eliminar(int id) {
		contactoRepository.deleteById(id);
	}
}
