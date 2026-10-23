package com.example.practica3algoritmosgui.Vista;

import com.example.practica3algoritmosgui.Modelo.Dado;
import com.example.practica3algoritmosgui.Modelo.DiceGame;
import com.example.practica3algoritmosgui.Modelo.Jugador;
import com.example.practica3algoritmosgui.Modelo.Persona;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class VentanaJuego {
    private int columnas = 5;
    private int filas = 2;
    private int maxFichasDibujadas = 45;

    private final Stage stage;
    private final Label lblRonda = new Label();
    private final Label lblThroughput = new Label();
    private final Label lblWip = new Label();
    private final Label lblMensaje = new Label();
    private final Label lblSalida = new Label("Salida: 0 personas");
    private final Button btnAccion = new Button("Tirar dados");
    private final Button btnReiniciar = new Button("Reiniciar juego");
    private final GridPane tablero = new GridPane();
    private final FlowPane zonaSalida = new FlowPane(3, 3);

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

        zonaSalida.setPadding(new Insets(6));
        ScrollPane scrollSalida = new ScrollPane(zonaSalida);
        scrollSalida.setFitToWidth(true);
        lblSalida.setStyle("-fx-font-weight: bold;");

        VBox panelSalida = new VBox(4, lblSalida, scrollSalida);
        panelSalida.setPadding(new Insets(0, 10, 10, 10));
        VBox.setVgrow(scrollSalida, Priority.ALWAYS);

        SplitPane centro = new SplitPane(scroll, panelSalida);
        centro.setOrientation(Orientation.VERTICAL);
        centro.setDividerPositions(0.72);

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
        raiz.setCenter(centro);
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
            tablero.add(crearPanelJugador(jugadores.get(i), dadoSeleccionado),
                    i % columnas, i / columnas);
        }
        actualizarSalida(juego);
    }

    private VBox crearPanelJugador(Jugador j, Dado dadoSeleccionado) {
        Label titulo = new Label("Jugador " + j.getNumeroJugador());
        titulo.setFont(Font.font(15));
        titulo.setStyle("-fx-font-weight: bold;");

        FlowPane dados = new FlowPane(6, 6); // si no caben, saltan de línea
        dados.setAlignment(Pos.CENTER_LEFT);
        if (j.getDadosActuales().isEmpty()) {
            dados.getChildren().add(crearRanuraVacia());
        } else {
            for (Dado d : j.getDadosActuales()) {
                dados.getChildren().add(crearDado(j, d, d == dadoSeleccionado));
            }
        }

        Label info;
        FlowPane cola = new FlowPane(3, 3); // se ajusta al ancho real del panel
        cola.setPrefWrapLength(100);
        if (j.esFuente()) {
            info = new Label("Persona fuente");
        } else {
            info = new Label("En cola: " + j.getCantidadPersonas()
                    + " | Movió: " + j.getPersonasMovidasEnRondaAnterior());
            llenarCola(cola, j);
        }
        info.setWrapText(true);

        VBox panel = new VBox(8, titulo, dados, info, cola);
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

    private StackPane crearDado(Jugador j, Dado d, boolean seleccionado) {
        Rectangle fondo = new Rectangle(36, 36);
        fondo.setArcWidth(10);
        fondo.setArcHeight(10);
        fondo.setFill(seleccionado ? Color.GOLD : Color.WHITE);
        fondo.setStroke(seleccionado ? Color.DARKORANGE : Color.DARKSLATEGRAY);
        fondo.setStrokeWidth(seleccionado ? 3 : 1.5);

        Label valor = new Label(String.valueOf(d.getValor()));
        valor.setFont(Font.font(16));
        valor.setStyle("-fx-font-weight: bold;");

        StackPane dado = new StackPane(fondo, valor);
        dado.setCursor(Cursor.HAND);
        dado.setOnMouseClicked(e -> {
            alClickDado.accept(j, d);
            e.consume();
        });
        return dado;
    }

    private StackPane crearRanuraVacia() {
        Rectangle ranura = new Rectangle(36, 36);
        ranura.setArcWidth(10);
        ranura.setArcHeight(10);
        ranura.setFill(Color.TRANSPARENT);
        ranura.setStroke(Color.GRAY);
        ranura.getStrokeDashArray().addAll(5.0, 4.0);
        return new StackPane(ranura);
    }

    private void llenarCola(FlowPane cola, Jugador j) {
        int total = j.getCantidadPersonas();
        int dibujar = Math.min(total, maxFichasDibujadas);
        for (int i = 0; i < dibujar; i++) {
            Persona p = j.getPersonaEn(i);
            if (p == null) continue;
            if (p.personaBase()) {
                cola.getChildren().add(new Rectangle(11, 11, Color.STEELBLUE));
            } else {
                cola.getChildren().add(new Circle(5.5, Color.SEAGREEN));
            }
        }
        if (total > dibujar) {
            cola.getChildren().add(new Label("+" + (total - dibujar)));
        }
    }

    private void actualizarSalida(DiceGame juego) {
        zonaSalida.getChildren().clear();
        List<Persona> salidas = juego.getPersonasFinal();
        lblSalida.setText("Salida: " + salidas.size() + " personas");

        for (Persona p : salidas) {
            Node ficha;
            if (p.personaBase()) {
                ficha = new Rectangle(11, 11, Color.STEELBLUE);
            } else {
                ficha = new Circle(5.5, Color.SEAGREEN);
            }
            Tooltip.install(ficha, new Tooltip(p.toString())); // entrada y salida al pasar el mouse
            zonaSalida.getChildren().add(ficha);
        }
    }
}