package com.example.kade_c.hearth.fragments.statistics_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kade_c.hearth.MainActivity;
import com.example.kade_c.hearth.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles the Deck-specific Statistics Fragment.
 * Lets the user add a victory/defeat with the deck and
 * displays statistics on these informations.
 */
public class DeckSelectedStatistics extends Fragment {

    View view;

    // The name of the deck selected.
    String deckName;

    // A list of lines in our deck specific stat file.
    ArrayList<String> lines;

    Integer totalGame = 0;
    Integer numVictories = 0;
    Integer numDefeats = 0;

    /**
     * Saves deckName selected.
     * Sets the deck name as title and its class icon.
     * Creates or opens our stat file to write its first line.
     * Reads stat file to process data.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.statistics_deck_selected, container, false);

        // Sets the Drawer as enabled.
        ((MainActivity) getActivity()).setDrawerEnabled(true);

        deckName = getArguments().getString("deck");
        if (deckName == null) {
            deckName = this.getArguments().getString("deckName");
        }

        // Displays the right name and class icon.
        setDeckNameAndIcon();

        // Open or creates the deck specific statistics file.
        openStatisticsFile();

        // Reads the deck specific statistics file and saves lines in 'lines' ArrayList.
        readStatisticsFile();

        // Calculates stats and displays them in Fragment.
        calculateStats();

        /**
         * Handles the click of "Add Victory"
         */
        Button victoryButton = (Button) view.findViewById(R.id.addVictory_button);
        victoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddGame("V");
            }
        });

        /**
         * Handles the click of "Add Defeat"
         */
        Button defeatButton = (Button) view.findViewById(R.id.addDefeat_button);
        defeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddGame("D");
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Deck Statistics");
    }

    /**
     * Opens the AddGame Fragment to get who they have won against.
     */
    public void openAddGame(String type) {
        final Fragment homeFragment = new AddGame();
        final Bundle bundle = new Bundle();
        bundle.putString("deckName", deckName);
        bundle.putString("type", type);
        homeFragment.setArguments(bundle);
        ((MainActivity)getActivity()).addFragment(homeFragment);
    }

    /**
     * Should create or open a file names after the deck and save it.
     */
    public void openStatisticsFile() {
        try {
            FileOutputStream fos = getActivity().openFileOutput(deckName, Context.MODE_APPEND);
            File file = getContext().getFileStreamPath(deckName);
            if (file.length() == 0)
                fos.write(deckName.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open Deck specific stat file and reads its content.
     */
    public void readStatisticsFile() {
        File file = getContext().getFileStreamPath(deckName);
        String line = "";
        lines = new ArrayList<>();
        byte[] buffer = new byte[4096];
        char c;
        int ret;

        try {
            // Checks if file exists
            if (file != null && file.exists()) {
                FileInputStream fos = getActivity().openFileInput(deckName);

                int i = 0;
                for (ret = fos.read(buffer); ret > 0; ret--) {
                    c = (char) buffer[i];
                    line += c;
                    i++;

                    // At every new line, add the previous one to our ArrayList.
                    if (c == '\n') {
                        lines.add(line);
                        line = "";
                    }
                }
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the correct deck name and it's class icon.
     */
    public void setDeckNameAndIcon() {
        TextView deckNameText = (TextView) view.findViewById(R.id.selectedDeckName_text);
        ImageView img= (ImageView) view.findViewById(R.id.deck_stat_icon);
        String[] array;
        String deckClass;
        String deck;

        array = deckName.split("\\|");
        deckClass = array[0].trim().toLowerCase();
        deck = array[1].trim();

        deckNameText.setText(deck);
        int resourceId = getActivity().getResources().getIdentifier(deckClass, "mipmap", getActivity().getPackageName());
        img.setImageResource(resourceId);
    }

    /**
     * Sets the TextView values on the Fragment with the corresponding statistics.
     */
    public void setStatValues(String percentageWin, String totalGames,
                              String totalWin, String totalLoss) {
        TextView victoryPercentageView = (TextView) view.findViewById(R.id.victory_percentage_value);
        TextView totalGamesView = (TextView) view.findViewById(R.id.total_games_value);
        TextView totalWonView = (TextView) view.findViewById(R.id.game_won_value);
        TextView totalLostView = (TextView) view.findViewById(R.id.game_lost_value);

        victoryPercentageView.setText(percentageWin.toString() + "%");
        totalGamesView.setText(totalGames);
        totalWonView.setText(totalWin);
        totalLostView.setText(totalLoss);
    }

    /**
     * Retrieves the following stats:
     * Percentage of win
     * Number of games played
     * Number of games won
     * Number of games lost
     */
    public void calculateStats() {
        Float percentageWin = 0f;

        for (int i = 1; i < lines.size(); i ++) {
            String line = lines.get(i);
            if (line.charAt(0) == 'V') {
                numVictories++;
            } else if (line.charAt(0) == 'D') {
                numDefeats++;
            }
        }
        totalGame = numVictories + numDefeats;

        if (totalGame > 0) {
            percentageWin = (float) ((numVictories * 100) / totalGame);
        }

        setStatValues(percentageWin.toString(), totalGame.toString(), numVictories.toString(), numDefeats.toString());
    }
}
