package curso.java.tienda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import curso.java.tienda.model.Categoria;
import curso.java.tienda.model.Usuario;
import curso.java.tienda.repository.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	public List<Categoria> obtenerTodos() {
		return categoriaRepository.findAll();
	}

	public List<Categoria> obtenerTodosSinBaja() {
		return categoriaRepository.buscarCategoriasSinBaja();
	}

	public Categoria obtenerPorNombre(String nombre) {
		return categoriaRepository.findByNombre(nombre).orElse(null);
	}

	public Categoria obtenerPorId(int id) {
		return categoriaRepository.findById(id).orElse(null);
	}

	public Categoria crear(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	public Categoria modificar(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	public void eliminar(int id) {
		categoriaRepository.deleteById(id);
	}
}
