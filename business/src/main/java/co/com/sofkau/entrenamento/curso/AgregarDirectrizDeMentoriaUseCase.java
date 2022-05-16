package co.com.sofkau.entrenamento.curso;

import co.com.sofka.business.generic.UseCase;
import co.com.sofka.business.support.RequestCommand;
import co.com.sofka.business.support.ResponseEvents;
import co.com.sofkau.entrenamiento.curso.Curso;
import co.com.sofkau.entrenamiento.curso.commands.AgregarDirectrizDeMentoria;

public class AgregarDirectrizDeMentoriaUseCase extends UseCase<RequestCommand<AgregarDirectrizDeMentoria>, ResponseEvents> {

    @Override
    public void executeUseCase(RequestCommand<AgregarDirectrizDeMentoria> agregarDirectrizDeMentoriaRequestCommand) {
        var command = agregarDirectrizDeMentoriaRequestCommand.getCommand();
        var curso = Curso.from(
                command.getCoursoId(), repository().getEventsBy(command.getCoursoId().value())
        );
        curso.agregarDirectrizDeMentoria(command.getMentoriaId(), command.getDirectiz());

        emit().onResponse(new ResponseEvents(curso.getUncommittedChanges()));
    }
}
