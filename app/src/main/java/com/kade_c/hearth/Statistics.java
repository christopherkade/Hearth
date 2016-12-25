package com.kade_c.hearth;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Our statistics calculator and saving class.
 * When instantiated, retrieves and stores general stats to be manipulated.
 */
public class Statistics {

    private ArrayList<String> deckList;

    private int totalGames = 0;
    private int totalVictories = 0;
    private int totalDefeats = 0;

    private int gamesPlayed = 0;
    private int gamesWon = 0;
    private int gamesLost = 0;

    private Float percentageWin = 0f;
    private int activeDecksNumber = 0;

    // Wins / Losses against classes.
    private HashMap<String, Integer> winAgainstClass = new HashMap<>();
    private HashMap<String, Integer> lossAgainstClass = new HashMap<>();

    // Wins / Losses per class.
    private HashMap<String, Integer> winPerClass = new HashMap<>();
    private HashMap<String, Integer> lossPerClass = new HashMap<>();

    private String favoriteDeck = "None";
    private String favoriteDeckClass = "";

    private String mostSuccessDeck = "None";
    private String mostSuccessDeckClass = "";

    private String[] classes = {"Mage", "Hunter", "Paladin",
            "Warrior", "Druid", "Warlock",
            "Shaman", "Priest", "Rogue",
            null};

    public Statistics() {}

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
     */
    private void setWinAgainstClass(String className) {
        if (winAgainstClass.containsKey(className)) {
            int value = winAgainstClass.get(className);
            value++;
            winAgainstClass.put(className, value);
        } else {
            winAgainstClass.put(className, 1);
        }
    }

    /**
     * Adds a defeat against the class passed as parameter.
     */
    private void setLossAgainstClass(String className) {
        if (lossAgainstClass.containsKey(className)) {
            int value = lossAgainstClass.get(className);
            value++;
            lossAgainstClass.put(className, value);
        } else {
            lossAgainstClass.put(className, 1);
        }
    }

    /**
     * Adds a victory for a specific class.
     */
    private void setClassSpecificVictory(String className) {
        if (winPerClass.containsKey(className)) {
            int value = winPerClass.get(className);
            value++;
            winPerClass.put(className, value);
        } else {
            winPerClass.put(className, 1);
        }
    }

    /**
     * Adds a defeat for a specific class.
     */
    private void setClassSpecificDefeat(String className) {
        if (lossPerClass.containsKey(className)) {
            int value = lossPerClass.get(className);
            value++;
            lossPerClass.put(className, value);
        } else {
            lossPerClass.put(className, 1);
        }
    }

    /**
     * Sets the players favorite deck (most played).
     */
    private void determineFavoriteDeck(String deckName, int gamesPlayed, int lastDeckGamesPlayed) {
        if (gamesPlayed > lastDeckGamesPlayed) {
            String[] favDeckSplit = deckName.split(" \\| ");
            favoriteDeckClass = favDeckSplit[0];
            favoriteDeck = favDeckSplit[1].replace("\n", "");
        }
    }

    /**
     * Sets the players most successful deck (most victories).
     */
    private void determineSuccessfulDeck(String deckName, int deckVictories, int lastDeckVictories) {
        if (deckVictories > lastDeckVictories) {
            String[] successDeckSplit = deckName.split(" \\| ");
            mostSuccessDeckClass = successDeckSplit[0];
            mostSuccessDeck = successDeckSplit[1].replace("\n", "");
        }
    }

    /**
     * Saves statistics values in our Statistics object.
     */
    private void saveStatValues() {
        this.calculateVictoryPercentage(totalGames, totalVictories);
        this.setGamesPlayed(totalGames);
        this.setGamesWon(totalVictories);
        this.setGamesLost(totalDefeats);
        this.setActiveDecksNumber(deckList.size());
        this.setFavoriteDeck(favoriteDeck);
        this.setMostSuccessfulDeck(mostSuccessDeck);
        this.setFavoriteDeckClass(favoriteDeckClass);
        this.setMostSuccessDeckClass(mostSuccessDeckClass);
    }

    /**
     * Initializes our winPerClass map.
     */
    private void initMap() {
        for (int i = 0; classes[i] != null; i++) {
            winPerClass.put(classes[i], 0);
        }
    }

    /**
     * Called when Statistics is instantiated, retrieves general statistics.
     */
    public Statistics(Context ctx, FragmentActivity fragmentActivity) {
        initMap();
        InternalFilesManager.DeckListFileManager DLFM =
                new InternalFilesManager(ctx, fragmentActivity).new DeckListFileManager();

        // Get our deck list from the file.
        deckList = DLFM.getDecksFromFile();
        InternalFilesManager.DeckFileManager DFM =
                new InternalFilesManager(ctx, fragmentActivity).new DeckFileManager();
        List<List<String>> deckStats = new ArrayList<>();

        // Get each deck's stat list one by one and save them.
        for (String deck : deckList) {
            deckStats.add(DFM.readStatisticsFile(deck));
        }

        int lastDeckGamesPlayed = 0;
        int lastDeckVictories = 0;

        if (deckStats.size() != 0) {
            // Goes through every deck.
            for (List<String> deck : deckStats) {
                if (deck.size() != 0) {
                    // Class of the current deck being analysed.
                    String deckClass = deck.get(0).split(" \\| ")[0].trim();
                    int gamesPlayed = 0;
                    int deckVictories = 0;

                    // Goes through every line of every deck stat file.
                    for (int i = 1; i < deck.size(); i++) {
                        String line = deck.get(i);
                        String opponentClass = line.split(" ")[1];

                        // Register victory or defeat.
                        if (line.charAt(0) == 'V') {
                            deckVictories++;
                            totalVictories++;
                            this.setWinAgainstClass(opponentClass);
                            this.setClassSpecificVictory(deckClass);
                        } else if (line.charAt(0) == 'D') {
                            totalDefeats++;
                            this.setLossAgainstClass(opponentClass);
                            this.setClassSpecificDefeat(deckClass);
                        }
                        gamesPlayed++;
                    }
                    determineSuccessfulDeck(deck.get(0), deckVictories, lastDeckVictories);
                    determineFavoriteDeck(deck.get(0), gamesPlayed, lastDeckGamesPlayed);

                    lastDeckVictories = deckVictories;
                    lastDeckGamesPlayed = gamesPlayed;
                }
            }
            if (favoriteDeck == null) {
                favoriteDeck = "None";
            }
            if (mostSuccessDeck == null) {
                mostSuccessDeck = "None";
            }
            totalGames = totalVictories + totalDefeats;

            saveStatValues();
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
        this.mostSuccessDeck = mostSuccessfulDeck;
    }

    public void setMostSuccessDeckClass(String mostSuccessDeckClass) {
        this.mostSuccessDeckClass = mostSuccessDeckClass;
    }

    public void setFavoriteDeckClass(String favoriteDeckClass) {
        this.favoriteDeckClass = favoriteDeckClass;
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
        return mostSuccessDeck;
    }

    public String getMostSuccessDeckClass() {
        return mostSuccessDeckClass;
    }

    public String getFavoriteDeckClass() {
        return favoriteDeckClass;
    }

    public HashMap<String, Integer> getWinPerClass() {
        return winPerClass;
    }
}

