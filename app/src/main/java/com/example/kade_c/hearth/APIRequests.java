package com.example.kade_c.hearth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Contain the API request methods.
 * - Specific card research
 * - Card research by class
 * - Game info
 */
public class APIRequests {

    /**
     * Formats the card inputted like such:
     * WordOne%20WordTwo%20Word3 to be concatenated to the URL
     */
    private String formatCardName(String[] cardArray) {
        String formattedCard = "";
        int i;

        i = 0;
        for (String word : cardArray) {
            formattedCard += word;
            if (i < cardArray.length - 1) {
                formattedCard += "%20";
            }
            i++;
        }
        return formattedCard;
    }

    /**
     * Does a HTTP GET request to the Hearthstone API with the card stipulated.
     * @return a JSONObject containing information on the card inputted.
     */
    public JSONObject getCardInfo(String card) {
        String[] cardArray;
        String formattedCard;
        String urlStr;
        URL url;

        try {
            // Split card name in an array
            cardArray = card.split(" ");

            // Formats a string for the URL if the card name is composed of 2+ words.
            formattedCard = formatCardName(cardArray);

            urlStr = "https://omgvamp-hearthstone-v1.p.mashape.com/cards/" + formattedCard;
            url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set Headers
            conn.setRequestProperty("X-Mashape-Key", "pZG12ehuEamshSZzv230YsYrdKG3p1RT7iOjsnYgJS1L0xaxS5");
            conn.setRequestProperty("Accept", "application/json");

            // Request not successful
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            // Read response
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer jsonString = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            conn.disconnect();

            // Save response as JSONObject
            JSONArray jsonArray = new JSONArray(jsonString.toString());
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);

            return jsonObject;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Does a HTTP GET request to the Hearthstone API with the type stipulated:
     * Common, Mage, Hunter etc.
     * @param classSelected class selected in the Spinner
     * @return a JSONArray containing the cards of said class
     */
    public JSONArray getClassCards(String classSelected, Integer cost) {
        String urlStr;
        URL url;

        try {
            urlStr = "https://omgvamp-hearthstone-v1.p.mashape.com/cards/classes/" + classSelected + "?collectible=1";

            // Sets the cost filter parameter if one is stipulated in the Spinner
            if (cost != -1)
                urlStr += "&cost=" + cost;

            url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set Headers
            conn.setRequestProperty("X-Mashape-Key", "5jvSJewFIemshMXQyctYHfbH5PVyp1p1IzbjsnmWHZGJ93sTVE");

            // Request not successful
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            // Read response
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer jsonString = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            conn.disconnect();

            // Save response in JSONArray
            JSONArray jsonArray = new JSONArray(jsonString.toString());

            return jsonArray;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Does a HTTP GET request to the Hearthstone API to receive information about the
     * current state of the game.
     * @return a JSONObject containing information such as the current expansions,
     * patch, classes etc.
     */
    public JSONObject getGameInfo() {
        String urlStr;
        URL url;

        try {
            urlStr = "https://omgvamp-hearthstone-v1.p.mashape.com/info";

            url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set Headers
            conn.setRequestProperty("X-Mashape-Key", "pZG12ehuEamshSZzv230YsYrdKG3p1RT7iOjsnYgJS1L0xaxS5");
            conn.setRequestProperty("Accept", "application/json");

            // Request not successful
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            // Read response
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer jsonString = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            conn.disconnect();

            JSONObject jsonObject = new JSONObject(jsonString.toString());

            return jsonObject;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
