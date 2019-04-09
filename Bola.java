package bricker.breaker;
import java.awt.Graphics;

public class Bola extends Estructura{
        public int contador=0;
        private int direccionX;
        private int direccionY;
        
    public Bola(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }
    
    public void mover (int velocidad){
            try{
              
                x=x+direccionX;
                y=y+direccionY;
               
        Thread.sleep(velocidad);
     }catch(InterruptedException e){}

                contador++;
    }
    
    public void setDirecciones(int x,int y){
        direccionX=x;
        direccionY=y;
    }

    public int getDireccionX() {
        return direccionX;
    }

    public int getDireccionY() {
        return direccionY;
    }
  
    public void draw(Graphics g){
        //g.fillOval(x, y, RADIO_BOLA,RADIO_BOLA);
        g.drawImage(BOLA, x, y, RADIO_BOLA,RADIO_BOLA,null);
       
}
}