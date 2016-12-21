package com.kade_c.hearth.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kade_c.hearth.APIRequests;
import com.kade_c.hearth.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;

public class SearchCard extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search, container, false);

        Button button = (Button) view.findViewById(R.id.searchButton);
        button.setOnClickListener(new View.OnClickListener() {
            /**
             * When clicked, launch the API card searching method AsyncTask method
             * and display the given URL with imageLoader.
             */
            @Override
            public void onClick(View v) {
                EditText searchedCard = (EditText) view.findViewById(R.id.searchField);

                // Get the inputted name as a String
                String searchedCardString = searchedCard.getText().toString();

                try {
                    Search searchCard = new Search(searchedCardString);

                    // Get the image URL
                    String currentImageURL = searchCard.execute().get();

                    // Display it
                    displayImage(currentImageURL);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Search Card");
    }

    private void displayImage(String currentImageURL) {
        ImageView i = (ImageView)view.findViewById(R.id.cardImage);

        Glide.with(getContext()).load(currentImageURL)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(getResources().getIdentifier("emptycard", "mipmap", "com.example.kade_c.hearth"))
                .into(i);
    }

    /**
     * Takes care of requesting data from the API in an asynchronous way. (AsyncTask)
     */
    private class Search extends AsyncTask<String, Integer, String> {
        // The card that we search via the API.
        String searchedCard;

        Search(String card) {
            this.searchedCard = card;
        }

        /**
         * Requests information from APIRequest and retrieves the card image URL from it.
         * @return URL of the img inputted.
         */
        @Override
        protected String doInBackground(String... params) {
            APIRequests apiRequests = new APIRequests();
            JSONObject json;

            try {
                String url = null;

                // Get JSON from API
                json = apiRequests.getCardInfo(searchedCard);
                if (json != null)
                    url = (String)json.get("img");

                return url;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
