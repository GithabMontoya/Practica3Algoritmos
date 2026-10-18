package Modelo;

import java.util.ArrayList;

public class Jugador {
    private ColaCircular<Ficha> colaFichas;
    private ArrayList<Dado> dados;
    private int numeroJugador;
    private Dado dadoJugador;

    public Jugador (int numeroJugador){
        this.numeroJugador = numeroJugador;
        colaFichas = new ColaCircular<>();
        dados = new ArrayList<>();
    }

    public ArrayList<Dado> getDados(){
        return dados;
    }

}
