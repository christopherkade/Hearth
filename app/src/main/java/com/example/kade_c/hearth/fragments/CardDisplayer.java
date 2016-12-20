package com.example.kade_c.hearth.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.kade_c.hearth.APIRequests;
import com.example.kade_c.hearth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


/**
 * Handles the CardDisplayer tab that does the following:
 * Displays a card, has 2 filter spinners, lets the user swipe in order
 * to go through the card list.
 */
public class CardDisplayer extends Fragment {

    View view;

    // Contains the cards obtained via the API.
    private JSONArray cardsArray;

    // Swipe recognition variables.
    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    // Incremented or decremented depending on the swipes.
    private int currentPage = 0;
    private String currentClass = "None";
    private int currentCost = -1;

    // Variables to check the value selected in our Class Spinner.
    static final int NONE = 0;
    static final int NEUTRAL = 1;
    static final int MAGE = 2;
    static final int HUNTER = 3;
    static final int PALADIN = 4;
    static final int WARRIOR = 5;
    static final int DRUID = 6;
    static final int WARLOCK = 7;
    static final int SHAMAN = 8;
    static final int PRIEST = 9;
    static final int ROGUE = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.card_displayer, container, false);

        handleSpinners();

        /**
         * Listener that catches swipes and displays the correct card.
         */
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (currentClass.equals("None")) {
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;

                        if (Math.abs(deltaX) > MIN_DISTANCE) {
                            // Left to Right swipe action
                            if (x2 > x1) {
                                if (cardsArray != null) {
                                    if (currentPage > 0) {
                                        currentPage--;
                                        displayImage(currentClass, 0, currentCost);
                                    }
                                }
                            }
                            // Right to left swipe action
                            else {
                                if (cardsArray != null) {
                                    if (currentPage < cardsArray.length() - 1) {
                                        currentPage++;
                                        displayImage(currentClass, 0, currentCost);
                                    }
                                }
                            }
                        }
                        break;
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Card Displayer");
    }

    /**
     * Displays the images.
     * Gets the correct URL, loads image in ImageView.
     */
    protected void displayImage(String selectedType, int firstTime, int cost) {
        try {
            ImageView i = (ImageView) view.findViewById(R.id.cardDisplay);

            currentClass = selectedType;
            currentCost = cost;

            // Checks if we have selected a valid class.
            if (selectedType.equals("None")) {
                i.setImageResource(R.mipmap.emptycard);
                return;
            }

            SearchClassCard searchClassCard = new SearchClassCard(selectedType);

            // Retrieve URL for the right class.
            String currentImageURL;

            currentImageURL = searchClassCard.execute(firstTime, currentPage, cost).get();

            // Use Glide to load and display image.
            Glide.with(getContext()).load(currentImageURL)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(i);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a String of the cost selected.
     */
    public String getSelectedCost(int position) {
        switch (position) {
            case 0:
                return "All";
            case 1:
                return "0";
            case 2:
                return "1";
            case 3:
                return "2";
            case 4:
                return "3";
            case 5:
                return "4";
            case 6:
                return "5";
            case 7:
                return "6";
            case 8:
                return "7";
            case 9:
                return "8";
            case 10:
                return "9";
            case 11:
                return "10";
            case 12:
                return "12";
        }
        return "";
    }

    /**
     * Returns a String of the class selected.
     */
    public String getSelectedType(int position) {
        switch (position) {
            case NONE:
                return "None";
            case NEUTRAL:
                return "Neutral";
            case MAGE:
                return "Mage";
            case HUNTER:
                return "Hunter";
            case PALADIN:
                return "Paladin";
            case WARRIOR:
                return "Warrior";
            case DRUID:
                return "Druid";
            case WARLOCK:
                return "Warlock";
            case SHAMAN:
                return "Shaman";
            case PRIEST:
                return "Priest";
            case ROGUE:
                return "Rogue";
        }
        return null;
    }

    /**
     * Sets up our spinners and their listeners.
     */
    public void handleSpinners() {
        Spinner classSpinner = (Spinner) view.findViewById(R.id.typeSpinner);
        Spinner costSpinner = (Spinner) view.findViewById(R.id.costSpinner);

        // Gets the array that will fill our listener in our resources and sets it.
        ArrayAdapter<CharSequence> classAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.type_array, android.R.layout.simple_spinner_item);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(classAdapter);

        ArrayAdapter<CharSequence> costAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.cost_array, android.R.layout.simple_spinner_item);
        costAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        costSpinner.setAdapter(costAdapter);

        /**
         * Spinner listener that detects the selection of an item.
         */
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedType;
                selectedType = getSelectedType(position);

                cardsArray = new JSONArray();
                displayImage(selectedType, 1, currentCost);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}

        });

        /**
         * Spinner listener that detects the selection of an item.
         */
        costSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String cost = getSelectedCost(position);
                String selectedType;
                selectedType = currentClass;

                cardsArray = new JSONArray();
                if (cost.equals("All")) {
                    displayImage(selectedType, 1, -1);
                } else {
                    displayImage(selectedType, 1, Integer.parseInt(cost));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    /**
     * This class requests data from the API methods (APIRequests class),
     * it then retrieves the URL we need and returns it.
     * This class extends AsyncTask in order to do network-related calls in background.
     */
    public class SearchClassCard extends AsyncTask<Integer, Integer, String> {
        String classSelected;

        /**
         * Constructor saves the class we are researching.
         */
        SearchClassCard(String classSelect) {
            this.classSelected = classSelect;
        }

        /**
         * Calls the appropriate method in our APIRequests class,
         * retrieves the URL and returns it.
         * @return the picture URL of the current card to be displayed.
         */
        @Override
        protected String doInBackground(Integer... param) {
            APIRequests apiRequests = new APIRequests();

            try {
                if (param[0] == 1) {
                    String currentImageURL = null;
                    currentPage = 0;

                    // Request a JSONArray containing the cards of wonAgainst.
                    cardsArray = apiRequests.getClassCards(this.classSelected, param[2]);

                    if (cardsArray == null) {
                        return null;
                    }

                    // Save first card of the array.
                    JSONObject firstCard = (JSONObject) cardsArray.get(currentPage);

                    // Get the image URL of the first card.
                    currentImageURL = (String) firstCard.get("img");

                    // And return it.
                    return currentImageURL;
                } else if (param[0] == 0) {
                    if (cardsArray == null) {
                        return null;
                    }

                    JSONObject card = (JSONObject) cardsArray.get(param[1]);

                    return (String) card.get("img");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
