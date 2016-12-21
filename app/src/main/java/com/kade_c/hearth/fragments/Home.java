package com.kade_c.hearth.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kade_c.hearth.APIRequests;
import com.kade_c.hearth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;

/**
 * Handles the Home tab that displays simple information
 * about the game and is an entry point to the app.
 */
public class Home extends Fragment {

    View view;

    private String xpac = "";
    private String patch = "";

    TextView xpacValue;
    TextView patchValue;

    // true when API Request has already been done for this session.
    boolean requestDone = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.home, container, false);

        xpacValue = (TextView) view.findViewById(R.id.xpac_value);
        patchValue = (TextView) view.findViewById(R.id.patch_value);

        // Checks if we have already done an API request.
        // Gets expansion name and patch.
        if (!requestDone) {
            getGameInfo();
        }

        // Sets information
        displayGameInfo();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");
    }

    /**
     * Sets values to their TextViews
     */
    private void displayGameInfo() {
        xpacValue.setText(xpac);
        patchValue.setText(patch);
    }

    /**
     * Saves the current expansion and patch
     */
    private void getGameInfo() {
        SearchInfo searchInfo = new SearchInfo();
        JSONArray standardJSON;

        try {
            JSONObject info = searchInfo.execute().get();
            if (info != null) {
                standardJSON = (JSONArray) info.get("standard");
                int len = standardJSON.length() - 1;

                // Get the xpac value
                xpac = standardJSON.getString(len);

                // Get the patch value
                patch = (String) info.get("patch");
            }
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests information from the API via APIRequests
     * Extends AsyncTask in order to do network-related actions in background.
     */
    private class SearchInfo extends AsyncTask<Void, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject information;

            APIRequests apiRequests = new APIRequests();
            information = apiRequests.getGameInfo();

            requestDone = true;

            return information;
        }
    }
}

