package com.udes.todoapp.service;

import com.udes.todoapp.entity.Tarea;
import com.udes.todoapp.repository.TareaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TareaService {

    private final TareaRepository repo;

    public TareaService(TareaRepository repo) {
        this.repo = repo;
    }

    public List<Tarea> listarTodas() {
        return repo.findAll();
    }

    public Tarea crear(Tarea tarea) {
        if (tarea.getTitulo() == null || tarea.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        return repo.save(tarea);
    }

    public Tarea buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarea no encontrada: " + id));
    }

    public Tarea completar(Long id) {
        Tarea t = buscarPorId(id);
        t.setCompletada(true);
        return repo.save(t);
    }
}
