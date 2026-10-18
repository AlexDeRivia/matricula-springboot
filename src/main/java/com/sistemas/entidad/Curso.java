package com.sistemas.entidad;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "cursos")
@Getter
@Setter
public class Curso {

	@Id
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 50)
	@NotBlank(message = "El nombre no puede estar en blanco")
	private String nombre;
	
	@Min(value = 1, message = "El curso debe tener al menos 1 credito")
	@NotNull (message = "Los creditos deben tener un valor")
	private Integer creditos;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idDocente",
	referencedColumnName = "id",
	foreignKey = @ForeignKey(name = "fa_docentes_cursos"),
	nullable = false)
	private Docente docente;
	
	@OneToMany (fetch = FetchType.LAZY, mappedBy = "curso")
	private List<Nota> notas;
	
	public Curso() {
		this.docente = new Docente();
	}
	
}

