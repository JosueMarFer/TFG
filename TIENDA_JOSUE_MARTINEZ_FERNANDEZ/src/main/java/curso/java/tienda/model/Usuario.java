package curso.java.tienda.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name = "rolid", referencedColumnName = "id")
	private Rol rolid;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 1, max = 50, message = "El tamaño del campo debe ser inferior a 50 caracteres")
	@Email(message = "El email no es valido")
	private String email;
	private String pass;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 1, max = 50, message = "El tamaño del campo debe ser inferior a 50 caracteres")
	private String nombre;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 1, max = 100, message = "El tamaño del campo debe ser inferior a 100 caracteres")
	private String apellidos;
	private boolean baja;
}
