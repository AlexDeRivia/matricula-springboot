package com.sistemas.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistemas.entidad.Alumno;
import com.sistemas.repositorio.AlumnoRepository;
import com.sistemas.repositorio.NotaRepository;

@Service
public class AlumnoServiceImpl implements AlumnoService{

	@Autowired
	private AlumnoRepository alumnoRepository;
	@Autowired
	private NotaRepository notaRepository;
	
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
	@Transactional
	public void eliminar (Long id) {
		if (notaRepository.existsByAlumnoId(id)) {
			throw new RegistroEnUsoException("No se puede eliminar al alumno porque tiene cursos matriculados. Revise su boleta, borre las notas si corresponde y retire las matrículas.");
		}
		alumnoRepository.deleteById(id);
	}
}
