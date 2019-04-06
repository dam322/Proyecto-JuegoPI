/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bricker.breaker;

import java.util.ArrayList;
import java.util.Collections;

public class Scores  {

    public static final int MAX_SCORES = 5;
    private ArrayList<Jugador> scoresList = new ArrayList();

    public ArrayList<Jugador> getScoresList() {
        return scoresList;
    }

    public void setScoresList(ArrayList<Jugador> scoresList) {
        this.scoresList = scoresList;
    }

    public void addScore(Jugador score) {
        scoresList.add(score);
        Collections.sort(scoresList);
        if(scoresList.size() > MAX_SCORES) {
            scoresList.remove(scoresList.size() - 1);
        }
    }

    public int getPosition(Jugador score) {
        return scoresList.indexOf(score);
    }
    
    @Override
    public String toString() {
        String result = "";
        for(int i=0; i<scoresList.size(); i++) {
            Jugador score = scoresList.get(i);
            result += (i+1) + "º: " + score.getNombre() + ": " + score.getPuntaje() + "\n";
        }
        return result;
    }
       
}