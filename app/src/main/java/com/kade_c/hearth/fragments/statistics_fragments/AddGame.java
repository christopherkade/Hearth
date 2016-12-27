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
import com.kade_c.hearth.MainActivity;
import com.kade_c.hearth.R;

/**
 * Handles the Victory / Defeat adding.
 * Is called when the user clicks on an 'Add Victory' or 'Add Defeat' button
 * in the Deck-specific Statistics Fragment.
 */
public class AddGame extends Fragment {

    View view;

    // Either 'V' for Victory or 'D' for 'Defeat'.
    private String type;

    // Name of the deck concerned.
    private String deckName;

    // Class of the deck concerned.
    private String deckClass;

    // Class the player has won against.
    private String wonAgainst;

    // Our internal file manager that writes in our deck files.
    private InternalFilesManager IFM;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.add_game, container, false);

        IFM = new InternalFilesManager(getContext(), getActivity());

        ((MainActivity) getActivity()).setDrawerEnabled(false);

        TextView wonOrLostText = (TextView) view.findViewById(R.id.selectClass_text);

        type = this.getArguments().getString("type");
        // Checks if we have won or lost and displays the right sentence.
        if (type.equals("V")) {
            wonOrLostText.setText(R.string.victory_class_select_text);
        } else if (type.equals("D")) {
            wonOrLostText.setText(R.string.defeat_class_select_text);
        }

        // Saves deck name and class.
        deckName = this.getArguments().getString("deckName");
        deckClass = this.getArguments().getString("deckClass");

        setListeners();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("General Statistics");
    }

    /**
     * Sets the class button listeners.
     */
    private void setListeners() {
        final ImageView mageImg = (ImageView)view.findViewById(R.id.mage_button);
        mageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonAgainst = "Mage";
                IFM.writeStatisticsFile(deckClass, deckName, type, wonAgainst);
                callLastFragment();
            }
        });

        final ImageView hunterImg = (ImageView) view.findViewById(R.id.hunter_button);
        hunterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonAgainst = "Hunter";
                IFM.writeStatisticsFile(deckClass, deckName, type, wonAgainst);
                callLastFragment();
            }
        });

        final ImageView paladinImg = (ImageView) view.findViewById(R.id.paladin_button);
        paladinImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonAgainst = "Paladin";
                IFM.writeStatisticsFile(deckClass, deckName, type, wonAgainst);
                callLastFragment();
            }
        });

        final ImageView warriorImg = (ImageView) view.findViewById(R.id.warrior_button);
        warriorImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonAgainst = "Warrior";
                IFM.writeStatisticsFile(deckClass, deckName, type, wonAgainst);
                callLastFragment();
            }
        });

        final ImageView druidImg = (ImageView) view.findViewById(R.id.druid_button);
        druidImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonAgainst = "Druid";
                IFM.writeStatisticsFile(deckClass, deckName, type, wonAgainst);
                callLastFragment();
            }
        });

        final ImageView warlockImg = (ImageView) view.findViewById(R.id.warlock_button);
        warlockImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonAgainst = "Warlock";
                IFM.writeStatisticsFile(deckClass, deckName, type, wonAgainst);
                callLastFragment();
            }
        });

        final ImageView shamanImg = (ImageView) view.findViewById(R.id.shaman_button);
        shamanImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonAgainst = "Shaman";
                IFM.writeStatisticsFile(deckClass, deckName, type, wonAgainst);
                callLastFragment();
            }
        });

        final ImageView priestImg = (ImageView) view.findViewById(R.id.priest_button);
        priestImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonAgainst = "Priest";
                IFM.writeStatisticsFile(deckClass, deckName, type, wonAgainst);
                callLastFragment();
            }
        });

        final ImageView rogueImg = (ImageView) view.findViewById(R.id.rogue_button);
        rogueImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonAgainst = "Rogue";
                IFM.writeStatisticsFile(deckClass, deckName, type, wonAgainst);
                callLastFragment();
            }
        });
    }

    /**
     * Calls the DeckSelectedStatistics fragment.
     */
    private void callLastFragment() {
        final Fragment homeFragment = new DeckSelectedStatistics();
        final Bundle bundle = new Bundle();
        bundle.putString("deckName", deckName);
        bundle.putString("deckClass", deckClass);
        homeFragment.setArguments(bundle);
        ((MainActivity)getActivity()).addFragment(homeFragment);
    }
}
