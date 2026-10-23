package Modelo;

public class ColaCircular<T> {
    private T[] datos;
    private int inicio, fin, cantidad;

    public ColaCircular(int tamano) {
        datos = (T[]) new Object[tamano];
        inicio = -1;
        fin = -1;
        cantidad = 0;
    }

    public ColaCircular() {
        datos = (T[]) new Object[100];
        inicio = -1;
        fin = -1;
        cantidad = 0;
    }

    public void insertarCircular(T dato) {
        if (((fin == datos.length - 1) && (inicio == 0)) || (fin == inicio - 1)) {
            return;
        } else {
            if (fin == datos.length - 1) {
                fin = 0;
            } else {
                fin += 1;
            }
            datos[fin] = dato;
            cantidad++;
            if (inicio == -1) inicio = 0;
        }
    }

    public T eliminarCircular() {
        if (inicio == -1) return null;
        else {
            T dato = datos[inicio];
            if (inicio == fin) {
                inicio = -1;
                fin = -1;
            } else {

                if (inicio == datos.length - 1) {
                    inicio = 0;
                } else {
                    inicio += 1;
                }
            }
            cantidad--;
            return dato;
        }
    }

    public int tamanoCola() {
        return cantidad;
    }

    public void vaciar() {
        datos = (T[]) new Object[datos.length];
        inicio = -1;
        fin = -1;
        cantidad = 0;
    }

}