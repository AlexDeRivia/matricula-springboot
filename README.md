# Sistema de control académico — demo local

Aplicación educativa de Spring Boot para registrar alumnos, docentes y cursos, matricular alumnos, cargar calificaciones y consultar su boleta. El perfil `demo` arranca por defecto con una base H2 en memoria y datos completamente ficticios. Los datos vuelven a su estado inicial al reiniciar la aplicación.

## Requisitos

- JDK 21 o superior.
- Conexión a Internet la primera vez que Maven descargue sus dependencias.

## Ejecutar en Windows (PowerShell)

```powershell
$env:JAVA_HOME = 'C:\ruta\a\jdk-21'
$env:DEMO_ADMIN_PASSWORD = 'elige-una-clave-local'
.\mvnw.cmd spring-boot:run
```

Abre `http://localhost:8080`. El usuario `demo` con clave `demo123` puede consultar los listados. Para registrar, editar, matricular o eliminar, inicia sesión como `admin-demo` con el valor de `DEMO_ADMIN_PASSWORD`. Si no defines esa variable, la cuenta administradora no se crea.

La cuenta `demo` solo puede consultar. No uses datos reales ni habilites la cuenta administradora en una instancia pública.

## Demo pública de solo lectura

La demo pública usa el mismo perfil `demo`, con datos ficticios y H2 en memoria. Define estas variables en el servicio web:

```text
SPRING_PROFILES_ACTIVE=demo
DEMO_PUBLIC_READ_ONLY=true
```

No definas `DEMO_ADMIN_PASSWORD` en ese servicio. El modo `DEMO_PUBLIC_READ_ONLY` impide crear la cuenta administradora incluso si se configura una contraseña por error. El puerto se toma de `PORT` cuando lo proporciona el hosting; localmente sigue siendo `8080`.

El `Dockerfile` construye la aplicación con Java 21 y permite desplegarla en Render como servicio web Docker. Para comprobarla con Docker local:

```powershell
docker build -t control-academico-demo .
docker run --rm -p 10000:10000 -e PORT=10000 -e SPRING_PROFILES_ACTIVE=demo -e DEMO_PUBLIC_READ_ONLY=true control-academico-demo
```

Abre `http://localhost:10000` e inicia sesión con `demo` / `demo123`. Al reiniciar el proceso, las altas y cambios de H2 se pierden y se restauran los datos de ejemplo. La instancia pública no necesita una base de datos externa.

## Pruebas

```powershell
.\mvnw.cmd test
```

Los tests inician el perfil demo, comprueban los datos de ejemplo y recorren registro, matrícula, calificaciones, boleta, cálculo de promedios, protección de relaciones y autorización.

## Flujo del MVP

Un administrador registra docentes, cursos y alumnos; matricula al alumno desde su listado; abre la boleta y carga o edita las tres notas de cada curso. Un usuario lector puede consultar los listados y boletas. Las notas admiten valores de 0 a 20 y pueden quedar pendientes.

El promedio de un curso es la media aritmética de sus tres unidades. Solo los cursos con las tres notas participan en el promedio general del alumno, ponderados por sus créditos. Si ninguno está completo, la boleta muestra «Pendiente». El promedio se calcula a partir de las notas, sin introducirlo manualmente.

No se puede eliminar un alumno con matrículas, un curso con alumnos matriculados ni un docente con cursos asignados. Tampoco se puede retirar una matrícula que ya tiene calificaciones; el sistema muestra un mensaje claro y conserva los datos. Para retirarla, primero se deben borrar sus notas desde «Calificar». No hay todavía reporte histórico por periodos académicos ni exportación de boletas; quedan fuera de este MVP.
