package com.sistemas;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.sistemas.entidad.Alumno;
import com.sistemas.entidad.Curso;
import com.sistemas.entidad.Docente;
import com.sistemas.entidad.NotaID;
import com.sistemas.repositorio.AlumnoRepository;
import com.sistemas.repositorio.CursoRepository;
import com.sistemas.repositorio.DocenteRepository;
import com.sistemas.repositorio.NotaRepository;

@SpringBootTest(properties = "demo.admin.password=test-admin-secret")
@AutoConfigureMockMvc
class EjemploSpringBootApplicationTests {

    @Autowired MockMvc mvc;
    @Autowired AlumnoRepository alumnos;
    @Autowired DocenteRepository docentes;
    @Autowired CursoRepository cursos;
    @Autowired NotaRepository notas;

    @Test
    void demoArrancaConDatosFicticiosYFormulariosAccesibles() throws Exception {
        assertThat(alumnos.count()).isGreaterThanOrEqualTo(3);
        assertThat(docentes.count()).isGreaterThanOrEqualTo(2);
        assertThat(cursos.count()).isGreaterThanOrEqualTo(3);

        mvc.perform(get("/alumno/index").with(user("admin-demo").roles("ADMIN")))
                .andExpect(status().isOk()).andExpect(view().name("alumno/alumnoIndex"));
        mvc.perform(get("/alumno/nuevo").with(user("admin-demo").roles("ADMIN")))
                .andExpect(status().isOk());
        mvc.perform(get("/docente/nuevo").with(user("admin-demo").roles("ADMIN")))
                .andExpect(status().isOk());
        mvc.perform(get("/curso/nuevo").with(user("admin-demo").roles("ADMIN")))
                .andExpect(status().isOk());
        mvc.perform(get("/docente/index").with(user("demo").roles("VIEWER")))
                .andExpect(status().isOk());
        mvc.perform(get("/curso/index").with(user("demo").roles("VIEWER")))
                .andExpect(status().isOk());
        long idAlumno = alumnos.findAll().get(0).getId();
        mvc.perform(get("/nota/matricular/{id}", idAlumno).with(user("admin-demo").roles("ADMIN")))
                .andExpect(status().isOk());
        mvc.perform(get("/alumno/editar/{id}", idAlumno).with(user("admin-demo").roles("ADMIN")))
                .andExpect(status().isOk());
        mvc.perform(get("/docente/editar/{id}", docentes.findAll().get(0).getId())
                .with(user("admin-demo").roles("ADMIN"))).andExpect(status().isOk());
        mvc.perform(get("/curso/editar/{id}", cursos.findAll().get(0).getId())
                .with(user("admin-demo").roles("ADMIN"))).andExpect(status().isOk());
    }

    @Test
    void lectorNoPuedeCambiarDatosYBorradoNoAceptaGet() throws Exception {
        long idAlumno = alumnos.findAll().get(0).getId();
        long total = alumnos.count();

        mvc.perform(get("/alumno/index")).andExpect(status().is3xxRedirection());
        mvc.perform(get("/alumno/index").with(user("demo").roles("VIEWER")))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Matricular"))))
                .andExpect(content().string(not(containsString("Eliminar"))));
        mvc.perform(get("/curso/index").with(user("demo").roles("VIEWER")))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Agregar Curso"))));
        mvc.perform(post("/alumno/eliminar/{id}", idAlumno).with(user("demo").roles("VIEWER")).with(csrf()))
                .andExpect(status().isForbidden());
        mvc.perform(get("/alumno/eliminar/{id}", idAlumno).with(user("admin-demo").roles("ADMIN")))
                .andExpect(status().isMethodNotAllowed());
        mvc.perform(post("/alumno/eliminar/{id}", idAlumno).with(user("admin-demo").roles("ADMIN")))
                .andExpect(status().isForbidden());
        assertThat(alumnos.count()).isEqualTo(total);
    }

