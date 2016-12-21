package com.kade_c.hearth.fragments.statistics_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kade_c.hearth.MainActivity;
import com.kade_c.hearth.R;

/**
 * Handles the class selection on deck creation.
 * Displays class icons and lets the user click on the one that is appropriate.
 */
public class DeckCreationClassSelector extends Fragment {

    View view;

    private String classSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.deck_creation_class, container, false);

        ((MainActivity) getActivity()).setDrawerEnabled(false);
        setListeners();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Deck Creation - Class");
    }

    /**
     * Sets the class button listeners.
     */
    private void setListeners() {
        final ImageView mageImg = (ImageView)view.findViewById(R.id.mage_button);
        mageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classSelected = "Mage";
                callNameFragment();
            }
        });

        final ImageView hunterImg = (ImageView) view.findViewById(R.id.hunter_button);
        hunterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classSelected = "Hunter";
                callNameFragment();
            }
        });

        final ImageView paladinImg = (ImageView) view.findViewById(R.id.paladin_button);
        paladinImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classSelected = "Paladin";
                callNameFragment();
            }
        });

        final ImageView warriorImg = (ImageView) view.findViewById(R.id.warrior_button);
        warriorImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classSelected = "Warrior";
                callNameFragment();
            }
        });

        final ImageView druidImg = (ImageView) view.findViewById(R.id.druid_button);
        druidImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classSelected = "Druid";
                callNameFragment();
            }
        });

        final ImageView warlockImg = (ImageView) view.findViewById(R.id.warlock_button);
        warlockImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classSelected = "Warlock";
                callNameFragment();
            }
        });

        final ImageView shamanImg = (ImageView) view.findViewById(R.id.shaman_button);
        shamanImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classSelected = "Shaman";
                callNameFragment();
            }
        });

        final ImageView priestImg = (ImageView) view.findViewById(R.id.priest_button);
        priestImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classSelected = "Priest";
                callNameFragment();
            }
        });

        final ImageView rogueImg = (ImageView) view.findViewById(R.id.rogue_button);
        rogueImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classSelected = "Rogue";
                callNameFragment();
            }
        });
    }

    /**
     * After selecting a class, we call the Name selection Fragment.
     */
    private void callNameFragment() {
        final Fragment homeFragment = new DeckCreationName();
        final Bundle bundle = new Bundle();
        bundle.putString("classSelected", classSelected);
        homeFragment.setArguments(bundle);
        ((MainActivity)getActivity()).addFragment(homeFragment);
    }
}
