package br.edu.urnacidada.excecao;

public class VotoInvalidoException extends Exception {
    public VotoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
