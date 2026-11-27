package br.edu.urnacidada;

import br.edu.urnacidada.excecao.VotoInvalidoException;
import br.edu.urnacidada.modelo.*;
import br.edu.urnacidada.repositorio.RepositorioLocal;
import br.edu.urnacidada.servico.UrnaService;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UrnaServiceTest {
    private UrnaService criarUrna() {
        return new UrnaService(
                List.of(new Vereador(10123, "Ana", "Sol")),
                List.of(new Prefeito(10, "Clara", "Sol")),
                new RepositorioLocal());
    }

    @Test void deveComecarComVereador() { assertEquals("Vereador", criarUrna().getCargoAtual()); }

    @Test void deveAvancarParaPrefeito() throws VotoInvalidoException {
        UrnaService urna = criarUrna(); urna.votar(10123);
        assertEquals("Prefeito", urna.getCargoAtual());
    }

    @Test void deveEncontrarPartidoPeloInicioDoNumero() {
        UrnaService urna = criarUrna();
        assertEquals("Ana", urna.consultarPorPrefixo("10").getNome());
    }

    @Test void deveRejeitarNumeroInexistente() {
        assertThrows(VotoInvalidoException.class, () -> criarUrna().votar(99999));
    }

    @Test void deveRegistrarVotoNuloEAvancar() throws VotoInvalidoException {
        UrnaService urna = criarUrna();
        urna.votarNulo();
        assertEquals("Prefeito", urna.getCargoAtual());
    }

    @Test void deveEncerrarDepoisDosDoisCargos() throws VotoInvalidoException {
        UrnaService urna = criarUrna(); urna.votar(10123); urna.votar(10);
        assertTrue(urna.finalizada());
    }

    @Test void devePrepararUrnaParaOutroEleitorSemApagarOCicloAnterior() throws VotoInvalidoException {
        UrnaService urna = criarUrna();
        urna.votar(10123);
        urna.votar(10);
        urna.reiniciar();
        assertEquals("Vereador", urna.getCargoAtual());
        urna.votar(10123);
        assertEquals("Prefeito", urna.getCargoAtual());
    }
}
