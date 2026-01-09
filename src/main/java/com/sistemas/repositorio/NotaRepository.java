package com.sistemas.repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sistemas.entidad.NotaID;
import com.sistemas.entidad.Nota;

public interface NotaRepository extends JpaRepository <Nota, NotaID>{
	
	public List<Nota> findByAlumnoId(Long idAlumno);

}
