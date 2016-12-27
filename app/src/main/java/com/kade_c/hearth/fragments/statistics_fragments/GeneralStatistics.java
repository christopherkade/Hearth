package com.kade_c.hearth.fragments.statistics_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kade_c.hearth.MainActivity;
import com.kade_c.hearth.R;
import com.kade_c.hearth.Statistics;

/**
 * First slide of our ViewPager.
 * Displays the players general statistics.
 */
public class GeneralStatistics extends Fragment {

    View view;

    // Handles the saving / calculating of our statistics.
    private Statistics statistics;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.statistics_general, container, false);

        // Retrieves stats.
        statistics = new Statistics(getContext(), getActivity());

        // Sets them on the view.
        setStatValues();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Statistics");
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

        String favDeck = statistics.getFavoriteDeck();
        String successfulDeck = statistics.getMostSuccessfulDeck();
        if (!favDeck.equals("")) {
            int resourceIdFav = getActivity().getResources().getIdentifier(statistics.getFavoriteDeckClass().toLowerCase(), "mipmap", getActivity().getPackageName());
            favDeckIcon.setImageResource(resourceIdFav);
        }
        if (!successfulDeck.equals("")) {
            int resourceIdSuccess = getActivity().getResources().getIdentifier(statistics.getMostSuccessDeckClass().toLowerCase(), "mipmap", getActivity().getPackageName());
            successDeckIcon.setImageResource(resourceIdSuccess);
        }
    }

}
