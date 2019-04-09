package bricker.breaker;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Bloques extends Estructura {

    int dureza;
    public Item item;
    Color itemColor;
    Random rand = new Random();

    public Bloques(int x, int y, int ancho, int alto, int itemType) {
        super(x, y, ancho, alto);
        dureza = rand.nextInt(5) + 0;

        if (itemType == 1) {
            itemColor = Color.GREEN;
        }
        if (itemType == 2) {
            itemColor = Color.RED;
        }

        //Places an item of specified type inside the brick to fall when the brick is destroyed
        item = new Item(x + (ancho / 4), y + (alto / 4), 50, 20, itemType);
    }

    public void draw(Graphics g) {
        switch (dureza) {
            case 0:
                break;
            case 1:
                g.drawImage(LADRILLO1, x, y, ancho, alto, null);
                break;
            case 2:
                g.drawImage(LADRILLO2, x, y, ancho, alto, null);
                break;
            case 3:
                g.drawImage(LADRILLO3, x, y, ancho, alto, null);
                break;
            case 4:
                g.drawImage(BRONCE, x, y, ancho, alto, null);
                break;

        }

    }

    public boolean caughtDisparo(Disparos i) {
        if ((i.getY() == (y + ALTO_LADRILLO)) && (i.getX() <= x + ANCHO_LADRILLO - 28 && i.getX() + 28 >= x)) {

            return true;
        }

        return false;

    }

    public void golpe() {
        if (dureza != 4) {
            dureza--;
            Sonidos sound = new Sonidos("golpe");

        }
        Sonidos sound = new Sonidos("golpe");

    }

    public int getDureza() {
        return dureza;
    }

    public void SetDureza(int dureza) {

        this.dureza = dureza;
    }

}
