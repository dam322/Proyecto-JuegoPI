package bricker.breaker;

import java.awt.Graphics;
import java.util.Random;

public class Bloques extends Estructura {
    private int dureza=0;
    Random rand = new Random();
    int destroy = 0;
    
    public Bloques(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
        dureza=rand.nextInt(5) + 0;
        System.out.println(dureza);
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
    


    public void golpe(){
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
