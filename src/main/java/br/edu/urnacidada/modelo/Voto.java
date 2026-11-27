package br.edu.urnacidada.modelo;

import java.time.LocalDateTime;

public class Voto {
    private final String cargo;
    private final int numero;
    private final LocalDateTime horario;

    public Voto(String cargo, int numero) {
        this.cargo = cargo;
        this.numero = numero;
        this.horario = LocalDateTime.now();
    }

    public String getCargo() { return cargo; }
    public int getNumero() { return numero; }
    public LocalDateTime getHorario() { return horario; }
}
