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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javazoom.jl.decoder.JavaLayerException;

public final class Tablero extends JPanel implements Constantes, Runnable {

    private final AtomicBoolean isPaused = new AtomicBoolean(true);
    private final ArrayList<Item> items = new ArrayList<Item>();
    private int vidas = 2;
    private int totalEventos;
    int cosa = 1;
    int velocidad = 3;
    private int tempoDisparo;
    private final ArrayList<Disparos> disparos = new ArrayList<Disparos>();
    private final Thread game;
    private final Plataforma plataforma;
    private final Bola bola;
    private final Bloques[][] bloques = new Bloques[CANTIDAD_LADRILLOS_Y][CANTIDAD_LADRILLOS_X];
    private static JLabel tiempo;
    private static JLabel nombreJugador;
    private static JLabel nombre2;
    private static JLabel puntuacion;
    private static JLabel vida;
    private static JTextArea textAreaResults;
    private static Timer temporizador;
    Scores scores = new Scores();
    ScoreFile scoresFile = new ScoreFile();
    private static ManejaEjemploTimer manejador;

    public final Menú menu;
    private Sonidos sound;

    public boolean magnetic = true;
    public boolean shoting = false;
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
                Random rand = new Random();
                int itemType = rand.nextInt(11);
                bloques[i][j] = new Bloques(X, Y, ANCHO_LADRILLO, ALTO_LADRILLO, itemType);
                X = X + 98;
            }
        }
    }

    public void addItem(Item i) {
        items.add(i);
    }

    public void dropItems() {
        for (int i = 0; i < items.size(); i++) {
            Item tempItem = items.get(i);
            tempItem.drop();
            items.set(i, tempItem);
        }
    }

    public void checkItemList() {
        for (int i = 0; i < items.size(); i++) {
            Item tempItem = items.get(i);
            if (plataforma.caughtItem(tempItem)) {
                if (tempItem.getType() == 3) {
                    shoting = true;

                } else if (tempItem.getType() == 2) {
                    magnetic = true;
                }
                items.remove(i);
            } else if (tempItem.getY() > ALTO_PANTALLA) {
                items.remove(i);
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
            vida.setText("Vidas: " + vidas);

        } else {
            temporizador.stop();
            JOptionPane.showMessageDialog(this, "Perdiste.");
            int opcion = JOptionPane.showConfirmDialog(this, "¿Desea iniciar un nuevo juego?");

            if (opcion == JOptionPane.YES_NO_OPTION) {
                highScore(menu.getNombre(), menu.getPuntuacion());
                menu.setPuntuacion(0);
                puntuacion.setText("Puntuacion: 0");
                tiempo.setText("Tiempo : 0");
                CrearBloques();
                vidas = 3;
                totalEventos = 0;
                temporizador.restart();
                run();
            } else {
                highScore(menu.getNombre(), menu.getPuntuacion());
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
                    if ((bola.getY() + RADIO_BOLA == bloques[i][j].getY()) && (bola.getX() <= bloques[i][j].getX() + ANCHO_LADRILLO) && (bola.getX() + RADIO_BOLA > bloques[i][j].getX())) {
                        arriba = true;
                        izquierda = true;
                        derecha = false;
                        abajo = false;
                        bloques[i][j].golpe();

                        if (bloques[i][j].getDureza() == 0) {
                            addItem(bloques[i][j].item);
                            menu.sumarPuntuacion();
                            puntuacion.setText("Puntuacion: " + Integer.toString(menu.getPuntuacion()));
                        }
                        bola.setDirecciones(-1, -1);
                    } else if (((bola.getX() == bloques[i][j].getX() + ANCHO_LADRILLO) || (bola.getX() == bloques[i][j].getX() + ANCHO_LADRILLO + 1)) && (bola.getY() <= bloques[i][j].getY() + ALTO_LADRILLO) && (bola.getY() + RADIO_BOLA > bloques[i][j].getY())) {
                        abajo = true;
                        derecha = true;
                        arriba = false;
                        izquierda = false;
                        bloques[i][j].golpe();

                        if (bloques[i][j].getDureza() == 0) {
                            addItem(bloques[i][j].item);
                            menu.sumarPuntuacion();
                            puntuacion.setText("Puntuacion: " + Integer.toString(menu.getPuntuacion()));
                        }
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

                        if (bloques[i][j].getDureza() == 0) {
                            addItem(bloques[i][j].item);
                            menu.sumarPuntuacion();
                            puntuacion.setText("Puntuacion: " + Integer.toString(menu.getPuntuacion()));
                        }
                        bola.setDirecciones(1, -1);
                    } else if (((bola.getX() + RADIO_BOLA == bloques[i][j].getX()) || (bola.getX() + RADIO_BOLA - 1 == bloques[i][j].getX())) && (bola.getY() <= bloques[i][j].getY() + ALTO_LADRILLO) && (bola.getY() + RADIO_BOLA > bloques[i][j].getY())) {
                        abajo = true;
                        derecha = false;
                        arriba = false;
                        izquierda = true;
                        bloques[i][j].golpe();

                        if (bloques[i][j].getDureza() == 0) {
                            addItem(bloques[i][j].item);
                            menu.sumarPuntuacion();
                            puntuacion.setText("Puntuacion: " + Integer.toString(menu.getPuntuacion()));
                        }
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

                        if (bloques[i][j].getDureza() == 0) {
                            addItem(bloques[i][j].item);
                            menu.sumarPuntuacion();
                            puntuacion.setText("Puntuacion: " + Integer.toString(menu.getPuntuacion()));
                        }
                        bola.setDirecciones(1, 1);
                    } //No modificar hitbox   
                    else if (((bola.getX() + RADIO_BOLA == bloques[i][j].getX()) || (bola.getX() + RADIO_BOLA - 1 == bloques[i][j].getX())) && (bola.getY() <= bloques[i][j].getY() + ALTO_LADRILLO) && (bola.getY() + RADIO_BOLA > bloques[i][j].getY())) {
                        abajo = false;
                        derecha = false;
                        arriba = true;
                        izquierda = true;
                        bloques[i][j].golpe();

                        if (bloques[i][j].getDureza() == 0) {
                            addItem(bloques[i][j].item);
                            menu.sumarPuntuacion();
                            puntuacion.setText("Puntuacion: " + Integer.toString(menu.getPuntuacion()));
                        }
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

                        if (bloques[i][j].getDureza() == 0) {
                            addItem(bloques[i][j].item);
                            menu.sumarPuntuacion();
                            puntuacion.setText("Puntuacion: " + Integer.toString(menu.getPuntuacion()));
                        }
                        bola.setDirecciones(-1, 1);
                    } else if (((bola.getX() == bloques[i][j].getX() + ANCHO_LADRILLO) || (bola.getX() == bloques[i][j].getX() + ANCHO_LADRILLO - 1)) && (bola.getY() <= bloques[i][j].getY() + ALTO_LADRILLO) && (bola.getY() + RADIO_BOLA > bloques[i][j].getY())) {
                        abajo = false;
                        derecha = true;
                        arriba = true;
                        izquierda = false;
                        bloques[i][j].golpe();

                        if (bloques[i][j].getDureza() == 0) {
                            addItem(bloques[i][j].item);
                            menu.sumarPuntuacion();
                            puntuacion.setText("Puntuacion: " + Integer.toString(menu.getPuntuacion()));
                        }
                        bola.setDirecciones(1, -1);

                    }

                }
            }
        }
    }

    public void verificarPlataforma() throws FileNotFoundException, JavaLayerException {

        if ((bola.getY() == ALTO_PANTALLA)) {
            recolocar();
        } //0% & 20%
        else if ((bola.getY() + RADIO_BOLA == plataforma.getY()) && (bola.getX() + RADIO_BOLA < plataforma.getX() + ANCHO_PLATAFORMA / 6) && (bola.getX() + RADIO_BOLA >= plataforma.getX())) {
            izquierda = true;
            derecha = false;
            arriba = true;
            abajo = false;
            bola.setDirecciones(-2, -1);
            sound = new Sonidos("hacha");
        } //20% & 40%
        else if ((bola.getY() + RADIO_BOLA == plataforma.getY()) && (bola.getX() + RADIO_BOLA < plataforma.getX() + ANCHO_PLATAFORMA / 3) && (bola.getX() + RADIO_BOLA >= plataforma.getX() + ANCHO_PLATAFORMA / 6)) {
            izquierda = true;
            derecha = false;
            arriba = true;
            abajo = false;
            bola.setDirecciones(-1, -1);
            sound = new Sonidos("hacha");
        } //40% & 60%
        else if ((bola.getY() + RADIO_BOLA == plataforma.getY()) && (bola.getX() + RADIO_BOLA < plataforma.getX() + ANCHO_PLATAFORMA / 2) && (bola.getX() + RADIO_BOLA >= plataforma.getX() + ANCHO_PLATAFORMA / 3)) {
            izquierda = true;
            derecha = false;
            arriba = true;
            abajo = false;
            bola.setDirecciones(-1, -2);
            sound = new Sonidos("hacha");
        } // 60% & 80%
        else if ((bola.getY() + RADIO_BOLA == plataforma.getY()) && (bola.getX() + RADIO_BOLA < plataforma.getX() + ANCHO_PLATAFORMA * 2 / 3) && (bola.getX() + RADIO_BOLA >= plataforma.getX() + ANCHO_PLATAFORMA / 2)) {
            izquierda = false;
            derecha = true;
            arriba = true;
            abajo = false;
            bola.setDirecciones(0, -2);
            sound = new Sonidos("hacha");
        }// 80% & 100%
        else if ((bola.getY() + RADIO_BOLA == plataforma.getY()) && (bola.getX() + RADIO_BOLA < plataforma.getX() + ANCHO_PLATAFORMA * 5 / 6) && (bola.getX() + RADIO_BOLA >= plataforma.getX() + ANCHO_PLATAFORMA * 2 / 3)) {
            izquierda = false;
            derecha = true;
            arriba = true;
            abajo = false;
            bola.setDirecciones(1, -2);
            sound = new Sonidos("hacha");
        } else if ((bola.getY() + RADIO_BOLA == plataforma.getY()) && (bola.getX() + RADIO_BOLA < plataforma.getX() + ANCHO_PLATAFORMA) && (bola.getX() + RADIO_BOLA >= plataforma.getX() + ANCHO_PLATAFORMA * 5 / 6)) {
            izquierda = false;
            derecha = true;
            arriba = true;
            abajo = false;
            bola.setDirecciones(1, -1);
            sound = new Sonidos("hacha");
        } else if ((bola.getY() + RADIO_BOLA == plataforma.getY()) && (bola.getX() + RADIO_BOLA < plataforma.getX() + ANCHO_PLATAFORMA * 7 / 6) && (bola.getX() + RADIO_BOLA >= plataforma.getX() + ANCHO_PLATAFORMA)) {
            izquierda = false;
            derecha = true;
            arriba = true;
            abajo = false;
            bola.setDirecciones(2, -1);
            sound = new Sonidos("hacha");
        } else if (derecha && abajo) {
            if (bola.getX() + RADIO_BOLA >= ANCHO_PANTALLA) {
                izquierda = true;
                derecha = false;
                abajo = true;
                arriba = false;
                bola.setDirecciones(-1, 1);
                sound = new Sonidos("hacha");

            }
        } else if (derecha && arriba) {
            if (bola.getY() <= 0) {
                arriba = false;
                abajo = true;
                derecha = true;
                izquierda = false;
                bola.setDirecciones(1, 1);
                sound = new Sonidos("hacha");
            } else if (bola.getX() + RADIO_BOLA >= ANCHO_PANTALLA) {
                arriba = true;
                izquierda = true;
                derecha = false;
                abajo = false;
                bola.setDirecciones(-1, -1);
                sound = new Sonidos("hacha");
            }
        } else if (izquierda && arriba) {
            if (bola.getY() <= 0) {
                izquierda = true;
                derecha = false;
                abajo = true;
                arriba = false;
                bola.setDirecciones(-1, 1);
                sound = new Sonidos("hacha");
            } else if (bola.getX() <= 0) {
                derecha = true;
                izquierda = false;
                arriba = true;
                abajo = false;
                bola.setDirecciones(1, -1);
                sound = new Sonidos("hacha");
            }
        } else if (izquierda && abajo) {
            if (bola.getX() <= 0) {
                derecha = true;
                izquierda = false;
                arriba = false;
                abajo = true;
                bola.setDirecciones(1, 1);
                sound = new Sonidos("hacha");
            }

        }
    }

    //Constructor
    public Tablero(int ancho, int alto) {

        menu = new Menú();

        JOptionPane.showMessageDialog(null, "¡Bienvenido a Destructor!\n", "Destructor", 1);
        JOptionPane.showMessageDialog(null, "Instrucciones:\n- Inicias el movimiento de la bola con un click derecho y puedes mover la barra solo con el mouse\n"
                + "- Cada bloque que destruyas te dara 100 puntos\n- Cada nivel tiene un tiempo maximo de 300 segundos\n"
                + "- Puedes elegir entre jugar los niveles pre-establecidos o elegir un nivel personalizado(es un archivo de "
                + "texto que se encuentraen la carpeta base de este proyecto)\n"
                + "- Puedes pausar el juego con la tecla 'ESC' y quitar la puasa con la barra espaciadora\n"
                + "- Se tienen dos poderes: un caño que dispara multiples misiles por unos segundos al presionar la tecla 'S' y un iman que pega la bola a la barra para que puedas apuntar donde quieras", "Destructor", 1);
        menu.setNombre(JOptionPane.showInputDialog(null, "Por favor digite su nombre: ", "Destructor", 1));
        
        super.setSize(ancho, alto);
        super.setLayout(null);
        addMouseListener(new EscuchaMouse());
        addMouseMotionListener(new EscuchaMouse());
        this.addKeyListener(new BoardListener());
        setFocusable(true);

        int op = JOptionPane.showConfirmDialog(null, "¿Desea cargar un nivel personalizado?");
        if (op == JOptionPane.YES_NO_OPTION) {
            Personalizable();

        } else{
            if(op == JOptionPane.NO_OPTION){
                CrearBloques();
            }else{
                System.exit(0);
            }
            
        }

        tiempo = new JLabel("Tiempo: 0");
        tiempo.setBounds(1072, 80, 1172, 90);
        tiempo.setFont(new Font("OCR A Std", Font.PLAIN, 14));
        nombreJugador = new JLabel("Jugador: ");
        nombreJugador.setBounds(1072, 50, 1162, 60);
        nombreJugador.setFont(new Font("OCR A Std", Font.PLAIN, 14));
        nombre2 = new JLabel(" " + menu.getNombre());
        nombre2.setBounds(1142, 50, 1300, 60);
        nombre2.setFont(new Font("OCR A Std", Font.PLAIN, 14));
        puntuacion = new JLabel("Puntuacion: 0");
        puntuacion.setBounds(1072, 110, 1182, 120);
        puntuacion.setFont(new Font("OCR A Std", Font.PLAIN, 14));
        vida = new JLabel("Vidas: 2");
        vida.setBounds(1072, 150, 1182, 160);
        vida.setFont(new Font("OCR A Std", Font.PLAIN, 14));
        textAreaResults = new JTextArea();
        textAreaResults.setBounds(1062, 420, 1355, 500);
        textAreaResults.setEditable(false);
        scoresFile.load(scores);
        textAreaResults.setText(scores.toString());
        textAreaResults.setFont(new Font("OCR A Std", Font.PLAIN, 14));
        manejador = new ManejaEjemploTimer();
        temporizador = new Timer(1000, manejador);
        textAreaResults.setFont(new Font("OCR A Std", Font.PLAIN, 14));
        textAreaResults.setOpaque(false);
        textAreaResults.setForeground(Color.BLACK);
        super.add(tiempo);
        super.add(nombreJugador);
        super.add(nombre2);
        super.add(puntuacion);
        super.add(vida);
        super.add(textAreaResults);

        temporizador.start();
        plataforma = new Plataforma(240, 650, ANCHO_PLATAFORMA, ALTO_PLATAFORMA);
        bola = new Bola(290, 650 - RADIO_BOLA, RADIO_BOLA, RADIO_BOLA);

        game = new Thread(this);
        game.start();

    }

    public void highScore(String playerName, int value) {
        // Creación de objetos para almacenar máximas puntuaciones

        // Cargar la lista inicial de máximas puntuaciones
        scoresFile.load(scores);
        // Mostrar la lista inicial de máximas puntuaciones
        textAreaResults.setText(scores.toString());
        

        // Recoger datos de nueva puntuación desde la ventana
        // Crear una nueva puntuación
        Jugador score = new Jugador(playerName, value);
        // Añadirla a la lista de puntuaciones
        scores.addScore(score);

        // Mostrar la lista de máximas puntuaciones
        textAreaResults.setText(scores.toString() + "\n");
        
        // Almacenar la lista de máximas puntuaciones
        scoresFile.save(scores);
    }

    @Override //Pintor
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        switch (menu.getNivel()) {
            case 1:
                g.drawImage(FONDO1, 0, 0, ANCHO_PANTALLA, ALTO_PANTALLA, null);
                g.drawImage(INFORMACION1, 1063, 0, 1360, 700, null);
                // Define rendering hint, font name, font style and font size
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(new Font("OCR A Std", Font.BOLD, 40));
                g2.setPaint(Color.BLACK);

                // Draw String
                g2.drawString("DESTRUCTOR", 1065, 50);
                // Define rendering hint, font name, font style and font size
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(new Font("OCR A Std", Font.BOLD, 20));
                g2.setPaint(Color.BLACK);

                // Draw String
                g2.drawString("HIGHSCORES", 1062, 380);
                break;
            case 2:
                g.drawImage(FONDO2, 0, 0, ANCHO_PANTALLA, ALTO_PANTALLA, null);
                g.drawImage(INFORMACION2, 1063, 0, 1360, 700, null);
                
                tiempo.setForeground(Color.WHITE);
                nombreJugador.setForeground(Color.WHITE);
                nombre2.setForeground(Color.white);
                puntuacion.setForeground(Color.WHITE);
                vida.setForeground(Color.WHITE);
                textAreaResults.setForeground(Color.WHITE);

                // Define rendering hint, font name, font style and font size
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(new Font("OCR A Std", Font.BOLD, 40));
                g2.setPaint(Color.ORANGE);

                // Draw String
                g2.drawString("DESTRUCTOR", 1065, 50);
                // Define rendering hint, font name, font style and font size
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(new Font("OCR A Std", Font.BOLD, 20));
                g2.setPaint(Color.ORANGE);

                // Draw String
                g2.drawString("HIGHSCORES", 1062, 380);
                
                break;
            default:
                g.drawImage(FONDO3, 0, 0, ANCHO_PANTALLA, ALTO_PANTALLA, null);
                g.drawImage(INFORMACION3, 1063, 0, 1360, 700, null);
                //textAreaResults.setBackground(new Color(158, 167, 227, 0));
                tiempo.setForeground(Color.WHITE);
                nombreJugador.setForeground(Color.WHITE);
                nombre2.setForeground(Color.white);
                puntuacion.setForeground(Color.WHITE);
                vida.setForeground(Color.WHITE);
                textAreaResults.setForeground(Color.WHITE);

                // Define rendering hint, font name, font style and font size
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(new Font("OCR A Std", Font.BOLD, 40));
                g2.setPaint(Color.ORANGE);

                // Draw String
                g2.drawString("DESTRUCTOR", 1065, 50);
                // Define rendering hint, font name, font style and font size
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(new Font("OCR A Std", Font.BOLD, 20));
                g2.setPaint(Color.ORANGE);

                // Draw String
                g2.drawString("HIGHSCORES", 1062, 380);
                break;
        }

        items.forEach((i) -> {
            i.draw(g);
        });
        disparos.forEach((i) -> {
            i.draw(g);
        });

        bola.draw(g);

        g.drawLine(1062, 0, 1062, 700);
        g.drawLine(1062, 0, 1356, 0);
        g.drawLine(1356, 0, 1356, 700);
        g.drawLine(1062, 700, 1356, 700);
        g.drawLine(1062, 350, 1356, 350);

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

    public void EstadoInicial() {

        if ((bola.getY() + RADIO_BOLA == plataforma.getY()) && (bola.getX() <= plataforma.getX() + ANCHO_PLATAFORMA) && (bola.getX() + RADIO_BOLA > plataforma.getX())) {

            while (magnetic == true) {

                bola.setX(plataforma.getX() + ANCHO_PLATAFORMA / 2 - 10);
                bola.setY(plataforma.getY() - RADIO_BOLA);
                repaint();

            }
        }

    }
    
    
    @Override
    public void run() {
        while (true) {

            if (IsComplete(bloques)) {
                menu.siguienteNivel();
                velocidad --;
                if (menu.getNivel() > 3) {
                    temporizador.stop();
                    JOptionPane.showMessageDialog(null, "Felicidades ha ganado los tres niveles de este juego", "Destructor", 1);
                    System.exit(0);

                }
                temporizador.stop();
                totalEventos = 0;
                tiempo.setText("Tiempo: 0");
                JOptionPane.showMessageDialog(null, "Nivel " + menu.getNivel() + ", listo?", "Destructor", 1);
                temporizador.restart();
                CrearBloques();
            }
            for (int i = 0; i < CANTIDAD_LADRILLOS_Y; i++) {
                for (int j = 0; j < CANTIDAD_LADRILLOS_X; j++) {
                    if (bloques[i][j].getDureza() <= 0) {

                    }
                }

                if (bola.contador == 2000) {
                    moverBloques();
                    bola.contador = 0;
                }

                verificarBloque();
                checkDisparos();
                try {
                    verificarPlataforma();
                } catch (FileNotFoundException | JavaLayerException ex) {
                    Logger.getLogger(Tablero.class.getName()).log(Level.SEVERE, null, ex);
                }
                dropItems();
                checkItemList();
                EstadoInicial();
                for (Disparos j : disparos) {
                    j.mover();
                }
                bola.mover(velocidad);
                repaint();
            }

        }
    }

    public void checkDisparos() {

        for (int i = 0; i < CANTIDAD_LADRILLOS_Y; i++) {

            for (int j = 0; j < CANTIDAD_LADRILLOS_X; j++) {
                for (int k = 0; k < disparos.size(); k++) {
                    Disparos disparoTemp = disparos.get(k);
                    if (bloques[i][j].caughtDisparo(disparoTemp)) {
                        if (bloques[i][j].getDureza() != 0) {
                            bloques[i][j].golpe();
                            disparos.remove(k);
                        } else
                            if(bloques[i][j].getDureza()==0){
                                menu.sumarPuntuacion();
                                puntuacion.setText("Puntuacion: "+menu.getPuntuacion());
                            }else
                            if (disparoTemp.getY() < 0) {
                            disparos.remove(k);
                        }
                    }
                }

            }
        }
    }

    private class EscuchaMouse implements MouseMotionListener, MouseListener {

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override

        public void mouseMoved(MouseEvent e) {
            if (e.getX() < ANCHO_PANTALLA - (ANCHO_PLATAFORMA / 2)) {
                plataforma.setX(e.getX() - ANCHO_PLATAFORMA / 2);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            magnetic = false;
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
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
                case KeyEvent.VK_S:
                    if (shoting) {

                        Disparos cosa = new Disparos(plataforma.getX() + (ANCHO_PLATAFORMA / 2) - 28, plataforma.getY(), 20, 20);
                        disparos.add(cosa);

                    }
                    break;
            }
        }
    }

    private class ManejaEjemploTimer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent eventoAccion) {
            totalEventos++;
            tiempo.setText("Tiempo: " + totalEventos);
            if (totalEventos == 300) {
                stop();
                temporizador.stop();
                JOptionPane.showMessageDialog(null, "Se termino el tiempo");
                int opcion = JOptionPane.showConfirmDialog(null, "¿Desea iniciar un nuevo juego?");

                if (opcion == JOptionPane.YES_NO_OPTION) {
                    highScore(menu.getNombre(), menu.getPuntuacion());
                    menu.setPuntuacion(0);
                    puntuacion.setText("Puntuacion: 0");
                    tiempo.setText("Tiempo: 0");
                    totalEventos = 0;
                    CrearBloques();
                    vidas = 3;
                    temporizador.restart();
                    start();

                } else {
                    highScore(menu.getNombre(), menu.getPuntuacion());
                    System.exit(0);
                }
            }

            if (shoting == true) {
                tempoDisparo++;
                if (tempoDisparo == 2) {
                    System.out.println(tempoDisparo);
                    shoting = false;
                    tempoDisparo=0;
                }
            }
        }
    }

    public void Personalizable() {
        String nombre;
        nombre = JOptionPane.showInputDialog(null, "Digite el nombre del archivo", "Destructor", 1) + ".txt";
        
        if (!(new File(nombre)).exists()) {
            System.out.println("No he encontrado " + nombre);
            JOptionPane.showMessageDialog(null, "No se ha encontrado el archivo, por favor creelo en la carpeta base de este proyecto o escriba bien el nombre", "Erro", JOptionPane.WARNING_MESSAGE);
            Personalizable();
        }

        System.out.println(
                "Leyendo fichero de texto...");
        try {
            try (BufferedReader ficheroEntrada = new BufferedReader(new FileReader(new File(nombre)))) {
                String linea;

                int X;
                int Y = 25;
                for (int i = 0; i < CANTIDAD_LADRILLOS_Y; i++) {
                    Y = Y + 28;
                    X = 50;
                    linea = ficheroEntrada.readLine();
                    int itemType;
                    for (int j = 0; j < CANTIDAD_LADRILLOS_X; j++) {
                        Random rand = new Random();
                        itemType = rand.nextInt(3) + 1;
                        bloques[i][j] = new Bloques(X, Y, ANCHO_LADRILLO, ALTO_LADRILLO, itemType);
                        bloques[i][j].SetDureza(linea.charAt(j) - '0');
                        X = X + 98;
                        //System.out.println( linea.charAt(j)- '0');
                    }

                }
            }
        } catch (IOException errorDeFichero) {
            System.out.println(
                    "Ha habido problemas: "
                    + errorDeFichero.getMessage());

        }
    }

    public boolean IsComplete(Bloques[][] bloques) {

        for (int i = 0; i < CANTIDAD_LADRILLOS_Y; i++) {
            for (int j = 0; j < CANTIDAD_LADRILLOS_X; j++) {
                if (bloques[i][j].getDureza() > 0 && bloques[i][j].getDureza() < 4) {

                    return false;
                }
            }

        }
        return true;
    }

}
