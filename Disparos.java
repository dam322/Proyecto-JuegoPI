/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bricker.breaker;

import java.awt.Graphics;

/**
 *
 * @author dasd-
 */
public class Disparos extends Estructura implements Constantes {
    public Disparos(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    public void mover() {
        

            y = y - 1;

         

    }

    public void draw(Graphics g) {

  
        g.drawImage(PROYECTIL, x, y,50,20, null);

    }
}
