package com.kade_c.hearth.fragments.statistics_fragments;

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
import android.widget.Toast;

import com.kade_c.hearth.InternalFilesManager;
import com.kade_c.hearth.MainActivity;
import com.kade_c.hearth.R;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Handles the name inputting on deck creation.
 * Lets the user input a name for their deck and creates deck file.
 */
public class DeckCreationName extends Fragment {

    View view;

    // Name inputted.
    private String name = "";

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

                // If input is valid, create deck.
                if (checkInput()) {
                    createDeck();
                    callDeckFragment();
                }
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
     * Returns false if input is invalid:
     * Empty, duplicate or non-alphanumerical.
     */
    private boolean checkInput() {
        if (name.isEmpty()) {
            Toast.makeText(getContext().getApplicationContext(),
                    "Please input a name", Toast.LENGTH_LONG)
                    .show();
            return false;
        } else {
            InternalFilesManager.DeckListFileManager DLFM = new InternalFilesManager(getContext(), getActivity()).new DeckListFileManager();

            // Checks if duplicate.
            if (DLFM.checkDuplicateName(name)) {
                Toast.makeText(getContext().getApplicationContext(),
                        "Name taken", Toast.LENGTH_LONG)
                        .show();
                return false;
            }

            // Checks if non-alphanumerical.
            String pattern = "[a-zA-Z0-9]+";
            if (!name.matches(pattern)) {
                Toast.makeText(getContext().getApplicationContext(),
                        "Invalid name", Toast.LENGTH_LONG)
                        .show();
                return false;
            }
        }
        return true;
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
            // Name of the file containing our deck list.
            String FILENAME = "Deck_Info";

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
