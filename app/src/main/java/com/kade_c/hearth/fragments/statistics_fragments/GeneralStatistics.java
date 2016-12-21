package com.kade_c.hearth.fragments.statistics_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kade_c.hearth.InternalFilesManager;
import com.kade_c.hearth.R;
import com.kade_c.hearth.Statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles general statistics about the user.
 * Number of decks, number of games played etc.
 */
public class GeneralStatistics extends Fragment {

    View view;

    // List of all the decks created.
    private ArrayList<String> deckList;

    private int totalGames = 0;
    private int totalVictories = 0;
    private int totalDefeats = 0;

    // Handles the saving / calculating of our statistics.
    private Statistics statistics;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.statistics_general, container, false);

        saveDeckList();
        saveTotalDeckStatistics();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("General Statistics");
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
        determineGameValues(deckStats);
    }

    /**
     * Determines the total number of games played, won and lost for
     * every deck available.
     */
    private void determineGameValues(List<List<String>> deckStatsList) {
        statistics = new Statistics();

        if (deckStatsList.size() != 0) {
            // Goes through every deck.
            for (List<String> deckStat : deckStatsList) {
                if (deckStat.size() != 0) {

                    // Goes through every line of every deck stat file.
                    for (int i = 1; i < deckStat.size(); i++) {
                        String line = deckStat.get(i);
                        String opponentClass = line.split(" ")[1];

                        // Register victory or defeat.
                        if (line.charAt(0) == 'V') {
                            totalVictories++;
                            statistics.setClassSpecificVictory(opponentClass);
                        } else if (line.charAt(0) == 'D') {
                            totalDefeats++;
                            statistics.setClassSpecificDefeat(opponentClass);
                        }
                    }
                }
            }
            totalGames = totalVictories + totalDefeats;

            // Save the statistics in our Statistics object.
            statistics.calculateVictoryPercentage(totalGames, totalVictories);
            statistics.setGamesPlayed(totalGames);
            statistics.setGamesWon(totalVictories);
            statistics.setGamesLost(totalDefeats);
            statistics.setActiveDecksNumber(deckList.size());
        }

        setGameValues();
    }

    /**
     * Sets textual values on our View.
     */
    private void setGameValues() {
        TextView totalPercentageWin = (TextView) view.findViewById(R.id.total_victory_percentage_value);
        TextView totalGames = (TextView) view.findViewById(R.id.total_games_played_value);
        TextView totalGamesWon = (TextView) view.findViewById(R.id.total_games_won_value);
        TextView totalGamesLost = (TextView) view.findViewById(R.id.total_games_lost_value);
        TextView totalDecks = (TextView) view.findViewById(R.id.total_active_decks_value);

        totalPercentageWin.setText(statistics.getPercentageWin().toString() + "%");
        totalGames.setText(String.valueOf(statistics.getGamesPlayed()));
        totalGamesWon.setText(String.valueOf(statistics.getGamesWon()));
        totalGamesLost.setText(String.valueOf(statistics.getGamesLost()));
        totalDecks.setText(String.valueOf(statistics.getActiveDecksNumber()));
    }
}
