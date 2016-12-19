package com.example.kade_c.hearth;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileOutputStream;
import java.io.IOException;

public class DeckCreationName extends Fragment {
    View view;

    String FILENAME = "Deck_Info";
    String name;
    String classSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.deck_creation_name, container, false);
        classSelected = getArguments().getString("classSelected");

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
     * Create file containing the newly created deck
     * Adds a "class | deck_name" line in our deck info file.
     */
    public void createDeck() {
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

    public void callDeckFragment() {
        // Call back Deck stat fragment
        Fragment fragment = new DeckStatistics();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Set argument
        Bundle args = new Bundle();
        args.putString("classSelected", classSelected);
        args.putString("name", name);
        fragment.setArguments(args);

        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
