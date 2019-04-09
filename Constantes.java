package bricker.breaker;

import java.awt.Image;
import javax.swing.ImageIcon;

public interface Constantes {

    //Tiempos de espera:
    public static final int SLEEP_1 = 3;
    public static final int SLEEP_2 = 4;
    public static final int SLEEP_3 = 5;
    public static final int SLEEP_4 = 6;

    //Imágenes
    public static final Image LADRILLO3 = new ImageIcon("dureza3.png").getImage();
    public static final Image LADRILLO2 = new ImageIcon("dureza2.png").getImage();
    public static final Image LADRILLO1 = new ImageIcon("dureza1.png").getImage();
    public static final Image BRONCE = new ImageIcon("indestructible.png").getImage();

    //Medidas                              98 &  28 opcional
    public static final int ANCHO_LADRILLO = 90;
    public static final int ALTO_LADRILLO = 20;

    public static final int RADIO_BOLA = 20;

    public static final int ANCHO_PLATAFORMA = 120;
    public static final int ALTO_PLATAFORMA = 20;

    public static final Image FONDO1 = new ImageIcon("FondoNivel1.jpg").getImage();
    public static final Image FONDO2 = new ImageIcon("FondoNivel2.png").getImage();
    public static final Image FONDO3 = new ImageIcon("FondoNivel3.png").getImage();
    // public static final Image PLATAFORMA = new ImageIcon("troncazo.png").getImage();
    public static final Image PLATAFORMA = new ImageIcon("barra.jpg").getImage();
    public static final Image INFORMACION1 = new ImageIcon("informacion1.jpg").getImage();
    public static final Image INFORMACION2 = new ImageIcon("informacion2.jpg").getImage();
    public static final Image INFORMACION3 = new ImageIcon("informacion3.jpg").getImage();
    
    public static final int ANCHO_PANTALLA = 1061;
    public static final int ANCHO_PANTALLA2 = 1366;
    public static final int ALTO_PANTALLA = 748;

    public static final int CANTIDAD_LADRILLOS_X = 10;
    public static final int CANTIDAD_LADRILLOS_Y = 6;

    public static final Image BOLA = new ImageIcon("bola3.png").getImage();
//

// Definicion de los componentes    
}
