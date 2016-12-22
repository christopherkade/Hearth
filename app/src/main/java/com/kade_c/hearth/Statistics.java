package com.kade_c.hearth;

import java.util.HashMap;

/**
 * Our statistics calculator and saving class.
 */
public class Statistics {

    private int gamesPlayed = 0;
    private int gamesWon = 0;
    private int gamesLost = 0;
    private Float percentageWin = 0f;
    private int activeDecksNumber = 0;

    private HashMap<String, Integer> winPerClass = new HashMap<>();
    private HashMap<String, Integer> lossPerClass = new HashMap<>();

    private String favoriteDeck = "None";
    private String mostSuccessfulDeck = "None";

    /**
     * Calculates the victory percentage with the parameters.
     * @param totalGames total games won
     * @param totalVictories total victories
     */
    public Float calculateVictoryPercentage(int totalGames, int totalVictories) {
        Float victoryPercentage = 0f;

        if (totalGames > 0) {
            victoryPercentage = (float) ((totalVictories * 100) / totalGames);

            percentageWin = victoryPercentage;
            return victoryPercentage;
        }
        percentageWin = victoryPercentage;
        return victoryPercentage;
    }

    /**
     * Adds a victory against the class passed as parameter.
     * @param className
     */
    public void setClassSpecificVictory(String className) {
        if (winPerClass.containsKey(className)) {
            int value = winPerClass.get(className);
            value++;
            winPerClass.put(className, value);
        } else {
            winPerClass.put(className, 1);
        }
    }

    /**
     * Adds a defeat against the class passed as parameter.
     * @param className
     */
    public void setClassSpecificDefeat(String className) {
        if (lossPerClass.containsKey(className)) {
            int value = lossPerClass.get(className);
            value++;
            lossPerClass.put(className, value);
        } else {
            lossPerClass.put(className, 1);
        }
    }


    // SETTERS
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    public void setActiveDecksNumber(int activeDecksNumber) {
        this.activeDecksNumber = activeDecksNumber;
    }

    public void setFavoriteDeck(String favoriteDeck) {
        this.favoriteDeck = favoriteDeck;
    }

    public void setMostSuccessfulDeck(String mostSuccessfulDeck) {
        this.mostSuccessfulDeck = mostSuccessfulDeck;
    }

    // GETTERS
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public Float getPercentageWin() {
        return percentageWin;
    }

    public int getActiveDecksNumber() {
        return activeDecksNumber;
    }

    public String getFavoriteDeck() {
        return favoriteDeck;
    }

    public String getMostSuccessfulDeck() {
        return mostSuccessfulDeck;
    }
}

