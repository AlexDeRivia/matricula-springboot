package com.sistemas.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistemas.entidad.Alumno;
import com.sistemas.repositorio.AlumnoRepository;

@Service
public class AlumnoServiceImpl implements AlumnoService{

	@Autowired
	private AlumnoRepository alumnoRepository;
	
	@Override
	public Alumno agregar (Alumno entidad) {
		return alumnoRepository.save(entidad);
	}
	
	@Override
	public List<Alumno> listarTodos(){
		return alumnoRepository.findAll();
	}
	
	@Override
	public Alumno buscar (Long id) {
		Alumno encontrado = null;
		Optional<Alumno> buscado = alumnoRepository.findById(id);
		if(buscado.isPresent()) {
			encontrado = buscado.get();
		}
		return encontrado;
	}
	
	@Override
	public Alumno actualizar(Alumno entidad) {
		return alumnoRepository.save(entidad);
	}
	
	@Override
	public void eliminar (Long id) {
		alumnoRepository.deleteById(id);
	}
}
