package com.udes.todoapp.controller;

import com.udes.todoapp.entity.Tarea;
import com.udes.todoapp.service.TareaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService service;

    public TareaController(TareaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tarea crear(@Valid @RequestBody Tarea tarea) {
        return service.crear(tarea);
    }

    @GetMapping("/{id}")
    public Tarea obtener(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PatchMapping("/{id}/completar")
    public Tarea completar(@PathVariable Long id) {
        return service.completar(id);
    }
}
