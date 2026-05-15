package com.udes.todoapp.service;

import com.udes.todoapp.entity.Tarea;
import com.udes.todoapp.repository.TareaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TareaServiceTest {

    @Mock
    TareaRepository repo;

    @InjectMocks
    TareaService service;

    @Test
    void crear_conTituloValido_guardaYRetorna() {
        Tarea t = new Tarea();
        t.setTitulo("Estudiar JUnit");
        when(repo.save(any())).thenReturn(t);

        Tarea resultado = service.crear(t);

        assertThat(resultado.getTitulo()).isEqualTo("Estudiar JUnit");
        verify(repo).save(t);
    }

    @Test
    void crear_conTituloVacio_lanzaIllegalArgumentException() {
        Tarea t = new Tarea();
        t.setTitulo("   ");

        assertThrows(IllegalArgumentException.class, () -> service.crear(t));
        verify(repo, never()).save(any());
    }

    @Test
    void crear_conTituloNull_lanzaIllegalArgumentException() {
        Tarea t = new Tarea();
        t.setTitulo(null);

        assertThrows(IllegalArgumentException.class, () -> service.crear(t));
        verify(repo, never()).save(any());
    }

    @Test
    void buscarPorId_noExiste_lanzaEntityNotFoundException() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void buscarPorId_existe_retornaTarea() {
        Tarea t = new Tarea();
        t.setId(1L);
        t.setTitulo("Existe");
        when(repo.findById(1L)).thenReturn(Optional.of(t));

        Tarea resultado = service.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getTitulo()).isEqualTo("Existe");
    }

    @Test
    void completar_tareaExiste_marcaCompletadaYGuarda() {
        Tarea t = new Tarea();
        t.setId(1L);
        t.setTitulo("Pendiente");
        t.setCompletada(false);
        when(repo.findById(1L)).thenReturn(Optional.of(t));
        when(repo.save(any())).thenReturn(t);

        Tarea resultado = service.completar(1L);

        assertThat(resultado.isCompletada()).isTrue();
        verify(repo).save(t);
    }

    @Test
    void listarTodas_retornaListaCompleta() {
        Tarea t1 = new Tarea();
        t1.setTitulo("Una");
        Tarea t2 = new Tarea();
        t2.setTitulo("Dos");
        when(repo.findAll()).thenReturn(List.of(t1, t2));

        List<Tarea> resultado = service.listarTodas();

        assertThat(resultado).hasSize(2)
                .extracting("titulo")
                .containsExactly("Una", "Dos");
    }
}
