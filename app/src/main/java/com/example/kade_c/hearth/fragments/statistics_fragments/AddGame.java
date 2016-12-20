package com.example.kade_c.hearth.fragments.statistics_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.kade_c.hearth.MainActivity;
import com.example.kade_c.hearth.R;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Handles the Victory / Defeat adding.
 * Is called when the user clicks on an 'Add Victory' or 'Add Defeat' button
 * in the Deck-specific Statistics Fragment.
 */
public class AddGame extends Fragment {

    View view;

    // Either 'V' for Victory or 'D' for 'Defeat'.
    String type;

    // Name of the deck concerned.
    String deckName;

    // Class the player has won against.
    String wonAgainst;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.add_game, container, false);

        ((MainActivity) getActivity()).setDrawerEnabled(false);

        TextView wonOrLostText = (TextView) view.findViewById(R.id.selectClass_text);

        type = this.getArguments().getString("type");
        // Checks if we have won or lost and displays the right sentence.
        if (type.equals("V")) {
            wonOrLostText.setText(R.string.victory_class_select_text);
        } else if (type.equals("D")) {
            wonOrLostText.setText(R.string.defeat_class_select_text);
        }

        deckName = this.getArguments().getString("deckName");

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
                writeInStatFile();
            }
        });

        final ImageView hunterImg = (ImageView) view.findViewById(R.id.hunter_button);
        hunterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonAgainst = "Hunter";
                writeInStatFile();
            }
        });

        final ImageView paladinImg = (ImageView) view.findViewById(R.id.paladin_button);
        paladinImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonAgainst = "Paladin";
                writeInStatFile();
            }
        });

        final ImageView warriorImg = (ImageView) view.findViewById(R.id.warrior_button);
        warriorImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonAgainst = "Warrior";
                writeInStatFile();
            }
        });

        final ImageView druidImg = (ImageView) view.findViewById(R.id.druid_button);
        druidImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonAgainst = "Druid";
                writeInStatFile();
            }
        });

        final ImageView warlockImg = (ImageView) view.findViewById(R.id.warlock_button);
        warlockImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonAgainst = "Warlock";
                writeInStatFile();
            }
        });

        final ImageView shamanImg = (ImageView) view.findViewById(R.id.shaman_button);
        shamanImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonAgainst = "Shaman";
                writeInStatFile();
            }
        });

        final ImageView priestImg = (ImageView) view.findViewById(R.id.priest_button);
        priestImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonAgainst = "Priest";
                writeInStatFile();
            }
        });

        final ImageView rogueImg = (ImageView) view.findViewById(R.id.rogue_button);
        rogueImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wonAgainst = "Rogue";
                writeInStatFile();
            }
        });
    }

    /**
     * Writes the inputted information in our deck-specific file.
     * Line format:
     * [V/D] [Class]
     */
    private void writeInStatFile() {
        try {
            FileOutputStream fos = getActivity().openFileOutput(deckName, Context.MODE_APPEND);

            if (type.equals("V")) {
                fos.write("V ".getBytes());
            } else if (type.equals("D")) {
                fos.write("D ".getBytes());
            }
            fos.write(wonAgainst.getBytes());
            fos.write('\n');
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        callLastFragment();
    }

    /**
     * Calls the DeckSelectedStatistics fragment.
     */
    private void callLastFragment() {
        final Fragment homeFragment = new DeckSelectedStatistics();
        final Bundle bundle = new Bundle();
        bundle.putString("deckName", deckName);
        homeFragment.setArguments(bundle);
        ((MainActivity)getActivity()).addFragment(homeFragment);
    }
}
