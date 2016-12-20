package com.example.kade_c.hearth.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kade_c.hearth.APIRequests;
import com.example.kade_c.hearth.R;

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
    JSONObject info;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.home, container, false);

        handleGameInformation();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");
    }

    /**
     * Saves the current expansion and patch and displays them.
     */
    private void handleGameInformation() {
        SearchInfo searchInfo = new SearchInfo();
        JSONArray standardJSON;

        // Standard expansions
        String[] standard;

        try {
            info = searchInfo.execute().get();
            if (info != null) {
                standardJSON = (JSONArray) info.get("standard");
                standard = new String[standardJSON.length()];

                // Get the xpac value
                for (int i = 0; i < standardJSON.length(); i++) {
                    standard[i] = standardJSON.getString(i);
                    if (i + 1 == standardJSON.length()) {
                        xpac = standard[i];
                    }
                }

                // Get the patch value
                patch = (String) info.get("patch");

                // Sets information
                TextView xpacValue = (TextView) view.findViewById(R.id.xpac_value);
                xpacValue.setText(xpac);
                TextView patchValue = (TextView) view.findViewById(R.id.patch_value);
                patchValue.setText(patch);
            }
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests information from the API via APIRequests
     * Extends AsyncTask in order to do network-related actions in background.
     */
    public class SearchInfo extends AsyncTask<Void, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject information;

            APIRequests apiRequests = new APIRequests();
            information = apiRequests.getGameInfo();

            return information;
        }
    }
}

