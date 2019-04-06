/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bricker.breaker;

import javafx.scene.control.TextArea;

/**
 *
 * @author dasd-
 */
public class HighScores {
    
    
    TextArea textAreaResults = new TextArea();
    Tablero tab;
    public void puntajes(String playerName, int value){
        // Creación de objetos para almacenar máximas puntuaciones
        Scores scores = new Scores();
        ScoreFile scoresFile = new ScoreFile();
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
            textAreaResults.setText(scores.toString());
            // Almacenar la lista de máximas puntuaciones
            scoresFile.save(scores);
        
    }
    
    
}
