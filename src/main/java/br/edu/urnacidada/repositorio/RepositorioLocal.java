package br.edu.urnacidada.repositorio;

import br.edu.urnacidada.modelo.Voto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioLocal implements RepositorioVotos {
    private final List<Voto> votos = new ArrayList<>();

    @Override
    public void salvar(Voto voto) { votos.add(voto); }

    public List<Voto> listar() { return Collections.unmodifiableList(votos); }

    @Override
    public List<Voto> listarPorData(LocalDate data) {
        return votos.stream()
                .filter(voto -> voto.getHorario().toLocalDate().equals(data))
                .collect(Collectors.toList());
    }
}
