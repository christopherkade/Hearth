package com.kade_c.hearth.fragments.statistics_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kade_c.hearth.InternalFilesManager;
import com.kade_c.hearth.R;
import com.kade_c.hearth.Statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * First slide of our ViewPager.
 * Displays the players general statistics.
 */
public class GeneralStatistics extends Fragment {

    View view;

    private ArrayList<String> deckList;

    private int totalGames = 0;
    private int totalVictories = 0;
    private int totalDefeats = 0;
    private String favoriteDeck = "None";
    private String favoriteDeckClass = "";
    private String mostSuccessDeck = "None";
    private String mostSuccessDeckClass = "";


    // Handles the saving / calculating of our statistics.
    private Statistics statistics;


    public static final String ARG_OBJECT = "object";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.statistics_general, container, false);
        saveDeckList();
        saveTotalDeckStatistics();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("General");
    }

    /**
     * Calls our InternalFileManager to get the deck list.
     */
    private void saveDeckList() {
        // Instantiate our Deck List File Manager.
        InternalFilesManager.DeckListFileManager DLFM =
                new InternalFilesManager(getContext(), getActivity()).new DeckListFileManager();

        // Get our deck list from the file.
        deckList = DLFM.getDecksFromFile();
    }

    /**
     * Calls our InternalFilesManager to get every deck-specific file
     * and save it in a List of Lists.
     */
    private void saveTotalDeckStatistics() {
        // Instantiate our Deck File Manager.
        InternalFilesManager.DeckFileManager DFM =
                new InternalFilesManager(getContext(), getActivity()).new DeckFileManager();
        List<List<String>> deckStats = new ArrayList<>();

        // Get each deck's stat list one by one and save them.
        for (String deck : deckList) {
            deckStats.add(DFM.readStatisticsFile(deck));
        }
        determineStatValues(deckStats);
    }

    /**
     * Determines the total number of games played, won, and lost,
     * aswell as the most played deck and the most successful one.
     */
    private void determineStatValues(List<List<String>> deckStatsList) {
        int lastDeckGamesPlayed = 0;
        int lastDeckVictories = 0;
        statistics = new Statistics();

        if (deckStatsList.size() != 0) {
            // Goes through every deck.
            for (List<String> deck : deckStatsList) {
                if (deck.size() != 0) {
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
                            statistics.setClassSpecificVictory(opponentClass);
                        } else if (line.charAt(0) == 'D') {
                            totalDefeats++;
                            statistics.setClassSpecificDefeat(opponentClass);
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

        setStatValues();
    }

    /**
     * Sets the players favorite deck (most played).
     */
    private void determineFavoriteDeck(String deckName, int gamesPlayed, int lastDeckGamesPlayed) {
        if (gamesPlayed > lastDeckGamesPlayed) {
            String[] favDeckSplit = deckName.split(" \\| ");
            favoriteDeckClass = favDeckSplit[0];
            favoriteDeck = favDeckSplit[1];
        }
    }

    /**
     * Sets the players most successful deck (most victories).
     */
    private void determineSuccessfulDeck(String deckName, int deckVictories, int lastDeckVictories) {
        if (deckVictories > lastDeckVictories) {
            String[] successDeckSplit = deckName.split(" \\| ");
            mostSuccessDeckClass = successDeckSplit[0];
            mostSuccessDeck = successDeckSplit[1];
        }
    }

    /**
     * Saves statistics values in our Statistics object.
     */
    private void saveStatValues() {
        statistics.calculateVictoryPercentage(totalGames, totalVictories);
        statistics.setGamesPlayed(totalGames);
        statistics.setGamesWon(totalVictories);
        statistics.setGamesLost(totalDefeats);
        statistics.setActiveDecksNumber(deckList.size());
        statistics.setFavoriteDeck(favoriteDeck);
        statistics.setMostSuccessfulDeck(mostSuccessDeck);
    }

    /**
     * Sets textual values on our View.
     */
    private void setStatValues() {
        TextView totalPercentageWin = (TextView) view.findViewById(R.id.total_victory_percentage_value);
        TextView totalGames = (TextView) view.findViewById(R.id.total_games_played_value);
        TextView totalGamesWon = (TextView) view.findViewById(R.id.total_games_won_value);
        TextView totalGamesLost = (TextView) view.findViewById(R.id.total_games_lost_value);
        TextView totalDecks = (TextView) view.findViewById(R.id.total_active_decks_value);
        TextView favoriteDeck = (TextView) view.findViewById(R.id.favorite_deck_value);
        TextView successDeck = (TextView) view.findViewById(R.id.most_success_deck_value);
        ImageView favDeckIcon = (ImageView) view.findViewById(R.id.most_played_deck_icon);
        ImageView successDeckIcon = (ImageView) view.findViewById(R.id.most_success_deck_icon);

        totalPercentageWin.setText(statistics.getPercentageWin().toString() + "%");
        totalGames.setText(String.valueOf(statistics.getGamesPlayed()));
        totalGamesWon.setText(String.valueOf(statistics.getGamesWon()));
        totalGamesLost.setText(String.valueOf(statistics.getGamesLost()));
        totalDecks.setText(String.valueOf(statistics.getActiveDecksNumber()));
        favoriteDeck.setText(String.valueOf(statistics.getFavoriteDeck()));
        successDeck.setText(String.valueOf(statistics.getMostSuccessfulDeck()));

        if (!favoriteDeckClass.equals("")) {
            int resourceIdFav = getActivity().getResources().getIdentifier(favoriteDeckClass.toLowerCase(), "mipmap", getActivity().getPackageName());
            favDeckIcon.setImageResource(resourceIdFav);
        }
        if (!mostSuccessDeckClass.equals("")) {
            int resourceIdSuccess = getActivity().getResources().getIdentifier(mostSuccessDeckClass.toLowerCase(), "mipmap", getActivity().getPackageName());
            successDeckIcon.setImageResource(resourceIdSuccess);
        }
    }

}