    @Test
    void credencialesDeDemoDanAccesoSegunSuRol() throws Exception {
        mvc.perform(formLogin().user("demo").password("demo123"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/"));
        mvc.perform(formLogin().user("admin-demo").password("test-admin-secret"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/"));
        mvc.perform(formLogin().user("admin-demo").password("incorrecta"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuario/login?error"));
    }

    @Test
    void administradorPuedeRegistrarAlumno() throws Exception {
        long total = alumnos.count();
        mvc.perform(post("/alumno/guardar").with(user("admin-demo").roles("ADMIN")).with(csrf())
                .param("nombre", "Elena")
                .param("apellido", "Mora")
                .param("dni", "00000006")
                .param("celular", "900000006")
                .param("eMail", "elena.mora@example.com")
                .param("fechaNacimiento", "2005-01-01"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/alumno/index"))
                .andExpect(flash().attribute("success", is("Alumno registrado.")));
        assertThat(alumnos.count()).isEqualTo(total + 1);
    }

    @Test
    void administradorPuedeRegistrarDocenteYCurso() throws Exception {
        long totalDocentes = docentes.count();
        mvc.perform(post("/docente/guardar").with(user("admin-demo").roles("ADMIN")).with(csrf())
                .param("nombres", "Raúl")
                .param("apellidos", "Díaz")
                .param("dni", "00000007")
                .param("celular", "900000007")
                .param("eMail", "raul.diaz@example.com")
                .param("fechaNacimiento", "1985-01-01")
                .param("fechaIngreso", "2021-03-01"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/docente/index"));
        assertThat(docentes.count()).isEqualTo(totalDocentes + 1);

        long totalCursos = cursos.count();
        long idDocente = docentes.findAll().get(0).getId();
        mvc.perform(post("/curso/guardar").with(user("admin-demo").roles("ADMIN")).with(csrf())
                .param("nombre", "Química")
                .param("creditos", "4")
                .param("docente.id", Long.toString(idDocente)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curso/index"));
        assertThat(cursos.count()).isEqualTo(totalCursos + 1);
    }

    @Test
    void matriculaConservaCursoExistenteYAñadeSeleccionado() throws Exception {
        long idAlumno = alumnos.findAll().get(0).getId();
        var actuales = notas.findByAlumnoId(idAlumno);
        long idNuevo = cursos.findAll().stream()
                .map(curso -> curso.getId())
                .filter(id -> actuales.stream().noneMatch(nota -> nota.getCurso().getId().equals(id)))
                .findFirst().orElseThrow();

        var request = post("/nota/matricula/guardar")
                .with(user("admin-demo").roles("ADMIN")).with(csrf())
                .param("id", Long.toString(idAlumno))
                .param("idSeleccionados", Long.toString(idNuevo));
        for (var nota : actuales) {
            request.param("idSeleccionados", Long.toString(nota.getCurso().getId()));
        }
        mvc.perform(request).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/alumno/index"));
        assertThat(notas.findByAlumnoId(idAlumno)).hasSize(actuales.size() + 1);
    }

    @Test
    void lectorPuedeVerBoletaPeroNoEditarCalificaciones() throws Exception {
        Long idAlumno = alumnos.findAll().stream()
                .filter(alumno -> "00000003".equals(alumno.getDni()))
                .findFirst().orElseThrow().getId();
        mvc.perform(get("/nota/boleta/{id}", idAlumno).with(user("demo").roles("VIEWER")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Boleta de calificaciones")))
                .andExpect(content().string(containsString("Matemática")))
                .andExpect(content().string(not(containsString("Calificar"))));
        mvc.perform(get("/nota/calificar/{alumno}/{curso}", idAlumno, cursos.findAll().get(0).getId())
                .with(user("demo").roles("VIEWER"))).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void administradorValidaYGuardaNotasDeUnaMatricula() throws Exception {
        var nota = notas.findAll().get(0);
        long idAlumno = nota.getAlumno().getId();
        long idCurso = nota.getCurso().getId();
        Double original = nota.getUnidad1();
        Double promedioAnterior = alumnos.findById(idAlumno).orElseThrow().getPromedio();

        mvc.perform(get("/nota/calificar/{alumno}/{curso}", idAlumno, idCurso)
                .with(user("admin-demo").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Unidad 1")));
        mvc.perform(post("/nota/calificar/{alumno}/{curso}", idAlumno, idCurso)
                .with(user("admin-demo").roles("ADMIN")).with(csrf())
                .param("unidad1", "21").param("unidad2", "11").param("unidad3", "12"))
                .andExpect(status().isOk())
                .andExpect(view().name("nota/calificacionForm"))
                .andExpect(content().string(containsString("La nota no puede superar 20")));
        assertThat(notas.findById(new NotaID(idAlumno, idCurso)).orElseThrow().getUnidad1()).isEqualTo(original);

        mvc.perform(post("/nota/calificar/{alumno}/{curso}", idAlumno, idCurso)
                .with(user("admin-demo").roles("ADMIN")).with(csrf())
                .param("unidad1", "10").param("unidad2", "11").param("unidad3", "12"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nota/boleta/" + idAlumno));
        var guardada = notas.findById(new NotaID(idAlumno, idCurso)).orElseThrow();
        assertThat(guardada.getUnidad1()).isEqualTo(10);
        assertThat(guardada.getPromedio()).isEqualTo(11);
        assertThat(alumnos.findById(idAlumno).orElseThrow().getPromedio()).isNotEqualTo(promedioAnterior);
    }

    @Test
    @Transactional
    void promedioSeCalculaDesdeCursosCompletosYPonderaCreditos() {
        Alumno maria = alumnos.findAll().stream()
                .filter(alumno -> "00000003".equals(alumno.getDni()))
                .findFirst().orElseThrow();
        assertThat(Math.abs(maria.getPromedio() - 347d / 21)).isLessThan(0.000001);

        maria.getNotas().get(0).setUnidad3(null);
        assertThat(maria.getPromedio()).isNotNull();
        maria.getNotas().get(1).setUnidad3(null);
        assertThat(maria.getPromedio()).isNull();
    }

    @Test
    @Transactional
    void eliminacionProtegeRelacionesYPermiteRegistrosSinDependencias() throws Exception {
        var matricula = notas.findAll().get(0);
        long idAlumno = matricula.getAlumno().getId();
        long idCurso = matricula.getCurso().getId();
        long idDocente = matricula.getCurso().getDocente().getId();
        long totalNotas = notas.count();
        long totalCursos = cursos.count();

        mvc.perform(post("/alumno/eliminar/{id}", idAlumno)
                .with(user("admin-demo").roles("ADMIN")).with(csrf()))
                .andExpect(redirectedUrl("/alumno/index"))
                .andExpect(flash().attributeExists("error"));
        mvc.perform(post("/curso/eliminar/{id}", idCurso)
                .with(user("admin-demo").roles("ADMIN")).with(csrf()))
                .andExpect(redirectedUrl("/curso/index"))
                .andExpect(flash().attributeExists("error"));
        mvc.perform(post("/docente/eliminar/{id}", idDocente)
                .with(user("admin-demo").roles("ADMIN")).with(csrf()))
                .andExpect(redirectedUrl("/docente/index"))
                .andExpect(flash().attributeExists("error"));
        assertThat(notas.count()).isEqualTo(totalNotas);
        assertThat(cursos.count()).isEqualTo(totalCursos);

        Alumno libre = new Alumno();
        libre.setNombre("Libre");
        libre.setApellido("Prueba");
        libre.setDni("99999999");
        libre.setCelular("999999999");
        libre.setEMail("libre@example.com");
        Long idLibre = alumnos.save(libre).getId();
        mvc.perform(post("/alumno/eliminar/{id}", idLibre)
                .with(user("admin-demo").roles("ADMIN")).with(csrf()))
                .andExpect(redirectedUrl("/alumno/index"));
        assertThat(alumnos.existsById(idLibre)).isFalse();

        Docente libreDocente = new Docente();
        libreDocente.setNombres("Docente");
        libreDocente.setApellidos("Libre");
        libreDocente.setDni("88888888");
        libreDocente.setCelular("988888888");
        libreDocente.setEMail("docente.libre@example.com");
        libreDocente = docentes.save(libreDocente);
        Curso libreCurso = new Curso();
        libreCurso.setNombre("Curso temporal");
        libreCurso.setCreditos(2);
        libreCurso.setDocente(libreDocente);
        Long idCursoLibre = cursos.save(libreCurso).getId();
        mvc.perform(post("/curso/eliminar/{id}", idCursoLibre)
                .with(user("admin-demo").roles("ADMIN")).with(csrf()))
                .andExpect(redirectedUrl("/curso/index"));
        assertThat(cursos.existsById(idCursoLibre)).isFalse();
        Long idDocenteLibre = libreDocente.getId();
        mvc.perform(post("/docente/eliminar/{id}", idDocenteLibre)
                .with(user("admin-demo").roles("ADMIN")).with(csrf()))
                .andExpect(redirectedUrl("/docente/index"));
        assertThat(docentes.existsById(idDocenteLibre)).isFalse();
    }

    @Test
    @Transactional
    void matriculaCalificadaNoSeRetiraAlDesmarcarCurso() throws Exception {
        var nota = notas.findAll().get(0);
        long idAlumno = nota.getAlumno().getId();
        long idCurso = nota.getCurso().getId();
        mvc.perform(post("/nota/matricula/guardar")
                .with(user("admin-demo").roles("ADMIN")).with(csrf())
                .param("id", Long.toString(idAlumno)))
                .andExpect(redirectedUrl("/nota/matricular/" + idAlumno))
                .andExpect(flash().attributeExists("error"));
        assertThat(notas.existsById(new NotaID(idAlumno, idCurso))).isTrue();
    }

    @Test
    @Transactional
    void matriculaSinNotasSePuedeRetirar() throws Exception {
        Alumno alumno = new Alumno();
        alumno.setNombre("Alumno");
        alumno.setApellido("Temporal");
        alumno.setDni("77777777");
        alumno.setCelular("977777777");
        alumno.setEMail("temporal@example.com");
        long idAlumno = alumnos.save(alumno).getId();
        long idCurso = cursos.findAll().get(0).getId();

        mvc.perform(post("/nota/matricula/guardar")
                .with(user("admin-demo").roles("ADMIN")).with(csrf())
                .param("id", Long.toString(idAlumno))
                .param("idSeleccionados", Long.toString(idCurso)))
                .andExpect(redirectedUrl("/alumno/index"));
        assertThat(notas.existsById(new NotaID(idAlumno, idCurso))).isTrue();

        mvc.perform(post("/nota/matricula/guardar")
                .with(user("admin-demo").roles("ADMIN")).with(csrf())
                .param("id", Long.toString(idAlumno)))
                .andExpect(redirectedUrl("/alumno/index"));
        assertThat(notas.existsById(new NotaID(idAlumno, idCurso))).isFalse();
    }
}
