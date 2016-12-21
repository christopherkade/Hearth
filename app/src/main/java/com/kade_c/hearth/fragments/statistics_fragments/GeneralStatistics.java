package com.kade_c.hearth.fragments.statistics_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kade_c.hearth.R;

/**
 * TODO: Should handle general statistics about the user.
 * Number of decks, number of games played etc.
 */
public class GeneralStatistics extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.statistics_general, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("General Statistics");
    }
}
