package com.sistemas.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistemas.entidad.Curso;
import com.sistemas.repositorio.CursoRepository;
import com.sistemas.repositorio.NotaRepository;

@Service
public class CursoServiceImpl implements CursoService{

	@Autowired
	private CursoRepository cursoRepository;
	@Autowired
	private NotaRepository notaRepository;
	
	@Override
	public Curso agregar (Curso entidad) {
		return cursoRepository.save(entidad);
	}
	
	@Override
	public List<Curso> listarTodos(){
		return cursoRepository.findAll();
	}
	
	@Override
	public Curso buscar (Long id) {
		return cursoRepository.findById(id).orElse(null);
	}
	
	@Override
	public Curso actualizar(Curso entidad) {
		return cursoRepository.save(entidad);
	}
	
	@Override
	@Transactional
	public void eliminar (Long id) {
		if (notaRepository.existsByCursoId(id)) {
			throw new RegistroEnUsoException("No se puede eliminar el curso porque tiene alumnos matriculados. Borre las notas si corresponde y retire primero las matrículas.");
		}
		cursoRepository.deleteById(id);
	}
}
