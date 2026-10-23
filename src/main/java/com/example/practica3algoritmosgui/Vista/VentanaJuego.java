package com.example.practica3algoritmosgui.Vista;

import com.example.practica3algoritmosgui.Modelo.Dado;
import com.example.practica3algoritmosgui.Modelo.DiceGame;
import com.example.practica3algoritmosgui.Modelo.Jugador;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class VentanaJuego {
    private int columnas = 5;
    private int filas = 2;

    private final Stage stage;
    private final Label lblRonda = new Label();
    private final Label lblThroughput = new Label();
    private final Label lblWip = new Label();
    private final Label lblMensaje = new Label();
    private final Button btnAccion = new Button("Tirar dados");
    private final Button btnReiniciar = new Button("Reiniciar juego");
    private final GridPane tablero = new GridPane();

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

        tablero.setHgap(10);
        tablero.setVgap(10);
        tablero.setPadding(new Insets(10));
        for (int c = 0; c < columnas; c++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / columnas);
            col.setHgrow(Priority.ALWAYS);
            tablero.getColumnConstraints().add(col);
        }
        for (int f = 0; f < filas; f++) {
            RowConstraints fila = new RowConstraints();
            fila.setVgrow(Priority.ALWAYS);
            tablero.getRowConstraints().add(fila);
        }
        ScrollPane scroll = new ScrollPane(tablero);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

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
        raiz.setCenter(scroll);
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

        tablero.getChildren().clear();
        List<Jugador> jugadores = juego.getJugadores();
        for (int i = 0; i < jugadores.size(); i++) {
            tablero.add(crearPanelJugador(jugadores.get(i)),
                    i % columnas, i / columnas);
        }
    }

    private VBox crearPanelJugador(Jugador j) {
        Label titulo = new Label("Jugador " + j.getNumeroJugador());
        titulo.setFont(Font.font(15));
        titulo.setStyle("-fx-font-weight: bold;");

        Label info;
        if (j.esFuente()) {
            info = new Label("Persona fuente");
        } else {
            info = new Label("En cola: " + j.getCantidadPersonas()
                    + " | Movió: " + j.getPersonasMovidasEnRondaAnterior());
        }
        info.setWrapText(true);

        VBox panel = new VBox(8, titulo, info);
        panel.setPadding(new Insets(8));
        panel.setMinWidth(0);
        panel.setMaxWidth(Double.MAX_VALUE);
        panel.setMaxHeight(Double.MAX_VALUE);
        panel.setStyle("-fx-border-color: #555; -fx-border-radius: 6; -fx-background-radius: 6;"
                + "-fx-background-color: #f4f4f4;");
        panel.setCursor(Cursor.HAND);
        panel.setOnMouseClicked(e -> alClickJugador.accept(j));
        GridPane.setHgrow(panel, Priority.ALWAYS);
        GridPane.setVgrow(panel, Priority.ALWAYS);
        return panel;
    }
}