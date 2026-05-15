package com.udes.todoapp.controller;

import com.udes.todoapp.entity.Tarea;
import com.udes.todoapp.service.TareaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tareas")
public class TareaWebController {

    private final TareaService service;

    public TareaWebController(TareaService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("tareas", service.listarTodas());
        return "tareas/lista";
    }

    @GetMapping("/nueva")
    public String formularioNueva(Model model) {
        model.addAttribute("tarea", new Tarea());
        return "tareas/nueva";
    }

    @PostMapping
    public String crear(@ModelAttribute Tarea tarea) {
        service.crear(tarea);
        return "redirect:/tareas";
    }
}
