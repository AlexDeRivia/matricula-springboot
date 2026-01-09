package com.sistemas.entidad;

import java.io.Serializable;
import lombok.Data;

@Data
public class NotaID implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long alumno;
	private Long curso;
	
	public NotaID(){
		
	}
	
	public NotaID(Long alumno, Long curso) {
		super();
		this.alumno = alumno;
		this.curso = curso;
	}
}
