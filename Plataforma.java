package bricker.breaker;

import java.awt.Graphics;

public class Plataforma extends Estructura {

    public Plataforma(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    public void draw(Graphics g) {
        // g.fillRect(x, y, ancho,alto);
        g.drawImage(PLATAFORMA, x, y, ancho, alto, null);
    }

    public boolean caughtItem(Item i) {
        return (i.getX() < x + ancho) && (i.getX() + i.getAncho() > x) && (y == i.getY() || y == i.getY() - 1);
    }
}
