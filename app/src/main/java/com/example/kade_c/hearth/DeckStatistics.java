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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DeckStatistics extends Fragment {
    String FILENAME = "Deck_Info";
    View view;
    ArrayList<String> lines;
    private ListView mListView;
    boolean deletion = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.statistics_deck, container, false);

        saveDecksFromFile();
        displayDecks();
        handleDeckPress();

        Button createDeckButton = (Button) view.findViewById(R.id.newDeck_button);
        createDeckButton.setOnClickListener(new View.OnClickListener() {

            /**
             * When the user clicks on new deck, go to
             * deck creation fragment.
             */
            @Override
            public void onClick(View v) {
                Fragment fragment = new DeckCreationClass();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Button deleteDeckButton = (Button) view.findViewById(R.id.deleteDeck_button);
        deleteDeckButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Clicked when the user wants to delete a deck.
             */
            @Override
            public void onClick(View v) {
                if (deletion) {
                    Toast.makeText(getContext().getApplicationContext(),
                            "Deletion mode stopped", Toast.LENGTH_LONG)
                            .show();
                    deletion = false;
                } else {
                    Toast.makeText(getContext().getApplicationContext(),
                            "Selection deck to delete", Toast.LENGTH_LONG)
                            .show();
                    deletion = true;
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Deck Statistics");
    }

    // TODO: Find a cleaner way to delete line in our deck file.
    /**
     * Goes through our deck file and deletes the 'deck' line.
     * @param deck
     */
    public void deleteDeck(String deck)  {
        try {
            File inputFile = getContext().getFileStreamPath(FILENAME);
            FileOutputStream fos = getActivity().openFileOutput("temp_deck_list", Context.MODE_PRIVATE);
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String lineToRemove = deck;
            lineToRemove = lineToRemove.replace("\n", "");
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.equals(lineToRemove)) continue;
                fos.write(currentLine.getBytes());
                fos.write('\n');
            }
            reader.close();

            File tempFile = getContext().getFileStreamPath("temp_deck_list");
            FileOutputStream fileToUpdate = getActivity().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedReader writer = new BufferedReader(new FileReader(tempFile));
            while ((currentLine = writer.readLine()) != null) {
                // trim newline when comparing with lineToRemove
                String trimmedLine = currentLine.trim();
                if (trimmedLine.equals(lineToRemove)) continue;
                fileToUpdate.write(trimmedLine.getBytes());
                fileToUpdate.write('\n');
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: Work on stat display for decks.
    /**
     * Handles when a user presses on a deck.
     * If user has deletion mode on, delete deck and refresh deck list.
     * If not, display stats for said deck. (WIP)
     */
    public void handleDeckPress() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (deletion == true) {
                    deletion = false;
                    deleteDeck(mListView.getItemAtPosition(position).toString());
                    saveDecksFromFile();
                    displayDecks();
                } else {
                    Toast.makeText(getContext().getApplicationContext(),
                            "Display stats for deck number " + position, Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    public void displayDecks() {
        mListView = (ListView) view.findViewById(R.id.deckList);
        String[] deckList = new String[lines.size()];

        for (int i = 0; i < lines.size(); i++) {
            deckList[i] = lines.get(i);
        }

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, deckList);
        mListView.setAdapter(adapter);
    }

    /**
     * Reads from our deck list file and saves every line in
     * an ArrayList
     */
    public void saveDecksFromFile() {
        File file = getContext().getFileStreamPath(FILENAME);
        String line = "";
        lines = new ArrayList<>();
        byte[] buffer = new byte[4096];
        char c;
        int ret;

        try {
            // Checks if file exists
            if (file != null && file.exists()) {
                FileInputStream fos = getActivity().openFileInput(FILENAME);

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
}
