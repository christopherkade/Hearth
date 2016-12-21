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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Handles the Deck Statistics Fragment
 * Lets the user create and delete decks and click on them in order to display statistics.
 */
public class DeckStatistics extends Fragment {

    View view;

    // Our file's lines
    private ArrayList<String> lines;

    // Our list of decks
    private ListView mListView;

    private String[] deckClasses;
    private String[] deckNames;

    // True when the user has pressed the deletion mode button
    private boolean deletion = false;

    // Our Internal File Manager that handles reading and writing in our Internal files.
    private InternalFilesManager.DeckListFileManager DLFM;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.statistics_deck, container, false);

        DLFM = new InternalFilesManager(getContext(), getActivity()). new DeckListFileManager();


        // Sets the Drawer as enabled.
        ((MainActivity) getActivity()).setDrawerEnabled(true);

        // Gets decks from file.
        lines = DLFM.getDecksFromFile();

        // Refresh deck list.
        refreshDeckList();

        // Handles if user clicks on a deck in the list.
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
                    String fileToDelete = deckClasses[position] + " | " + deckNames[position];
                    lines = DLFM.deleteDeckFromList(fileToDelete, position);
                    refreshDeckList();
                } else {
                    openDeckStatsFragment(deckNames[position], deckClasses[position]);
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
     * Sets the decks in our deck file in the ListView.
     */
    private void refreshDeckList() {
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
}
