# oropeza-post2-u10

**Programación Web — Unidad 10**
**Post-Contenido 2: Pruebas E2E con Selenium, Postman y Newman**
Ingeniería de Sistemas — UDES 2026

---

## Descripción

Aplicación Spring Boot de gestión de tareas con tres niveles de pruebas E2E:

- **Selenium WebDriver** con el patrón **Page Object Model** sobre una vista Thymeleaf real
- **Postman** — colección de 5 requests REST con test scripts (status, body, variables encadenadas)
- **Newman** — ejecución de la colección automatizada en un pipeline de **GitHub Actions**

## Requisitos

- Java 17+
- Maven 3.9.x
- Google Chrome (estable) para los tests de Selenium en local
- Node.js 18+ con npm (para Newman local: `npm install -g newman`)
- Cuenta GitHub con Actions habilitado

## Estructura del proyecto

```
oropeza-post2-u10/
├── pom.xml
├── README.md
├── .github/workflows/
│   └── api-tests.yml              ← Pipeline CI con Newman
├── postman/
│   ├── ColeccionToDo.json         ← Colección de 5 requests
│   ├── env-local.json             ← Entorno local
│   └── env-ci.json                ← Entorno para GitHub Actions
└── src/
    ├── main/
    │   ├── java/com/udes/todoapp/
    │   │   ├── controller/        ← TareaController (REST) + TareaWebController (Thymeleaf)
    │   │   ├── entity/, repository/, service/, exception/
    │   └── resources/
    │       ├── application.properties
    │       └── templates/tareas/  ← lista.html, nueva.html
    └── test/java/com/udes/todoapp/
        ├── service/, controller/, repository/   ← Tests del Post 1 (JUnit + Mockito)
        └── e2e/
            ├── TareasE2ETest.java               ← Test E2E con Selenium
            └── pages/
                ├── TareasPage.java              ← Page Object listado
                └── NuevaTareaPage.java          ← Page Object formulario
```

## Ejecutar la aplicación localmente

```bash
mvn spring-boot:run
```

Abre `http://localhost:8080/tareas` en el navegador.

## Cómo ejecutar las pruebas

### Tests unitarios e integración (JUnit + Mockito + MockMvc + DataJpaTest)

```bash
mvn test
```

Estos NO incluyen Selenium (se excluyen del ciclo de Surefire).

### Tests E2E con Selenium (requieren Chrome instalado)

```bash
mvn verify
```

`verify` ejecuta tanto los tests unitarios como los E2E (Failsafe).
Los tests de Selenium corren en **modo headless**, así que no abren ventana del navegador.

### Tests de API con Newman (en local)

1. Asegúrate de tener la app corriendo (`mvn spring-boot:run` en otra terminal).
2. Ejecuta:

```bash
newman run postman/ColeccionToDo.json --environment postman/env-local.json
```

Resultado esperado: 5 requests, todos con status verde, **0 failures**.

## Checkpoint 1 — Page Object Model con Selenium

Los Page Objects encapsulan los selectores y acciones de cada vista:

- **`TareasPage`** — selectores `#btn-nueva`, `.tarea-item`, etc. Métodos: `abrir()`, `contarTareas()`, `irANuevaTarea()`.
- **`NuevaTareaPage`** — selectores `#titulo`, `#descripcion`, `#btn-guardar`. Métodos: `llenarTitulo()`, `llenarDescripcion()`, `guardar()`.

El test `TareasE2ETest` levanta la app real en un puerto aleatorio (`@SpringBootTest(webEnvironment = RANDOM_PORT)`), arranca Chrome con `WebDriverManager` (sin descarga manual de driver) y valida dos flujos:

1. `paginaTareas_cargaCorrectamente` — la página `/tareas` responde y tiene el título esperado
2. `crearNuevaTarea_apareceEnElListado` — flujo completo: clic en "Nueva", llenar formulario, guardar, verificar que la lista creció

## Checkpoint 2 — Colección Postman con 5 requests

| # | Método | Ruta | Test scripts |
|---|--------|------|--------------|
| 1 | POST   | `/api/tareas` | Status 201, id numérico, tiempo < 500ms, guarda `tareaId` |
| 2 | GET    | `/api/tareas/{{tareaId}}` | Status 200, body correcto, completada=false |
| 3 | PATCH  | `/api/tareas/{{tareaId}}/completar` | Status 200, completada=true |
| 4 | GET    | `/api/tareas/{{tareaId}}` | Status 200, completada=true (persistido) |
| 5 | GET    | `/api/tareas/99999` | Status 404, body con `error` |

La variable `tareaId` se establece en el request #1 y se reutiliza en los siguientes — los requests están **encadenados**.

## Checkpoint 3 — Newman en GitHub Actions

El workflow `.github/workflows/api-tests.yml` se dispara en cada `push` y `pull_request` a `main`. Pasos:

1. Checkout del código
2. Java 17 (Temurin)
3. `mvn -B package -DskipTests` → compila el JAR
4. Inicia la app en segundo plano (`java -jar target/*.jar &`)
5. Espera a que `/actuator/health` responda OK (con retry hasta 60s)
6. Instala Node.js + Newman
7. Ejecuta la colección con `newman run` apuntando a `env-ci.json`
8. Sube el reporte JSON como artifact (visible incluso si falla)

El check verde en la pestaña **Actions** del repo es la evidencia 3.

## Evidencias

- `evidencias/selenium-tests-verde.png` — captura del terminal/IDE con los tests de Selenium en verde
- `evidencias/postman-runner.png` — captura del Postman Runner con 0 failures
- `evidencias/github-actions-passing.png` — captura del workflow en GitHub Actions con check verde
