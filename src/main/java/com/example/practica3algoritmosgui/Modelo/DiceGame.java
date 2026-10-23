package com.example.practica3algoritmosgui.Modelo;

import java.util.ArrayList;

public class DiceGame {
    // Variables enteras
    public int num_jugadores = 10;
    public int personasIniciales = 4;
    public int rondaActual;
    public int rondasTotales = 20;
    // ArrayList lógicos
    public ArrayList<Jugador> jugadores;
    public ArrayList<Persona> personasFinal;

    public ArrayList<Integer> historialThroughput;
    public ArrayList<Integer> historialPersonasEnSistema;

    public DiceGame (){
        jugadores = new ArrayList<>();
        personasFinal = new ArrayList<>();
        historialThroughput = new ArrayList<>();
        historialPersonasEnSistema = new ArrayList<>();
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
        historialPersonasEnSistema.add(calcularPersonasEnElSistema());
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

    public void moverPersonas(){
        if(haTerminado()) return;
        rondaActual++;

        for(int i = jugadores.size() - 1; i >= 0; i--){
            Jugador actual = jugadores.get(i);
            Jugador siguiente = (i < jugadores.size() - 1) ? jugadores.get(i + 1) : null;

            int valorDado = actual.getValorDados();
            int aMover = calcularMovimiento(actual, siguiente, valorDado);

            for(int k = 0; k < aMover; k++){
                entregarPersonas(tomarPersona(actual), siguiente);
            }

            actual.registrarActividad(valorDado, aMover);
        }

        historialThroughput.add(calcularThroughput());
        historialPersonasEnSistema.add(calcularPersonasEnElSistema());
    }

    private int calcularMovimiento(Jugador actual, Jugador siguiente, int valorDado){
        int mueve = valorDado;

        if(!actual.esFuente() && actual.getCantidadPersonas() < mueve){
            mueve = actual.getCantidadPersonas();
        }
        if(siguiente != null && siguiente.espacioLibre() < mueve){
            mueve = siguiente.espacioLibre();
        }
        return mueve;
    }

    private Persona tomarPersona(Jugador j){
        if(j.esFuente()) return new Persona(rondaActual);
        return j.getPersonas().eliminarCircular();
    }

    public void reiniciarJuego(){
        rondaActual = 0;
        personasFinal.clear();
        historialThroughput.clear();
        historialPersonasEnSistema.clear();

        for(Jugador j : jugadores){
            j.reiniciarJugador();
        }
        agregarPersonasIniciales();
    }

    public int calcularPersonasEnElSistema(){
        int total = 0;
        for(Jugador j : jugadores){
            if(!j.esFuente()) total += j.getCantidadPersonas();
        }
        return total;
    }

    public int calcularThroughput(){
        return personasFinal.size();
    }

    public ArrayList<Integer> getHistorialThroughput(){
        return historialThroughput;
    }

    public ArrayList<Integer> getHistorialPersonasEnSistema(){
        return historialPersonasEnSistema;
    }

    public ArrayList<Integer> getTiemposEnSistema(){
        ArrayList<Integer> tiempos = new ArrayList<>();
        for(Persona p : personasFinal){
            if(!p.personaBase()){
                tiempos.add(p.getSalida() - p.getEntrada());
            }
        }
        return tiempos;
    }

    public boolean haTerminado(){
        return rondaActual >= rondasTotales;
    }

    public Jugador getJugador(int numero){
        if(numero < 1 || numero > jugadores.size()) return null;
        return jugadores.get(numero - 1);
    }
}