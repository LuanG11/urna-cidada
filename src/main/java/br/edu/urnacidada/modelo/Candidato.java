package br.edu.urnacidada.modelo;

/** Abstração comum aos candidatos da simulação. */
public abstract class Candidato {
    private final int numero;
    private final String nome;
    private final String siglaPartido;
    private final String nomePartido;
    private final String imagemPath;

    protected Candidato(int numero, String nome, String siglaPartido,
                        String nomePartido, String imagemPath) {
        this.numero = numero;
        this.nome = nome;
        this.siglaPartido = siglaPartido;
        this.nomePartido = nomePartido;
        this.imagemPath = imagemPath;
    }

    public int getNumero() { return numero; }
    public String getNome() { return nome; }
    public String getSiglaPartido() { return siglaPartido; }
    public String getNomePartido() { return nomePartido; }
    public String getPartido() { return nomePartido; }
    public String getImagemPath() { return imagemPath; }
    public abstract String getCargo();
}
