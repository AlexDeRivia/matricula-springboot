package com.sistemas.configuracion;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.sistemas.entidad.Alumno;
import com.sistemas.entidad.Curso;
import com.sistemas.entidad.Docente;
import com.sistemas.entidad.Nota;
import com.sistemas.repositorio.AlumnoRepository;
import com.sistemas.repositorio.CursoRepository;
import com.sistemas.repositorio.DocenteRepository;
import com.sistemas.repositorio.NotaRepository;

@Configuration
@Profile("demo")
public class DemoData {

    @Bean
    CommandLineRunner cargarDatosDeEjemplo(DocenteRepository docentes, CursoRepository cursos,
            AlumnoRepository alumnos, NotaRepository notas) {
        return args -> {
            Docente ana = docente("Ana", "Salazar", "00000001", "900000001",
                    "ana.salazar@example.com", "Matemáticas", LocalDate.of(2019, 3, 1));
            Docente luis = docente("Luis", "Rojas", "00000002", "900000002",
                    "luis.rojas@example.com", "Ciencias", LocalDate.of(2020, 8, 15));
            docentes.save(ana);
            docentes.save(luis);

            Curso matematica = curso("Matemática", 4, ana);
            Curso comunicacion = curso("Comunicación", 3, ana);
            Curso biologia = curso("Biología", 4, luis);
            cursos.save(matematica);
            cursos.save(comunicacion);
            cursos.save(biologia);

            Alumno maria = alumno("María", "Vega", "00000003", "900000003");
            Alumno jose = alumno("José", "Paredes", "00000004", "900000004");
            Alumno lucia = alumno("Lucía", "Campos", "00000005", "900000005");
            alumnos.save(maria);
            alumnos.save(jose);
            alumnos.save(lucia);

            notas.save(nota(maria, matematica, 17.0, 16.0, 17.0));
            notas.save(nota(maria, comunicacion, 16.0, 17.0, 16.0));
            notas.save(nota(jose, biologia, 14.0, 15.0, 13.0));
            notas.save(nota(lucia, matematica, 18.0, 17.0, 16.0));
        };
    }

    private Docente docente(String nombres, String apellidos, String dni, String celular,
            String email, String especialidad, LocalDate ingreso) {
        Docente docente = new Docente();
        docente.setNombres(nombres);
        docente.setApellidos(apellidos);
        docente.setDni(dni);
        docente.setCelular(celular);
        docente.setEMail(email);
        docente.setEspecialidad(especialidad);
        docente.setFechaNacimiento(Date.valueOf(LocalDate.of(1985, 1, 1)));
        docente.setFechaIngreso(Date.valueOf(ingreso));
        return docente;
    }

    private Curso curso(String nombre, int creditos, Docente docente) {
        Curso curso = new Curso();
        curso.setNombre(nombre);
        curso.setCreditos(creditos);
        curso.setDocente(docente);
        return curso;
    }

    private Alumno alumno(String nombre, String apellido, String dni, String celular) {
        Alumno alumno = new Alumno();
        alumno.setNombre(nombre);
        alumno.setApellido(apellido);
        alumno.setDni(dni);
        alumno.setCelular(celular);
        alumno.setEMail(nombre.toLowerCase().replace('í', 'i').replace('é', 'e') + "@example.com");
        alumno.setFechaNacimiento(Date.valueOf(LocalDate.of(2005, 1, 1)));
        return alumno;
    }

    private Nota nota(Alumno alumno, Curso curso, double unidad1, double unidad2, double unidad3) {
        Nota nota = new Nota();
        nota.setAlumno(alumno);
        nota.setCurso(curso);
        nota.setUnidad1(unidad1);
        nota.setUnidad2(unidad2);
        nota.setUnidad3(unidad3);
        return nota;
    }
}
