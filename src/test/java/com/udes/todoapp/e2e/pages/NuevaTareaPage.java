package com.udes.todoapp.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model para la vista /tareas/nueva.
 * Encapsula el formulario de creación de tareas.
 */
public class NuevaTareaPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By inputTitulo = By.id("titulo");
    private final By inputDescripcion = By.id("descripcion");
    private final By btnGuardar = By.id("btn-guardar");
    private final By formulario = By.id("form-nueva-tarea");

    public NuevaTareaPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(formulario));
    }

    public NuevaTareaPage llenarTitulo(String titulo) {
        driver.findElement(inputTitulo).sendKeys(titulo);
        return this;
    }

    public NuevaTareaPage llenarDescripcion(String descripcion) {
        driver.findElement(inputDescripcion).sendKeys(descripcion);
        return this;
    }

    public TareasPage guardar() {
        driver.findElement(btnGuardar).click();
        return new TareasPage(driver);
    }
}
