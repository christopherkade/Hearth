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

    private Float percentageWin = 0f;
    private int activeDecksNumber = 0;

    // Wins / Losses against classes.
    private HashMap<String, Integer> winAgainstClass = new HashMap<>();
    private HashMap<String, Integer> lossAgainstClass = new HashMap<>();

    // Wins / Losses per class.
    private HashMap<String, Integer> winPerClass = new HashMap<>();
    private HashMap<String, Integer> lossPerClass = new HashMap<>();

    private String mostPlayedDeck = "None";
    private String mostPlayedDeckClass = "";

    private String mostSuccessDeck = "None";
    private String mostSuccessDeckClass = "";

    private String[] classes = {"Mage", "Hunter", "Paladin",
            "Warrior", "Druid", "Warlock",
            "Shaman", "Priest", "Rogue",
            null};

    /**
     * Calculates the victory percentage with the parameters.
     */
    private Float calculateVictoryPercentage(int totalGames, int totalVictories) {
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
     * Sets the players most played deck.
     */
    private void determineMostPlayedDeck(String deckName, int gamesPlayed, int mostPlayedDeckGames) {
        if (gamesPlayed > mostPlayedDeckGames) {
            String[] favDeckSplit = deckName.split(" \\| ");
            mostPlayedDeckClass = favDeckSplit[0];
            mostPlayedDeck = favDeckSplit[1].replace("\n", "");
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

        this.setTotalGames(totalGames);
        this.setTotalVictories(totalVictories);
        this.setTotalDefeats(totalDefeats);

        this.setActiveDecksNumber(deckList.size());

        this.setMostPlayedDeck(mostPlayedDeck);
        this.setMostSuccessfulDeck(mostSuccessDeck);
        this.setMostPlayedDeckClass(mostPlayedDeckClass);
        this.setMostSuccessDeckClass(mostSuccessDeckClass);
    }

    /**
     * Initializes our winPerClass map.
     */
    private void initMap() {
        for (int i = 0; classes[i] != null; i++) {
            winPerClass.put(classes[i], 0);
            lossPerClass.put(classes[i], 0);
        }
    }

    // Constructor for Victory / Defeat Graphs

    /**
     * Constructor for Victory and Defeat Graphs statistics.
     */
    public Statistics(Context ctx, FragmentActivity fragmentActivity, boolean isVictory) {
        InternalFilesManager IFM = new InternalFilesManager(ctx, fragmentActivity);

        // Initialize our victory / defeat HashMap.
        initMap();

        deckList = IFM.getDecksFromFile();

        if (deckList.size() != 0) {
            List<List<String>> games = new ArrayList<>();

            // For every deck in our Deck List
            for (String deck : deckList) {
                List<String> gameList = IFM.readStatisticsFile(deck);
                games.add(gameList);
            }

            // For every games list in our List of games list.
            for (List<String> gamesList : games) {
                // If no games for deck, continue.
                if (gamesList.size() == 0) continue;
                // Class of the current deck being analysed.
                String deckClass = gamesList.get(0).split(" \\| ")[0].trim();

                // For every game in our games list.
                for (int i = 1; i < gamesList.size(); i++) {
                    String line = gamesList.get(i);

                    // Register victory or defeat.
                    if (line.charAt(0) == 'V') {
                        if (isVictory)
                            this.setClassSpecificVictory(deckClass);
                    } else if (line.charAt(0) == 'D') {
                        if (!isVictory)
                            this.setClassSpecificDefeat(deckClass);
                    }
                }
            }
        }
    }

    /**
     * Called when Statistics is instantiated, retrieves general statistics.
     */
    public Statistics(Context ctx, FragmentActivity fragmentActivity) {
        InternalFilesManager IFM = new InternalFilesManager(ctx, fragmentActivity);

        // Initialize our victories HashMap.
        initMap();

        // Get our deck list from the file.
        deckList = IFM.getDecksFromFile();

        // Checks if decks exist.
        if (deckList.size() != 0) {
            List<List<String>> games = new ArrayList<>();

            // Get each deck's stat list one by one and save them.
            int gamesPlayed;
            int mostPlayedDeckGames = 0;
            int counter = 0;

            // For every deck in our Deck List
            for (String deck : deckList) {
                List<String> gameList = IFM.readStatisticsFile(deck);
                games.add(gameList);

                // Determines players most played deck.
                gamesPlayed = gameList.size() - 1;

                determineMostPlayedDeck(deckList.get(counter), gamesPlayed, mostPlayedDeckGames);
                if (gamesPlayed > mostPlayedDeckGames)
                    mostPlayedDeckGames = gamesPlayed;
                counter++;
            }

            // Determines players most successful deck.
            int lastDeckVictories = 0;

            // For every games list in our List of games list.
            for (List<String> gamesList : games) {
                // If no games for deck, continue.
                if (gamesList.size() == 0) continue;
                // Class of the current deck being analysed.
                String deckClass = gamesList.get(0).split(" \\| ")[0].trim();
                int deckVictories = 0;

                // For every game in our games list.
                for (int i = 1; i < gamesList.size(); i++) {
                    String line = gamesList.get(i);
                    String opponentClass = line.split(" ")[1].replace("\n", "");

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
                }
                determineSuccessfulDeck(gamesList.get(0), deckVictories, lastDeckVictories);
                if (deckVictories > lastDeckVictories)
                    lastDeckVictories = deckVictories;

                if (mostPlayedDeck == null) {
                    mostPlayedDeck = "None";
                }
                if (mostSuccessDeck == null) {
                    mostSuccessDeck = "None";
                }
            }
            totalGames = totalVictories + totalDefeats;

            saveStatValues();
        }
    }

    // SETTERS
    private void setTotalGames(int gamesPlayed) {
        this.totalGames = gamesPlayed;
    }
    private void setActiveDecksNumber(int activeDecksNumber) {
        this.activeDecksNumber = activeDecksNumber;
    }

    private void setMostPlayedDeck(String mostPlayedDeck) {
        this.mostPlayedDeck = mostPlayedDeck;
    }

    private void setMostSuccessfulDeck(String mostSuccessfulDeck) {
        this.mostSuccessDeck = mostSuccessfulDeck;
    }

    private void setMostSuccessDeckClass(String mostSuccessDeckClass) {
        this.mostSuccessDeckClass = mostSuccessDeckClass;
    }

    private void setMostPlayedDeckClass(String mostPlayedDeckClass) {
        this.mostPlayedDeckClass = mostPlayedDeckClass;
    }

    private void setTotalDefeats(int totalDefeats) {
        this.totalDefeats = totalDefeats;
    }

    private void setTotalVictories(int totalVictories) {
        this.totalVictories = totalVictories;
    }

    // GETTERS
    public Float getPercentageWin() {
        return percentageWin;
    }

    public int getActiveDecksNumber() {
        return activeDecksNumber;
    }

    public String getMostPlayedDeck() {
        return mostPlayedDeck;
    }

    public String getMostSuccessfulDeck() {
        return mostSuccessDeck;
    }

    public String getMostSuccessDeckClass() {
        return mostSuccessDeckClass;
    }

    public String getMostPlayedDeckClass() {
        return mostPlayedDeckClass;
    }

    public HashMap<String, Integer> getWinPerClass() {
        return winPerClass;
    }

    public HashMap<String, Integer> getLossPerClass() {
        return lossPerClass;
    }

    public int getTotalDefeats() {
        return totalDefeats;
    }

    public int getTotalVictories() {
        return totalVictories;
    }

    public int getTotalGames() {
        return totalGames;
    }
}

