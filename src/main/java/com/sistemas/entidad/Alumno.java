package com.sistemas.entidad;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table (name = "Alumnos")
@Data
public class Alumno {

	@Id
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 35, nullable = false)
	@NotBlank(message = "El nombre no puede estar en blanco")
	@Size(min = 2, max = 35, message = "El nombre debe tener entre 2 y 35 caracteres")
	private String nombre;
	
	@Column(length = 35, nullable = false)
	@NotBlank(message = "El apellido no puede estar en blanco")
	@Size(min = 2, max = 35, message = "El apellido debe tener entre 2 y 35 caracteres")
	private String apellido;
	
	@Column(length = 8, nullable = false, unique = true)
	@NotBlank(message = "El DNI no puede estar en blanco")
	@Size(min = 2, max = 35, message = "El DNI debe tener 8 caracteres")
	private String dni;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="YYYY-MM-dd")
	@Past(message="El nacimiento debe ser anterior a la echa actual")
	private Date fechaNacimiento;
	
	@Column(length = 9, nullable = true,unique = true)
	@NotBlank(message = "El celular no puede estar en blanco")
	@Size(min = 9, max = 9, message = "El celular debe tener 9 caracteres")
	private String celular;
	
	@Column(length = 80)
	@NotBlank(message = "El eMail no puede estar en blanco")
	@Email(message = "El ingresar un correo válido")
	private String eMail;
	
	@Column(length = 80)
	private String direccion;
	
	@DecimalMin(value = "0.0", message = "El promedio no puede ser menor que 0")
	@DecimalMax(value="20.0", message = "El proedio no puede ser mayor que 20")
	@NotNull(message = "El promedio no puede ser nulo")
	private Double promedio;
	
	@OneToMany (fetch = FetchType.LAZY, mappedBy = "alumno", cascade = CascadeType.ALL)
	private List<Nota> notas;
}

