package bricker.breaker;

import java.awt.Image;
import javax.swing.ImageIcon;


public interface Constantes {
    
    //Tiempos de espera:
    
    public static final int SLEEP_1 = 1;
    public static final int SLEEP_2 = 2;
    public static final int SLEEP_3 = 3;
    public static final int SLEEP_4 = 4;
    
    //Imágenes
    public static final Image LADRILLO3 = new ImageIcon("piedra.png").getImage();
    public static final Image LADRILLO2 = new ImageIcon("tierra.png").getImage();
    public static final Image LADRILLO1 = new ImageIcon("rocoso.png").getImage();
    
    //Medidas                              98 &  28 opcional
    public static final int ANCHO_LADRILLO = 90;
    public static final int ALTO_LADRILLO = 20;
    
    public static final int RADIO_BOLA = 20;
    
    public static final int ANCHO_PLATAFORMA = 120;
    public static final int ALTO_PLATAFORMA= 20;
    
  
    public static final Image FONDO = new ImageIcon("fondo.jpg").getImage();
   // public static final Image PLATAFORMA = new ImageIcon("troncazo.png").getImage();
    public static final Image PLATAFORMA = new ImageIcon("barra.jpg").getImage();
    public static final Image INFORMACION = new ImageIcon("fondoverde.jpg").getImage();
    
    public static final int ANCHO_PANTALLA = 1061;
    public static final int ANCHO_PANTALLA2 = 1366;
    public static final int ALTO_PANTALLA = 748;
    
    public static final int CANTIDAD_LADRILLOS_X = 10;
    public static final int CANTIDAD_LADRILLOS_Y = 6;
    
    public static final Image BOLA = new ImageIcon("cabeza.png").getImage();
//
    
// Definicion de los componentes    
    
    
}
