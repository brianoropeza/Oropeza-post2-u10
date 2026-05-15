package com.udes.todoapp.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model para la vista /tareas.
 * Encapsula selectores y acciones sobre la lista de tareas.
 */
public class TareasPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Selectores como constantes privadas (patrón POM)
    private final By btnNueva = By.id("btn-nueva");
    private final By listItems = By.cssSelector(".tarea-item");
    private final By tituloPagina = By.tagName("h1");

    public TareasPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public TareasPage abrir(String baseUrl) {
        driver.get(baseUrl + "/tareas");
        wait.until(ExpectedConditions.presenceOfElementLocated(btnNueva));
        return this;
    }

    public int contarTareas() {
        return driver.findElements(listItems).size();
    }

    public String obtenerTituloPagina() {
        return driver.findElement(tituloPagina).getText();
    }

    public NuevaTareaPage irANuevaTarea() {
        driver.findElement(btnNueva).click();
        return new NuevaTareaPage(driver);
    }
}
