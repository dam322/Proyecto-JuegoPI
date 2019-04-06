package bricker.breaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public final class Tablero extends JPanel implements Constantes, Runnable {

    private AtomicBoolean isPaused = new AtomicBoolean(true);
    private final Thread game;
    private final Plataforma plataforma;
    private final Bola bola;
    private final Bloques[][] bloques = new Bloques[CANTIDAD_LADRILLOS_Y][CANTIDAD_LADRILLOS_X];
    private static JLabel etiqueta;
    private static JLabel etiqueta2;
    private static JLabel etiqueta3;
    private static JLabel etiqueta4;
    private static Timer temporizador;
    private static ManejaEjemploTimer manejador;
    private int vidas = 2;
    private int totalEventos;
    private final Menú menu;
    
    
    boolean derecha = true;
    boolean izquierda = false;
    boolean abajo = true;
    boolean arriba = false;

    //starts the thread
    public void start() {
        game.resume();
        isPaused.set(false);
    }

    //stops the thread
    public void stop() {
        game.suspend();
    }

    //ends the thread
    public void destroy() {
        game.resume();
        isPaused.set(false);
        game.stop();
        isPaused.set(true);
    }

    public void CrearBloques() {
        int X;
        int Y = 25;
        for (int i = 0; i < CANTIDAD_LADRILLOS_Y; i++) {
            Y = Y + 28;
            X = 50;
            for (int j = 0; j < CANTIDAD_LADRILLOS_X; j++) {
                bloques[i][j] = new Bloques(X, Y, ANCHO_LADRILLO, ALTO_LADRILLO);
                X = X + 98;
            }
        }
    }

    public void recolocar() {

        if (vidas != 0) {
            JOptionPane.showMessageDialog(null, "¿Listo?");
            bola.setY(600);
            bola.setX(ANCHO_PANTALLA / 2);
            plataforma.setY(650);
            plataforma.setX(240);
            bola.setDirecciones(1, -1);
            arriba = true;
            derecha = true;
            izquierda = false;
            abajo = false;
            vidas--;

        } else {

            JOptionPane.showMessageDialog(this, "Perdiste.");
            int opcion = JOptionPane.showConfirmDialog(this, "¿Desea iniciar un nuevo juego?");
            if (opcion == JOptionPane.YES_NO_OPTION) {

                CrearBloques();
                vidas = 3;
                run();
            } else {
                System.exit(0);
            }
        }
    }

    public void moverBloques() {

        for (int i = 0; i < CANTIDAD_LADRILLOS_Y; i++) {
            for (int j = 0; j < CANTIDAD_LADRILLOS_X; j++) {
                bloques[i][j].setY(bloques[i][j].getY() + 10);
            }
        }
    }
//Esta función se puede optimizar preguntando los lados que son tocados y de ahí determinar la dirección por los booleanos

    public void verificarBloque() {

        for (int i = 0; i < CANTIDAD_LADRILLOS_Y; i++) {
            for (int j = 0; j < CANTIDAD_LADRILLOS_X; j++) {
                if (bloques[i][j].getDureza() == 0) {

                } else if (abajo && izquierda) {                   //Aquí había un 60
                    //if((bola.getY()+RADIO_BOLA == bloques[i][j].getY()) && (bola.getX() <= bloques[i][j].getX()+ANCHO_LADRILLO) && (bola.getX()+RADIO_BOLA > bloques[i][j].getX()))
                    //if (abajo && izquierda){
                    //arriba=true;izquierda=true;derecha=false;abajo=false;
                    //bloques[i][j].golpe();
                    //bola.setDirecciones(-1,-1); 
                    //else if (abajo && derecha){
                    //arriba=true;izquierda=false;derecha=true;abajo=false;
                    //bloques[i][j].golpe();
                    //bola.setDirecciones(1,-1);
                    //}

                    if ((bola.getY() + RADIO_BOLA == bloques[i][j].getY()) && (bola.getX() <= bloques[i][j].getX() + ANCHO_LADRILLO) && (bola.getX() + RADIO_BOLA > bloques[i][j].getX())) {
                        arriba = true;
                        izquierda = true;
                        derecha = false;
                        abajo = false;
                        bloques[i][j].golpe();
                        bola.setDirecciones(-1, -1);
                    } else if (((bola.getX() == bloques[i][j].getX() + ANCHO_LADRILLO) || (bola.getX() == bloques[i][j].getX() + ANCHO_LADRILLO + 1)) && (bola.getY() <= bloques[i][j].getY() + ALTO_LADRILLO) && (bola.getY() + RADIO_BOLA > bloques[i][j].getY())) {
                        abajo = true;
                        derecha = true;
                        arriba = false;
                        izquierda = false;
                        bloques[i][j].golpe();
                        bola.setDirecciones(1, 1);
                    }
                } //Segunda parte 
                else if (abajo && derecha) {                   //Aquí había un 60
                    if ((bola.getY() + RADIO_BOLA == bloques[i][j].getY()) && (bola.getX() <= bloques[i][j].getX() + ANCHO_LADRILLO) && (bola.getX() + RADIO_BOLA > bloques[i][j].getX())) {
                        arriba = true;
                        izquierda = false;
                        derecha = true;
                        abajo = false;
                        bloques[i][j].golpe();
                        bola.setDirecciones(1, -1);
                    } else if (((bola.getX() + RADIO_BOLA == bloques[i][j].getX()) || (bola.getX() + RADIO_BOLA - 1 == bloques[i][j].getX())) && (bola.getY() <= bloques[i][j].getY() + ALTO_LADRILLO) && (bola.getY() + RADIO_BOLA > bloques[i][j].getY())) {
                        abajo = true;
                        derecha = false;
                        arriba = false;
                        izquierda = true;
                        bloques[i][j].golpe();
                        bola.setDirecciones(-1, 1);
                    }
                } // Tercera parte
                else if (arriba && derecha) {
                    if (((bola.getY() == bloques[i][j].getY() + ALTO_LADRILLO) || (bola.getY() == bloques[i][j].getY() + ALTO_LADRILLO - 1)) && (bola.getX() <= bloques[i][j].getX() + ANCHO_LADRILLO) && (bola.getX() + RADIO_BOLA > bloques[i][j].getX())) {

                        arriba = false;
                        izquierda = false;
                        derecha = true;
                        abajo = true;
                        bloques[i][j].golpe();
                        bola.setDirecciones(1, 1);
                    } //No modificar hitbox   
                    else if (((bola.getX() + RADIO_BOLA == bloques[i][j].getX()) || (bola.getX() + RADIO_BOLA - 1 == bloques[i][j].getX())) && (bola.getY() <= bloques[i][j].getY() + ALTO_LADRILLO) && (bola.getY() + RADIO_BOLA > bloques[i][j].getY())) {
                        abajo = false;
                        derecha = false;
                        arriba = true;
                        izquierda = true;
                        bloques[i][j].golpe();
                        bola.setDirecciones(-1, -1);

                    }
                } // CUarta parte
                else if (arriba && izquierda) {
                    if ((bola.getY() == bloques[i][j].getY() + ALTO_LADRILLO || bola.getY() == bloques[i][j].getY() + ALTO_LADRILLO - 1) && (bola.getX() <= bloques[i][j].getX() + ANCHO_LADRILLO) && (bola.getX() + RADIO_BOLA > bloques[i][j].getX())) {
                        arriba = false;
                        izquierda = true;
                        derecha = false;
                        abajo = true;
                        bloques[i][j].golpe();
                        bola.setDirecciones(-1, 1);
                    } else if (((bola.getX() == bloques[i][j].getX() + ANCHO_LADRILLO) || (bola.getX() == bloques[i][j].getX() + ANCHO_LADRILLO - 1)) && (bola.getY() <= bloques[i][j].getY() + ALTO_LADRILLO) && (bola.getY() + RADIO_BOLA > bloques[i][j].getY())) {
                        abajo = false;
                        derecha = true;
                        arriba = true;
                        izquierda = false;
                        bloques[i][j].golpe();
                        bola.setDirecciones(1, -1);

                    }

                }
            }
        }
    }

    public void verificarPlataforma() {

        if ((bola.getY() == ALTO_PANTALLA)) {
            recolocar();
        } //0% & 20%
        else if ((bola.getY() + RADIO_BOLA == plataforma.getY()) && (bola.getX() + RADIO_BOLA < plataforma.getX() + ANCHO_PLATAFORMA / 6) && (bola.getX() + RADIO_BOLA >= plataforma.getX())) {
            izquierda = true;
            derecha = false;
            arriba = true;
            abajo = false;
            bola.setDirecciones(-2, -1);
        } //20% & 40%
        else if ((bola.getY() + RADIO_BOLA == plataforma.getY()) && (bola.getX() + RADIO_BOLA < plataforma.getX() + ANCHO_PLATAFORMA / 3) && (bola.getX() + RADIO_BOLA >= plataforma.getX() + ANCHO_PLATAFORMA / 6)) {
            izquierda = true;
            derecha = false;
            arriba = true;
            abajo = false;
            bola.setDirecciones(-1, -1);
        } //40% & 60%
        else if ((bola.getY() + RADIO_BOLA == plataforma.getY()) && (bola.getX() + RADIO_BOLA < plataforma.getX() + ANCHO_PLATAFORMA / 2) && (bola.getX() + RADIO_BOLA >= plataforma.getX() + ANCHO_PLATAFORMA / 3)) {
            izquierda = true;
            derecha = false;
            arriba = true;
            abajo = false;
            bola.setDirecciones(-1, -2);
        } // 60% & 80%
        else if ((bola.getY() + RADIO_BOLA == plataforma.getY()) && (bola.getX() + RADIO_BOLA < plataforma.getX() + ANCHO_PLATAFORMA * 2 / 3) && (bola.getX() + RADIO_BOLA >= plataforma.getX() + ANCHO_PLATAFORMA / 2)) {
            izquierda = false;
            derecha = true;
            arriba = true;
            abajo = false;
            bola.setDirecciones(0, -2);
        }// 80% & 100%
        else if ((bola.getY() + RADIO_BOLA == plataforma.getY()) && (bola.getX() + RADIO_BOLA < plataforma.getX() + ANCHO_PLATAFORMA * 5 / 6) && (bola.getX() + RADIO_BOLA >= plataforma.getX() + ANCHO_PLATAFORMA * 2 / 3)) {
            izquierda = false;
            derecha = true;
            arriba = true;
            abajo = false;
            bola.setDirecciones(1, -2);
        } else if ((bola.getY() + RADIO_BOLA == plataforma.getY()) && (bola.getX() + RADIO_BOLA < plataforma.getX() + ANCHO_PLATAFORMA) && (bola.getX() + RADIO_BOLA >= plataforma.getX() + ANCHO_PLATAFORMA * 5 / 6)) {
            izquierda = false;
            derecha = true;
            arriba = true;
            abajo = false;
            bola.setDirecciones(1, -1);
        } else if ((bola.getY() + RADIO_BOLA == plataforma.getY()) && (bola.getX() + RADIO_BOLA < plataforma.getX() + ANCHO_PLATAFORMA * 7 / 6) && (bola.getX() + RADIO_BOLA >= plataforma.getX() + ANCHO_PLATAFORMA)) {
            izquierda = false;
            derecha = true;
            arriba = true;
            abajo = false;
            bola.setDirecciones(2, -1);
        } else if (derecha && abajo) {
            if (bola.getX() + RADIO_BOLA >= ANCHO_PANTALLA) {
                izquierda = true;
                derecha = false;
                abajo = true;
                arriba = false;
                bola.setDirecciones(-1, 1);

            }
        } else if (derecha && arriba) {
            if (bola.getY() <= 0) {
                arriba = false;
                abajo = true;
                derecha = true;
                izquierda = false;
                bola.setDirecciones(1, 1);
            } else if (bola.getX() + RADIO_BOLA >= ANCHO_PANTALLA) {
                arriba = true;
                izquierda = true;
                derecha = false;
                abajo = false;
                bola.setDirecciones(-1, -1);
            }
        } else if (izquierda && arriba) {
            if (bola.getY() <= 0) {
                izquierda = true;
                derecha = false;
                abajo = true;
                arriba = false;
                bola.setDirecciones(-1, 1);
            } else if (bola.getX() <= 0) {
                derecha = true;
                izquierda = false;
                arriba = true;
                abajo = false;
                bola.setDirecciones(1, -1);
            }
        } else if (izquierda && abajo) {
            if (bola.getX() <= 0) {
                derecha = true;
                izquierda = false;
                arriba = false;
                abajo = true;
                bola.setDirecciones(1, 1);
            }

        }
    }

    //Constructor
    public Tablero(int ancho, int alto) {

        menu = new Menú();

        JOptionPane.showMessageDialog(null, "¡Bienvenido!\n\nInformacion:\nCada bloque te concederá 100 puntos.\n", "Bricker Breaker", 1);
        menu.setNombre(JOptionPane.showInputDialog(null, "Por favor digite su nombre: "));

        super.setSize(ancho, alto);
        super.setLayout(null);
        addMouseMotionListener(new EscuchaMouse());
        this.addKeyListener(new BoardListener());
        setFocusable(true);
        CrearBloques();
        etiqueta = new JLabel("Tiempo: 0");
        etiqueta.setBounds(1072, 80, 1172, 90);
        etiqueta2 = new JLabel("Jugador: ");
        etiqueta2.setBounds(1072, 50, 1162, 60);
        etiqueta3 = new JLabel(menu.getNombre());
        etiqueta3.setBounds(1142, 50, 1300, 60);
        etiqueta4 = new JLabel("Puntuacion: 0");
        etiqueta4.setBounds(1072, 100, 1182, 120);
        manejador = new ManejaEjemploTimer();
        temporizador = new Timer(1000, manejador);
        super.add(etiqueta);
        super.add(etiqueta2);
        super.add(etiqueta3);
        super.add(etiqueta4);
        temporizador.start();
        plataforma = new Plataforma(240, 650, ANCHO_PLATAFORMA, ALTO_PLATAFORMA);
        bola = new Bola(290, 650 - RADIO_BOLA, RADIO_BOLA, RADIO_BOLA);

        game = new Thread(this);
        game.start();

    }

    @Override //Pintor
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(FONDO, 0, 0, ANCHO_PANTALLA, ALTO_PANTALLA, null);
        bola.draw(g);
        g.drawImage(INFORMACION, 1063, 0, 1360, 700, null);
        g.drawLine(1062, 0, 1062, 700);
        g.drawLine(1062, 0, 1356, 0);
        g.drawLine(1356, 0, 1356, 700);
        g.drawLine(1062, 700, 1356, 700);
        g.drawLine(1062, 350, 1356, 350);
       
        Graphics2D g2 = (Graphics2D) g;

        // Define rendering hint, font name, font style and font size
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("Segoe Script", Font.BOLD + Font.ITALIC, 40));
        g2.setPaint(Color.ORANGE);

        // Draw String
        g2.drawString("LOLSITO", 1105, 50);
        // Define rendering hint, font name, font style and font size
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("Segoe Script", Font.BOLD + Font.ITALIC, 20));
        g2.setPaint(Color.ORANGE);

        // Draw String
        g2.drawString("MEJORES DO MUNDO", 1062, 380);
        
        plataforma.draw(g);
        for (int i = 0; i < CANTIDAD_LADRILLOS_Y; i++) {
            for (int j = 0; j < CANTIDAD_LADRILLOS_X; j++) {
                if (bloques[i][j].getDureza() == 0) {
                    
                } else {
                    
                    bloques[i][j].draw(g);
                }
            }
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < CANTIDAD_LADRILLOS_Y; i++) {
            for (int j = 0; j < CANTIDAD_LADRILLOS_X; j++) {
                if (bloques[i][j].getDureza() == 0) {
                    menu.sumarPuntuacion();
                    etiqueta4.setText("Puntuacion: "+Integer.toString(menu.getPuntuacion()));
                }}}
        while (true) {
            if (bola.contador == 2000) {
                moverBloques();
                bola.contador = 0;
            }
            verificarBloque();
            verificarPlataforma();
            bola.mover();
            repaint();
        }

    }

    private class EscuchaMouse implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
            
        public void mouseMoved(MouseEvent e) {
            if (e.getX() < ANCHO_PANTALLA-(ANCHO_PLATAFORMA/2)) {
                plataforma.setX(e.getX() - ANCHO_PLATAFORMA / 2);
            }
            //   bola.setX(e.getX());
            // bola.setY(e.getY());     
        }
    }

    private class BoardListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent ke) {

            switch (ke.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    stop();
                    isPaused.set(true);
                    break;
                case KeyEvent.VK_SPACE:

                    start();
                    break;
            }
        }
    }
    
    private class ManejaEjemploTimer implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent eventoAccion )
	   {
	    totalEventos++;
	    etiqueta.setText("Tiempo: " + totalEventos);
	   	      
	   }
    }
}
