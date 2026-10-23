package com.example.practica3algoritmosgui.Controlador;

import com.example.practica3algoritmosgui.Modelo.Dado;
import com.example.practica3algoritmosgui.Modelo.DiceGame;
import com.example.practica3algoritmosgui.Modelo.Jugador;
import com.example.practica3algoritmosgui.Vista.VentanaJuego;

public class Controlador {
    private final DiceGame juego;
    private final VentanaJuego vista;

    private Dado dadoSeleccionado;
    private Jugador jugadorOrigen;

    private boolean debeLanzar;

    public Controlador(DiceGame juego, VentanaJuego vista) {
        this.juego = juego;
        this.vista = vista;
        this.debeLanzar = true;

        vista.setAlAccion(this::manejarAccionRonda);
        vista.setAlReiniciar(this::manejarReinicio);
        vista.setAlClickDado(this::manejarClickDado);
        vista.setAlClickJugador(this::manejarClickJugador);

        vista.setMensaje("Puedes reasignar dados antes de tirar.");
        refrescar();
    }

    private void manejarAccionRonda() {
        if (debeLanzar) {
            deseleccionar();
            juego.lanzarDados();
            debeLanzar = false;
            vista.setTextoAccion("Mover personas");
            vista.setMensaje("Dados lanzados. Ahora mueve a las personas.");
        } else {
            juego.moverPersonas();
            if (juego.haTerminado()) {
                vista.mostrarFin(true);
                vista.setMensaje("Fin de la partida. Throughput final: " + juego.calcularThroughput());
            } else {
                debeLanzar = true;
                vista.setTextoAccion("Tirar dados");
                vista.setMensaje("Puedes reasignar dados antes de tirar.");
            }
        }
        refrescar();
    }

    private void manejarReinicio() {
        deseleccionar();
        juego.reiniciarJuego();
        debeLanzar = true;
        vista.mostrarFin(false);
        vista.setTextoAccion("Tirar dados");
        vista.setMensaje("Puedes reasignar dados antes de tirar.");
        refrescar();
    }

    private void manejarClickDado(Jugador jugador, Dado dado) {
        if (!debeLanzar) return;

        if (dado == dadoSeleccionado) {
            deseleccionar();
        } else {
            dadoSeleccionado = dado;
            jugadorOrigen = jugador;
        }
        refrescar();
    }

    private void manejarClickJugador(Jugador destino) {
        if (!debeLanzar || dadoSeleccionado == null) return;
        if (destino == jugadorOrigen) return;

        juego.moverDado(dadoSeleccionado, jugadorOrigen, destino);
        deseleccionar();
        refrescar();
    }

    private void deseleccionar() {
        dadoSeleccionado = null;
        jugadorOrigen = null;
    }

    private void refrescar() {
        vista.actualizar(juego, dadoSeleccionado);
    }
}