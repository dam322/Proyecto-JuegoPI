package bricker.breaker;

import java.awt.Container;
import javax.swing.JFrame;

public class Main implements Constantes{

        private static JFrame ventana;
	private static Tablero tablero;
	private static Container pane;
    
    public static void main(String[] args) {
        
                ventana = new JFrame("Brick Breaker 1.2");
		ventana.setSize(ANCHO_PANTALLA2,ALTO_PANTALLA);
		ventana.setResizable(false);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                tablero = new Tablero(ANCHO_PANTALLA,ALTO_PANTALLA);
                
                
		pane = ventana.getContentPane();
          
                
		pane.add(tablero);

                ventana.setVisible(true);
    
    }
    
}
