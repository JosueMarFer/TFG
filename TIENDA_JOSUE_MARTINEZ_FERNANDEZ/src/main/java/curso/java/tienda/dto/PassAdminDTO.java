package curso.java.tienda.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassAdminDTO {
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 8, max = 16, message = "El tamaño del campo debe tener entre 8 y 16 caracteres")
	@Pattern(regexp = ".*[A-Z].*", message = "La contraseña debe contener una letra mayuscula")
	@Pattern(regexp = ".*[a-z].*", message = "La contraseña debe contener una letra minuscula")
	@Pattern(regexp = ".*[0-9].*", message = "La contraseña debe contener un numero")
	@Pattern(regexp = "[a-zA-Z0-9]+", message = "La contraseña solo debe contener caracteres alfanuméricos")
	private String pass;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 8, max = 16, message = "El tamaño del campo debe tener entre 8 y 16 caracteres")
	@Pattern(regexp = ".*[A-Z].*", message = "La contraseña debe contener una letra mayuscula")
	@Pattern(regexp = ".*[a-z].*", message = "La contraseña debe contener una letra minuscula")
	@Pattern(regexp = ".*[0-9].*", message = "La contraseña debe contener un numero")
	@Pattern(regexp = "[a-zA-Z0-9]+", message = "La contraseña solo debe contener caracteres alfanuméricos")
	private String passTwo;
	@NotBlank(message = "El campo no puede estar vacio")
	private String passLast;
}
