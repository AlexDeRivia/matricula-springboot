package com.sistemas.entidad;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table (name = "notas")
@Data
public class Nota {
	
	@Id
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(
			name = "idAlumno",
			referencedColumnName = "id",
			nullable = false,
			foreignKey = @ForeignKey(name = "fk_alumno_notas")
			)
	private Alumno alumno;
	
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "idCurso",
			referencedColumnName = "id",
			foreignKey = @ForeignKey (name = "fk_cursos_notas"),
			nullable = false)
	private Curso curso;
	
	private Double unidad1;
	private Double unidad2;
	private Double unidad3;
}

