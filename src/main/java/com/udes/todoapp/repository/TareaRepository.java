package com.udes.todoapp.repository;

import com.udes.todoapp.entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findByCompletada(boolean completada);
}
