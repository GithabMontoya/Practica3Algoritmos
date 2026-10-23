package com.example.practica3algoritmosgui;

import com.example.practica3algoritmosgui.Controlador.Controlador;
import com.example.practica3algoritmosgui.Modelo.DiceGame;
import com.example.practica3algoritmosgui.Vista.VentanaJuego;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) {
        DiceGame juego = new DiceGame();
        juego.agregarPersonasIniciales();

        VentanaJuego ventana = new VentanaJuego(stage);
        new Controlador(juego, ventana);
    }

    public static void main(String[] args) {
        launch(args);
    }
}