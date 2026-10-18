package com.sistemas.servicio;

public class RegistroEnUsoException extends RuntimeException {
	public RegistroEnUsoException(String mensaje) {
		super(mensaje);
	}
}
