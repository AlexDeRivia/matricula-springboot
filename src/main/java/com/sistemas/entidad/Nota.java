package com.sistemas.entidad;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "notas")
@IdClass(NotaID.class)
@Getter
@Setter
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

	public Double getPromedio() {
		if (unidad1 == null || unidad2 == null || unidad3 == null) return null;
		return (unidad1 + unidad2 + unidad3) / 3;
	}

	public boolean tieneCalificaciones() {
		return unidad1 != null || unidad2 != null || unidad3 != null;
	}
}

