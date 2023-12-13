package curso.java.tienda.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contacto")
public class Contacto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 1, max = 50, message = "El tamaño del campo debe ser inferior a 50 caracteres")
	@Email(message = "El email no es valido")
	private String email;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 1, max = 50, message = "El tamaño del campo debe ser inferior a 50 caracteres")
	private String nombre;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 1, max = 100, message = "El tamaño del campo debe ser inferior a 100 caracteres")
	private String apellidos;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 1, max = 200, message = "El tamaño del campo debe ser inferior a 200 caracteres")
	private String rconsulta;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 1, max = 500, message = "El tamaño del campo debe ser inferior a 500 caracteres")
	private String consulta;
	private boolean baja;
}
