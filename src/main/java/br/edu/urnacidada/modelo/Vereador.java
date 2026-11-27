package br.edu.urnacidada.modelo;

public class Vereador extends Candidato {
    public Vereador(int numero, String nome, String sigla, String partido, String imagemPath) {
        super(numero, nome, sigla, partido, imagemPath);
    }

    /** Construtor curto mantido para facilitar testes e exemplos. */
    public Vereador(int numero, String nome, String partido) {
        this(numero, nome, "", partido, "");
    }

    @Override
    public String getCargo() { return "Vereador"; }
}
