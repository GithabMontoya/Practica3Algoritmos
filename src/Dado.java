package com.example.practica3algoritmos.Modelo;

import java.util.Random;

public class Dado {
    private int valor;
    private int caras;
    private Random rnd;

    public Dado() {
        caras = 6;
        rnd = new Random();
        valor = 1;
    }

    public int tirarDado() {
        valor = rnd.nextInt(caras) + 1;
        return valor;
    }

    public int getValor() {
        return valor;
    }

    public int getCaras() {
        return caras;
    }

    public void setCaras(int caras) {
        this.caras = caras;
    }

    @Override
    public String toString() {
        return "Valor: " + valor;
    }
}
