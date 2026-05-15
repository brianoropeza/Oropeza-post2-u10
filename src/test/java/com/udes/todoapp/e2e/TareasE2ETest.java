package com.udes.todoapp.e2e;

import com.udes.todoapp.e2e.pages.NuevaTareaPage;
import com.udes.todoapp.e2e.pages.TareasPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas E2E end-to-end con Selenium WebDriver en modo headless.
 * Levanta la aplicación real en un puerto aleatorio y la prueba con Chrome headless.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TareasE2ETest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--headless=new");
        opts.addArguments("--no-sandbox");
        opts.addArguments("--disable-gpu");
        opts.addArguments("--disable-dev-shm-usage");
        opts.addArguments("--window-size=1280,800");

        driver = new ChromeDriver(opts);
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void paginaTareas_cargaCorrectamente() {
        TareasPage page = new TareasPage(driver).abrir(baseUrl);

        assertThat(driver.getTitle()).contains("Tareas");
        assertThat(page.obtenerTituloPagina()).isEqualTo("Mis Tareas");
    }

    @Test
    void crearNuevaTarea_apareceEnElListado() {
        TareasPage tareasPage = new TareasPage(driver).abrir(baseUrl);
        int conteoInicial = tareasPage.contarTareas();

        NuevaTareaPage nuevaPage = tareasPage.irANuevaTarea();
        TareasPage listadoFinal = nuevaPage
                .llenarTitulo("Tarea E2E desde Selenium")
                .llenarDescripcion("Creada por el test automatizado")
                .guardar();

        assertThat(listadoFinal.contarTareas()).isEqualTo(conteoInicial + 1);
    }
}
