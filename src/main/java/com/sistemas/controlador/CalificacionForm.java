package com.sistemas.controlador;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalificacionForm {
	@DecimalMin(value = "0.0", message = "La nota debe ser al menos 0")
	@DecimalMax(value = "20.0", message = "La nota no puede superar 20")
	private Double unidad1;

	@DecimalMin(value = "0.0", message = "La nota debe ser al menos 0")
	@DecimalMax(value = "20.0", message = "La nota no puede superar 20")
	private Double unidad2;

	@DecimalMin(value = "0.0", message = "La nota debe ser al menos 0")
	@DecimalMax(value = "20.0", message = "La nota no puede superar 20")
	private Double unidad3;
}
