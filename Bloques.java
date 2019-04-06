package bricker.breaker;

import java.awt.Graphics;
import java.util.Random;

public class Bloques extends Estructura {
    private int dureza=0;
    Random rand = new Random();
    int destroy = 0;
    
    public Bloques(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
        dureza=rand.nextInt(4) + 0;
        System.out.println(dureza);
    }

    public void draw(Graphics g){
        switch (dureza) {
            case 0:
                
                break;
            case 1:
                g.drawImage(LADRILLO2, x, y,ancho,alto,null);
                break;
            case 2:
                g.drawImage(LADRILLO1, x, y,ancho,alto,null);
                break;
            default:
                g.drawImage(LADRILLO3, x, y,ancho,alto,null);
                break;
        }
        
       
        
    }
    


    public void golpe(){
        Sonidos sound = new Sonidos("golpe");
        dureza--;
        
    }
    
    

    public int getDureza() {
        return dureza;
    }
    
    
    
}
