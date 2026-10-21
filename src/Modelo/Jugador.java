package Modelo;

import java.util.ArrayList;

public class Jugador {
    // Variables enteras
    private int numeroJugador;

    // Cola de personas
    private ColaCircular<Persona> colaPersonas;

    // ArrayList para datos de historial o para historial de dados.
    private ArrayList<Dado> dados;
    private ArrayList<Integer> historialDados;
    private ArrayList<Integer> historialPersonas;

    // Dado original del jugador
    private Dado dadoJugador;

    public Jugador (int numeroJugador){
        // Identificador de jugador
        this.numeroJugador = numeroJugador;

        // Cola para almacenar las personas por ronda en los jugadores.
        colaPersonas = new ColaCircular<>();

        // ArrayList para métodos lógicos.
        dados = new ArrayList<>();
        historialDados = new ArrayList<>();
        historialPersonas = new ArrayList<>();

        dadoJugador = new Dado();
        dados.add(dadoJugador);
    }

    public int getNumeroJugador(){
        return numeroJugador;
    }

    public ArrayList<Integer> getHistorialDados(){
        return historialDados;
    }

    public ArrayList<Dado> getDadosActuales(){
        return dados;
    }

    public ColaCircular<Persona> getPersonas(){
        return colaPersonas;
    }

    public void agregarDado(Dado dadoAgregar){
        dados.add(dadoAgregar);
    }

    public int getValorDados(){
        int total = 0;
        for(Dado dado: dados){
            total += dado.getValor();
        }
        return total;
    }

    public void AgregarJugada(int valorDado){
        historialDados.add(valorDado);
    }

    // Falta de probar...
    public Dado eliminarDado(Dado dadoEliminar){
        return dados.remove(dados.size());
    }




}
