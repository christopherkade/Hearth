package com.example.kade_c.hearth.fragments.statistics_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.kade_c.hearth.MainActivity;
import com.example.kade_c.hearth.R;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Handles the name inputting on deck creation.
 * Lets the user input a name for their deck and creates deck file.
 */
public class DeckCreationName extends Fragment {

    View view;

    private String FILENAME = "Deck_Info";

    // Name inputted.
    private String name;

    // Class selected in the previous Fragment.
    private String classSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.deck_creation_name, container, false);
        classSelected = getArguments().getString("classSelected");

        setClassImage();

        final Button validateButton = (Button) view.findViewById(R.id.validate_name_button);
        validateButton.setOnClickListener(new View.OnClickListener() {

            /**
             * When clicked, launch the API card searching method AsyncTask method
             * and display the given URL with imageLoader.
             */
            @Override
            public void onClick(View v) {
                EditText nameInputted = (EditText) view.findViewById(R.id.selectName_input);

                // Get the inputted name as a String
                name = nameInputted.getText().toString();

                createDeck();

                callDeckFragment();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Deck Creation - Name");
    }

    /**
     * Sets the class icon image for the Name selection Fragment.
     */
    private void setClassImage() {
        ImageView img = (ImageView) view.findViewById(R.id.deckName_classImage);
        String classMin = classSelected.toLowerCase();

        int resourceId = getActivity().getResources().getIdentifier(classMin, "mipmap", getActivity().getPackageName());
        img.setImageResource(resourceId);
    }

    /**
     * Create file containing the newly created deck
     * Adds a "class | deck_name" line in our deck info file.
     */
    private void createDeck() {
        String deckInfo = classSelected + " | " + name;

        try {
            FileOutputStream fos = getActivity().openFileOutput(FILENAME, Context.MODE_APPEND);
            fos.write(deckInfo.getBytes());
            fos.write('\n');
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * After inputting a name, we go back to our Deck Statistics fragment.
     */
    private void callDeckFragment() {
        final Fragment homeFragment = new DeckStatistics();
        final Bundle bundle = new Bundle();
        bundle.putString("classSelected", classSelected);
        bundle.putString("name", name);
        homeFragment.setArguments(bundle);
        ((MainActivity)getActivity()).addFragment(homeFragment);
    }
}
