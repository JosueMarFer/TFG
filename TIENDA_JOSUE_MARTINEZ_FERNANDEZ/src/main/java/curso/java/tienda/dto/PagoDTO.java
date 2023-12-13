package curso.java.tienda.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.CreditCardNumber;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoDTO {
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 1, max = 200, message = "El tamaño del campo debe ser inferior a 200 caracteres")
	private String direccion;
	@NotBlank(message = "El campo no puede estar vacio")
	@Pattern(regexp = "\\d{5}", message = "El código postal debe tener 5 dígitos")
	private String codigoPostal;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 1, max = 100, message = "El tamaño del campo debe ser inferior a 100 caracteres")
	private String pais;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 1, max = 100, message = "El tamaño del campo debe ser inferior a 100 caracteres")
	private String comunidadAutonoma;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 1, max = 100, message = "El tamaño del campo debe ser inferior a 100 caracteres")
	private String provincia;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 1, max = 100, message = "El tamaño del campo debe ser inferior a 100 caracteres")
	private String localidad;
	@NotBlank(message = "El campo no puede estar vacio")
	private String metodopago;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 1, max = 50, message = "El tamaño del campo debe ser inferior a 50 caracteres")
	@Email(message = "El email no es valido")
	private String paypalemail;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 8, max = 16, message = "El tamaño del campo debe tener entre 8 y 16 caracteres")
	@Pattern(regexp = ".*[A-Z].*", message = "La contraseña debe contener una letra mayuscula")
	@Pattern(regexp = ".*[a-z].*", message = "La contraseña debe contener una letra minuscula")
	@Pattern(regexp = ".*[0-9].*", message = "La contraseña debe contener un numero")
	@Pattern(regexp = "[a-zA-Z0-9]+", message = "La contraseña solo debe contener caracteres alfanuméricos")
	private String paypalpass;
	@NotBlank(message = "El campo no puede estar vacio")
	@CreditCardNumber(message = "No es un numero de tarjeta valido")
	private String tarjetanumero;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 1, max = 200, message = "El tamaño del campo debe ser inferior a 200 caracteres")
	private String tarjetapropietario;
	@NotBlank(message = "El campo no puede estar vacio")
	@Pattern(regexp = "\\d{3}", message = "El CVV debe tener 3 dígitos numéricos")
	private String tarjetacvv;
	@NotBlank(message = "El campo no puede estar vacio")
	@Pattern(regexp = "^(0[1-9]|1[0-2])\\/\\d{2}$", message = "La fecha de caducidad debe tener el formato MM/AA")
	private String tarjetacaducidad;
}
