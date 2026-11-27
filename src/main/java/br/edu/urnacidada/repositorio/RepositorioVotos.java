package br.edu.urnacidada.repositorio;

import br.edu.urnacidada.modelo.Voto;
import java.time.LocalDate;
import java.util.List;

public interface RepositorioVotos {
    void salvar(Voto voto);

    List<Voto> listarPorData(LocalDate data);
}
