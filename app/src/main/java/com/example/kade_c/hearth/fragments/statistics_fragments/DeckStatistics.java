package com.example.kade_c.hearth.fragments.statistics_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.example.kade_c.hearth.MainActivity;
import com.example.kade_c.hearth.R;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Handles the Deck Statistics Fragment
 * Lets the user create and delete decks and click on them in order to display statistics.
 */
public class DeckStatistics extends Fragment {

    View view;

    // Name of our deck list file
    private String FILENAME = "Deck_Info";

    // Our file's lines
    private ArrayList<String> lines;

    // Our list of decks
    private ListView mListView;

    private String[] deckClasses;
    private String[] deckNames;

    // True when the user has pressed the deletion mode button
    private boolean deletion = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.statistics_deck, container, false);

        // Sets the Drawer as enabled.
        ((MainActivity) getActivity()).setDrawerEnabled(true);

        // Gets decks from file.
        saveDecksFromFile();

        // Displays them in the ListView.
        displayDecks();

        // Handles the selection of a deck by calling a Stat display fragment.
        handleDeckPress();

        // When the user presses the '+' (Deck creation) button, call the DeckCreationClassSelector Fragment.
        Button createDeckButton = (Button) view.findViewById(R.id.newDeck_button);
        createDeckButton.setOnClickListener(new View.OnClickListener() {

            /**
             * When the user clicks on new deck, go to
             * deck creation fragment.
             */
            @Override
            public void onClick(View v) {
                final Fragment homeFragment = new DeckCreationClassSelector();
                ((MainActivity)getActivity()).addFragment(homeFragment);
            }
        });

        // Lets the user activate deck deletion with the '-' button.
        Button deleteDeckButton = (Button) view.findViewById(R.id.deleteDeck_button);
        deleteDeckButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Clicked when the user wants to delete a deck.
             */
            @Override
            public void onClick(View v) {
                if (deletion) {
                    Toast.makeText(getContext().getApplicationContext(),
                            "Deletion OFF", Toast.LENGTH_LONG)
                            .show();
                    deletion = false;
                } else {
                    Toast.makeText(getContext().getApplicationContext(),
                            "Deletion ON", Toast.LENGTH_LONG)
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

    /**
     * Hides keyboard when going back to our Stat Fragment.
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    /**
     * Goes through our deck file and deletes the 'deck' line.
     * @param deck
     */
    private void deleteDeck(String deck, int lineToDelete)  {
        try {
            // Reads file and saves file without deck to be deleted in temporary file.
            File deckInfoFile = getContext().getFileStreamPath(FILENAME);
            FileOutputStream tempFile = getActivity().openFileOutput("temp_deck_list", Context.MODE_PRIVATE);
            BufferedReader reader = new BufferedReader(new FileReader(deckInfoFile));
            String currentLine;
            int i = -1;

            while ((currentLine = reader.readLine()) != null) {
                i++;
                if (i == lineToDelete) continue;
                tempFile.write(currentLine.getBytes());
                tempFile.write('\n');
            }
            reader.close();

            // Then rewrites the temp file in our deck file.
            File tempFile2 = getContext().getFileStreamPath("temp_deck_list");
            FileOutputStream fileToUpdate = getActivity().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedReader tempFileReader = new BufferedReader(new FileReader(tempFile2));

            while ((currentLine = tempFileReader.readLine()) != null) {
                fileToUpdate.write(currentLine.getBytes());
                fileToUpdate.write('\n');
            }
            tempFileReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        deleteFile(deck);
    }

    /**
     * If a stat file exists, delete it.
     * Is called when the user decides to delete a deck.
     */
    private void deleteFile(String fileToDelete) {
        File dir = getActivity().getFilesDir();
        File file = new File(dir, fileToDelete);
        boolean deleted = file.delete();
    }

    /**
     * Handles when a user presses on a deck.
     * If user has deletion mode on, delete deck and refresh deck list.
     * If not, display stats for said deck. (WIP)
     */
    private void handleDeckPress() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (deletion == true) {
                    deletion = false;
                    deleteDeck(mListView.getItemAtPosition(position).toString(), position);
                    saveDecksFromFile();
                    displayDecks();
                } else {
                    openDeckStatsFragment(deckNames[position], deckClasses[position]); // mListView.getItemAtPosition(position).toString()
                }
            }
        });
    }

    /**
     * Opens the fragment for the deck that has been clicked on.
     */
    private void openDeckStatsFragment(String deck, String deckClass) {
        final Fragment homeFragment = new DeckSelectedStatistics();
        final Bundle bundle = new Bundle();
        bundle.putString("deckName", deck);
        bundle.putString("deckClass", deckClass);
        homeFragment.setArguments(bundle);
        ((MainActivity)getActivity()).addFragment(homeFragment);
    }

    /**
     * Displays class icons and deck names in our custom ListView.
     */
    private void displayDecks() {
        mListView = (ListView) view.findViewById(R.id.deckList);

        // Each row in the list stores Deck icon ID and deck name.
        List<HashMap<String,String>> aList = new ArrayList<>();

        // Stores our list of deck names and associated classes.
        deckClasses = new String[lines.size()];
        deckNames = new String[lines.size()];

        for (int i = 0; i < lines.size(); i++) {
            HashMap<String, String> hm = new HashMap<>();

            String[] split = lines.get(i).split(" \\| ");
            deckClasses[i] = split[0];
            deckNames[i] = split[1];

            // Find the right icon for the decks class.
            Integer resourceId = getActivity().getResources().getIdentifier(deckClasses[i].toLowerCase(), "mipmap", getActivity().getPackageName());
            hm.put("Class", resourceId.toString());
            hm.put("Deck", deckNames[i]);
            aList.add(hm);
        }

        // Keys used in HashMap.
        String[] from = { "Class","Deck" };

        // IDs of views in our layout.
        int[] to = { R.id.ivClass,R.id.tvName };

        // Instantiating an adapter to store each items
        // R.layout.deck_list defines the layout of each item.
        SimpleAdapter adapter = new SimpleAdapter(view.getContext(), aList, R.layout.deck_list, from, to);

        mListView.setAdapter(adapter);
    }

    /**
     * Reads from our deck list file and saves every line in
     * an ArrayList
     */
    private void saveDecksFromFile() {
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
