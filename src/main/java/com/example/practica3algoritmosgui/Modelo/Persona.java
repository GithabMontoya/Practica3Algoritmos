package com.example.practica3algoritmosgui.Modelo;

public class Persona {
    private int entrada;
    private int salida;

    public Persona(int entrada) {
        this.entrada = entrada;
        salida = -1;
    }

    public int getEntrada(){
        return entrada;
    }

    public int getSalida(){
        return salida;
    }

    public void setSalida(int salida){
        this.salida = salida;
    }

    public boolean personaBase(){
        return entrada == 0;
    }

    public String toString(){
        return "Persona entró en la ronda " + entrada + " y salió en la ronda " + salida ;
    }
}
