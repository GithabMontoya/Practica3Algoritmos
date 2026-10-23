package com.example.practica3algoritmosgui.Vista;

import com.example.practica3algoritmosgui.Modelo.Dado;
import com.example.practica3algoritmosgui.Modelo.DiceGame;
import com.example.practica3algoritmosgui.Modelo.Jugador;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class VentanaJuego {

    private final Stage stage;
    private final Label lblRonda = new Label();
    private final Label lblThroughput = new Label();
    private final Label lblWip = new Label();
    private final Label lblMensaje = new Label();
    private final Button btnAccion = new Button("Tirar dados");
    private final Button btnReiniciar = new Button("Reiniciar juego");

    private Runnable alAccion = () -> { };
    private Runnable alReiniciar = () -> { };
    private BiConsumer<Jugador, Dado> alClickDado = (j, d) -> { };
    private Consumer<Jugador> alClickJugador = j -> { };

    public VentanaJuego(Stage stage) {
        this.stage = stage;
        construir();
    }

    private void construir() {
        Font fuente = Font.font(16);
        lblRonda.setFont(Font.font(18));
        lblThroughput.setFont(fuente);
        lblWip.setFont(fuente);
        lblMensaje.setFont(Font.font(14));

        HBox superior = new HBox(30, lblRonda, lblThroughput, lblWip);
        superior.setPadding(new Insets(10));
        superior.setAlignment(Pos.CENTER_LEFT);

        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        btnReiniciar.setVisible(false);
        btnReiniciar.setManaged(false);
        HBox inferior = new HBox(15, lblMensaje, espacio, btnAccion, btnReiniciar);
        inferior.setPadding(new Insets(10));
        inferior.setAlignment(Pos.CENTER_LEFT);

        btnAccion.setOnAction(e -> alAccion.run());
        btnReiniciar.setOnAction(e -> alReiniciar.run());

        BorderPane raiz = new BorderPane();
        raiz.setTop(superior);
        raiz.setCenter(new Label("Aquí irá el tablero de jugadores"));
        raiz.setBottom(inferior);

        stage.setTitle("Juego de dados - Línea de producción");
        stage.setScene(new Scene(raiz, 1200, 800));
        stage.setMinWidth(950);
        stage.setMinHeight(650);
        stage.setMaximized(true);
        stage.show();
    }

    public void setAlAccion(Runnable r) { alAccion = r; }
    public void setAlReiniciar(Runnable r) { alReiniciar = r; }
    public void setAlClickDado(BiConsumer<Jugador, Dado> c) { alClickDado = c; }
    public void setAlClickJugador(Consumer<Jugador> c) { alClickJugador = c; }

    public void setTextoAccion(String texto) { btnAccion.setText(texto); }
    public void setMensaje(String texto) { lblMensaje.setText(texto); }

    public void mostrarFin(boolean fin) {
        btnAccion.setVisible(!fin);
        btnAccion.setManaged(!fin);
        btnReiniciar.setVisible(fin);
        btnReiniciar.setManaged(fin);
    }

    public void actualizar(DiceGame juego, Dado dadoSeleccionado) {
        int rondaMostrada = juego.haTerminado() ? juego.getRondaActual() : juego.getRondaActual() + 1;
        lblRonda.setText("Ronda: " + rondaMostrada + " / " + juego.rondasTotales);
        lblThroughput.setText("Throughput: " + juego.calcularThroughput());
        lblWip.setText("Personas en el sistema: " + juego.calcularPersonasEnElSistema());

    }
}