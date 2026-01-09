package com.sistemas.repositorio;


import org.springframework.data.jpa.repository.JpaRepository;
import com.sistemas.entidad.Alumno;

public interface AlumnoRepository extends JpaRepository <Alumno, Long>{

}
