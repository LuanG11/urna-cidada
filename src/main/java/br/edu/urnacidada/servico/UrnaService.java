package br.edu.urnacidada.servico;

import br.edu.urnacidada.excecao.VotoInvalidoException;
import br.edu.urnacidada.modelo.Candidato;
import br.edu.urnacidada.modelo.Voto;
import br.edu.urnacidada.repositorio.RepositorioVotos;
import java.time.LocalDate;
import java.util.List;

public class UrnaService {
    private final List<? extends Candidato> vereadores;
    private final List<? extends Candidato> prefeitos;
    private final RepositorioVotos repositorio;
    private int etapa = 0;

    public UrnaService(List<? extends Candidato> vereadores,
                       List<? extends Candidato> prefeitos,
                       RepositorioVotos repositorio) {
        this.vereadores = vereadores;
        this.prefeitos = prefeitos;
        this.repositorio = repositorio;
    }

    public String getCargoAtual() { return etapa == 0 ? "Vereador" : etapa == 1 ? "Prefeito" : "Finalizado"; }
    public List<? extends Candidato> getVereadores() { return vereadores; }
    public List<? extends Candidato> getPrefeitos() { return prefeitos; }
    public RepositorioVotos getRepositorio() { return repositorio; }

    public Candidato consultarCandidato(int numero) {
        List<? extends Candidato> lista = etapa == 0 ? vereadores : prefeitos;
        return lista.stream().filter(c -> c.getNumero() == numero).findFirst().orElse(null);
    }

    public Candidato consultarPorPrefixo(String prefixo) {
        if (prefixo == null || prefixo.length() < 2) return null;
        List<? extends Candidato> lista = etapa == 0 ? vereadores : prefeitos;
        return lista.stream().filter(c -> String.valueOf(c.getNumero()).startsWith(prefixo)).findFirst().orElse(null);
    }

    public void votar(int numero) throws VotoInvalidoException {
        if (etapa > 1) throw new VotoInvalidoException("A votação já foi encerrada.");
        Candidato candidato = consultarCandidato(numero);
        if (candidato == null) throw new VotoInvalidoException("Número não encontrado para este cargo.");
        repositorio.salvar(new Voto(getCargoAtual(), numero)); etapa++;
    }

    public void votarNulo() throws VotoInvalidoException {
        if (etapa > 1) throw new VotoInvalidoException("A votação já foi encerrada.");
        repositorio.salvar(new Voto(getCargoAtual(), -1)); etapa++;
    }

    public void votarBranco() throws VotoInvalidoException {
        if (etapa > 1) throw new VotoInvalidoException("A votação já foi encerrada.");
        repositorio.salvar(new Voto(getCargoAtual(), 0)); etapa++;
    }

    public boolean finalizada() { return etapa > 1; }
    public void reiniciar() { etapa = 0; }
}
