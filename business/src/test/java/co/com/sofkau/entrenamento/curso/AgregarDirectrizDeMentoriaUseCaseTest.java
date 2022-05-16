package co.com.sofkau.entrenamento.curso;

import co.com.sofka.business.generic.UseCaseHandler;
import co.com.sofka.business.repository.DomainEventRepository;
import co.com.sofka.business.support.RequestCommand;
import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkau.entrenamiento.curso.commands.AgregarDirectrizDeMentoria;
import co.com.sofkau.entrenamiento.curso.events.CursoCreado;
import co.com.sofkau.entrenamiento.curso.events.DirectrizAgregadaAMentoria;
import co.com.sofkau.entrenamiento.curso.events.MentoriaCreada;
import co.com.sofkau.entrenamiento.curso.values.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AgregarDirectrizDeMentoriaUseCaseTest {
    @InjectMocks
    private AgregarDirectrizDeMentoriaUseCase useCase;

    @Mock
    private DomainEventRepository repository;

    @Test
    public void agregarDirectrizDeMentoriaHappyPass(){
        //arrange
        CursoId coursoId = CursoId.of("ddddd");
        MentoriaId mentoriaId = MentoriaId.of("UC01");
        Directiz directiz = new Directiz("Tests useCase");
        var command = new AgregarDirectrizDeMentoria( coursoId,  mentoriaId, directiz);

        when(repository.getEventsBy("ddddd")).thenReturn(history());
        useCase.addRepository(repository);
        //act

        List<DomainEvent> events = UseCaseHandler.getInstance()
                .setIdentifyExecutor(command.getCoursoId().value())
                .syncExecutor(useCase, new RequestCommand<>(command))
                .orElseThrow()
                .getDomainEvents();

        //assert
        var event = (DirectrizAgregadaAMentoria)events.get(0);
        Assertions.assertEquals("Tests useCase", event.getDirectiz().value());

    }

    private List<DomainEvent> history() {
        Nombre nombre = new Nombre("DDD");
        Descripcion descripcion = new Descripcion("DDD useCase");
        var event = new CursoCreado(
                nombre,
                descripcion
        );

        MentoriaId mentoriaId = MentoriaId.of("UC01");
        Nombre nombreMentoria = new Nombre("Aprendiendo de casos de usos");
        Fecha fecha = new Fecha(LocalDateTime.now(), LocalDate.now());
        var event2 = new MentoriaCreada(
                mentoriaId,
                nombreMentoria,
                fecha

        );
        event.setAggregateRootId("xxxxx");
        return List.of(event, event2);
    }
}
