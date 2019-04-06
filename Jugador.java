package bricker.breaker;

import java.io.Serializable;

public class Jugador implements Comparable<Jugador>, Serializable{

    private String nombre;
    private int puntaje;
    private int vidas;

    public Jugador(String nombre, int puntaje) {
        this.nombre = nombre;
        this.puntaje = puntaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    @Override
    public int compareTo(Jugador o) {
        if (this.puntaje < o.puntaje) {
            return 1;
        } else if (this.puntaje > o.puntaje) {
            return -1;
        } else {
            return 0;
        }
    }
    
    

}
