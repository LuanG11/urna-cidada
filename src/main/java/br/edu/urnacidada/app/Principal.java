package br.edu.urnacidada.app;

import br.edu.urnacidada.modelo.*;
import br.edu.urnacidada.repositorio.*;
import br.edu.urnacidada.servico.UrnaService;
import javax.swing.SwingUtilities;
import java.util.List;

public class Principal {
    public static void main(String[] args) {
        RepositorioVotos repositorio = criarRepositorio();

        var vereadores = List.of(
                new Vereador(11666, "Pérola", "PCM", "Partido do Capital Maritmo", "imagens/perola.png"),
                new Vereador(15067, "Karen", "PAT", "Partido do Avanço Tecnologico", "imagens/karen.png"),
                new Vereador(30255, "Larry, a Lagosta", "PSF", "Partido da Saúde e Força", "imagens/larry.png"),
                new Vereador(87833, "Sandy Bochechas", "PCI", "Partido da Ciencia E Inovaçao", "imagens/sandy.png"),
                new Vereador(32408, "Sra. Puff", "PDF", "Partido Democratico da Fenda", "imagens/sra-puff.png"));

        var prefeitos = List.of(
                new Prefeito(11, "Sr. Sirigueijo", "PCM", "Partido do Capital Maritmo", "imagens/sirigueijo.png"),
                new Prefeito(15, "Plankton", "PAT", "Partido do Avanço Tecnologico", "imagens/plankton.png"));

        UrnaService urna = new UrnaService(vereadores, prefeitos, repositorio);
        SwingUtilities.invokeLater(() -> new JanelaUrna(urna).setVisible(true));
    }

    private static RepositorioVotos criarRepositorio() {
        try {
            if (System.getenv("GOOGLE_APPLICATION_CREDENTIALS") != null)
                return new RepositorioFirebase();
        } catch (Exception ex) {
            System.err.println("Firebase indisponível; usando modo local: " + ex.getMessage());
        }
        return new RepositorioLocal();
    }
}
