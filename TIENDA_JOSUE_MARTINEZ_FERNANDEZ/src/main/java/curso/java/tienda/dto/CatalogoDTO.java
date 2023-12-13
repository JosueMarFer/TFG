package curso.java.tienda.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogoDTO {
	@Digits(integer = 5, fraction = 2, message = "El campo debe ser numérico y tener máximo 5 dígitos enteros y dos decimales.")
	private double precioMin;
	@Digits(integer = 5, fraction = 2, message = "El campo debe ser numérico y tener máximo 5 dígitos enteros y dos decimales.")
	private double precioMax;
	@Digits(integer = 5, fraction = 0, message = "El campo debe ser numérico y tener máximo 5 dígitos enteros y ningun decimal.")
	private int stockMin;
	private String categoria;
}
