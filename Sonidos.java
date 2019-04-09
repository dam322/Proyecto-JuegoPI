/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bricker.breaker;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author dasd-
 */
public class Sonidos implements Runnable{

    Thread hilo;
    String tipoSonido="";
    
    public Sonidos(String tipoSonido) {
        hilo = new Thread(this);
        hilo.start();
        this.tipoSonido = tipoSonido;
        
    }
    
    @Override
    public void run() {//SE CARGA EL ARCHIVO MP3 CUANDO ARRANCA EL HILO
        
        //SE UTILIZA LA LIBRERIA Y EL METODO PLAY DEL PLAYER
        try {
            
            FileInputStream fis;
            Player player;
            fis = new FileInputStream(tipoSonido+".mp3");
            
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
            player.play();
        } catch (JavaLayerException e) {
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Sonidos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void start() {
        
        if (hilo == null) {
            hilo = new Thread(this);
            hilo.start();
        }
    }
    
    
    
}
