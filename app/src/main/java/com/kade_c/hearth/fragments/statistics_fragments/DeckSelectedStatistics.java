package com.kade_c.hearth.fragments.statistics_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kade_c.hearth.InternalFilesManager;
import com.kade_c.hearth.MainActivity;
import com.kade_c.hearth.R;

import java.util.ArrayList;

/**
 * Handles the Deck-specific Statistics Fragment.
 * Lets the user add a victory/defeat with the deck and
 * displays statistics on these informations.
 */
public class DeckSelectedStatistics extends Fragment {

    View view;

    // The name of the deck selected.
    private String deckName;

    // The class of the deck selected.
    private String deckClass;

    // Name of the file that contains deck stats.
    private String fileName;

    // A list of lines in our deck specific stat file.
    private ArrayList<String> lines;

    // Our Internal file manager that handles the reading and writing of
    // internal statistics files.
    private InternalFilesManager.DeckFileManager DFM;

    private Integer totalGame = 0;
    private Integer numVictories = 0;
    private Integer numDefeats = 0;

    /**
     * Saves deckName selected.
     * Sets the deck name as title and its class icon.
     * Creates or opens our stat file to write its first line.
     * Reads stat file to process data.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.statistics_deck_selected, container, false);

        DFM = new InternalFilesManager(getContext(), getActivity()). new DeckFileManager();

        // Sets the Drawer as enabled.
        ((MainActivity) getActivity()).setDrawerEnabled(true);

        deckName = getArguments().getString("deckName");
        if (deckName == null) {
            deckName = this.getArguments().getString("deckName");
        }

        deckClass = getArguments().getString("deckClass");

        // Displays the right name and class icon.
        setDeckNameAndIcon();

        // Opens deck file and reads its information.
        lines = DFM.getDeckFileInformation(deckClass, deckName);

        // Calculates stats with information from file and displays them.
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
    private void openAddGame(String type) {
        final Fragment homeFragment = new AddGame();
        final Bundle bundle = new Bundle();
        bundle.putString("deckName", deckName);
        bundle.putString("deckClass", deckClass);
        bundle.putString("type", type);
        homeFragment.setArguments(bundle);
        ((MainActivity)getActivity()).addFragment(homeFragment);
    }

    /**
     * Displays the correct deck name and it's class icon.
     */
    private void setDeckNameAndIcon() {
        TextView deckNameText = (TextView) view.findViewById(R.id.selectedDeckName_text);
        ImageView img = (ImageView) view.findViewById(R.id.deck_stat_icon);

        deckNameText.setText(deckName);
        int resourceId = getActivity().getResources().getIdentifier(deckClass.toLowerCase(), "mipmap", getActivity().getPackageName());
        img.setImageResource(resourceId);
    }

    /**
     * Sets the TextView values on the Fragment with the corresponding statistics.
     */
    private void setStatValues(String percentageWin, String totalGames,
                              String totalWin, String totalLoss) {
        TextView victoryPercentageView = (TextView) view.findViewById(R.id.victory_percentage_value);
        TextView totalGamesView = (TextView) view.findViewById(R.id.total_games_value);
        TextView totalWonView = (TextView) view.findViewById(R.id.game_won_value);
        TextView totalLostView = (TextView) view.findViewById(R.id.game_lost_value);

        victoryPercentageView.setText(percentageWin + "%");
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
    private void calculateStats() {
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
