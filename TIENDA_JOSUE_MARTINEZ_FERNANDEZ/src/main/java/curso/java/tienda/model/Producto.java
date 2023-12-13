package curso.java.tienda.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "producto")
public class Producto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name = "categoriaid", referencedColumnName = "id")
	private Categoria categoria;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 1, max = 100, message = "El tamaño del campo debe ser inferior a 100 caracteres")
	private String nombre;
	@NotBlank(message = "El campo no puede estar vacio")
	@Size(min = 1, max = 300, message = "El tamaño del campo debe ser inferior a 300 caracteres")
	private String descripcion;
	@DecimalMin(value = "0.0", inclusive = false, message = "El valor debe ser mayor que 0.0")
	@DecimalMax(value = "1000.0", inclusive = true, message = "El valor debe ser menor o igual a 1000.0")
	private double precio;
	@DecimalMin(value = "0.0", inclusive = false, message = "El valor debe ser mayor que 0.0")
	@DecimalMax(value = "100.0", inclusive = true, message = "El valor debe ser menor o igual a 100.0")
	private double impuesto;
	@Min(value = 0, message = "El valor debe ser mayor o igual a 1")
	@Max(value = 100, message = "El valor debe ser menor o igual a 100")
	private int stock;
	private String imagenname;
	private boolean baja;
}
