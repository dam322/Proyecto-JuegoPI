/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bricker.breaker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoreFile {

    private final File highScoreFile;

    public ScoreFile() {
        highScoreFile = new File("highscores.dat");
    }

    public void load(Scores scores) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(highScoreFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            scores.setScoresList((ArrayList<Jugador>)ois.readObject());
        } catch (FileNotFoundException ex) {
            // No existe el fichero. Se creará posteriormente al guardar
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ScoreFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(fis != null) {
                   fis.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ScoreFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void save(Scores scores) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(highScoreFile);
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject((ArrayList<Jugador>)scores.getScoresList());
            }
        } catch (IOException ex) {
            Logger.getLogger(ScoreFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(fos != null) {
                   fos.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ScoreFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
