package com.sistemas.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistemas.entidad.Curso;

public interface CursoRepository extends JpaRepository <Curso, Long>{
	boolean existsByDocenteId(Long idDocente);

}
