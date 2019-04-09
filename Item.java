package bricker.breaker;

import java.awt.Graphics;

/**
 *
 * @author dasd-
 */
public class Item extends Estructura implements Constantes {

    //Variables
    private int type;

    //Constructor
    public Item(int x, int y, int width, int height, int type) {
        super(x, y, width, height);
        setType(type);
    }

    //Draw an item
    public void draw(Graphics g) {
        if (type == 2) {
            g.drawImage(IMAN, x, y, 30, 30, null);

        }

        if (type == 3) {

            g.drawImage(CANON, x, y, 30, 30, null);
        }
    }

    //Drop the item down towards the paddle at slow pace
    public void drop() {
        y += 2;
    }

    //Resize the paddle, depending on which item is caught. Changes in increments of 15 until min/max width is reached.
    public void resizePaddle(Plataforma p) {
        if (getType() == 1 && p.getAncho() < 200) {
            p.setAncho(p.getAncho() + 15);
        } else if (getType() == 2 && p.getAncho() > 50) {
            p.setAncho(p.getAncho() - 15);
        }
    }

    //Set the item's type
    public void setType(int type) {
        this.type = type;
    }

    //Get the item's type
    public int getType() {
        return type;
    }
}
