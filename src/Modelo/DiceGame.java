package Modelo;

import java.util.ArrayList;

public class DiceGame {
    // Variables enteras
    public int num_jugadores = 10;
    public int personasIniciales = 4;
    public int rondaActual;
    // ArrayList lógicos
    public ArrayList<Jugador> jugadores;
    public ArrayList<Persona> personasFinal;

    public DiceGame (){
        jugadores = new ArrayList<>();
        personasFinal = new ArrayList<>();
        for(int i = 1; i <= num_jugadores; i++){
            jugadores.add(new Jugador(i));
        }
        rondaActual = 0;
    }

    public void agregarPersonasIniciales(){
        for(Jugador j : jugadores){
            for(int i = 0; i < personasIniciales; i++){
                j.getPersonas().insertarCircular(new Persona(0));
            }
        }
    }

    public void lanzarDados(){
        for(Jugador j : jugadores){
            for(Dado d : j.getDadosActuales()){
                d.tirarDado();
            }
        }
    }

    public boolean moverDado(Dado dado, Jugador origen, Jugador destino){
        if(origen == destino || dado == null) return false;
        if(!origen.eliminarDado(dado)) return false;
        destino.agregarDado(dado);
        return true;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public int getRondaActual() {
        return rondaActual;
    }
    public ArrayList<Persona> getPersonasFinal() {
        return personasFinal;
    }

    public void entregarPersonas(Persona persona, Jugador siguiente){
        if (siguiente == null){
            persona.setSalida(rondaActual);
            personasFinal.add(persona);
        } else {
            siguiente.getPersonas().insertarCircular(persona);
        }
    }
}
