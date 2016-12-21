package com.kade_c.hearth;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Contains methods that handle Internal files (deck list, deck stats etc.)
 */
public class InternalFilesManager {

    private Context context;
    private FragmentActivity fActivity;

    // Name of the file containing the decks created by the user.
    private final String DECK_LIST_FILE = "Deck_Info";


    public InternalFilesManager(Context ctx, FragmentActivity activity) {
        context = ctx;
        fActivity = activity;
    }

    /**
     * Handles reading / writing of the deck list file.
     */
    public class DeckListFileManager {
        /**
         * Reads from our deck list file and saves every line in
         * an ArrayList
         */
        public ArrayList<String> getDecksFromFile() {
            ArrayList<String> lines;
            File file = context.getFileStreamPath(DECK_LIST_FILE);
            String line = "";
            lines = new ArrayList<>();
            byte[] buffer = new byte[4096];
            char c;
            int ret;

            try {
                // Checks if file exists
                if (file != null && file.exists()) {
                    FileInputStream fos = fActivity.openFileInput(DECK_LIST_FILE);

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
            return lines;
        }

        /**
         * Deletes the line passed as parameter from the decks list file.
         * @param lineToDelete
         */
        private void deleteDeckFromFile(int lineToDelete) {
            try {
                // Reads file and saves file without deck to be deleted in temporary file.
                File deckInfoFile = context.getFileStreamPath(DECK_LIST_FILE);
                FileOutputStream tempFile = fActivity.openFileOutput("temp_deck_list", Context.MODE_PRIVATE);
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
                File tempFile2 = context.getFileStreamPath("temp_deck_list");
                FileOutputStream fileToUpdate = fActivity.openFileOutput(DECK_LIST_FILE, Context.MODE_PRIVATE);
                BufferedReader tempFileReader = new BufferedReader(new FileReader(tempFile2));

                while ((currentLine = tempFileReader.readLine()) != null) {
                    fileToUpdate.write(currentLine.getBytes());
                    fileToUpdate.write('\n');
                }
                tempFileReader.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Deletes the deck-specific Stat file
         * @param deckFile file to delete
         */
        private void deleteDeckFile(String deckFile) {
            File dir = fActivity.getFilesDir();
            File file = new File(dir, deckFile);
            boolean deleted = file.delete();
        }

        /**
         * Deletes the deck from deck file and its associated stat file
         * and returns an ArrayList<String> containing currently
         * available decks.
         */
        public ArrayList<String> deleteDeckFromList(String deck, int position) {
            deleteDeckFromFile(position);
            deleteDeckFile(deck);
            return getDecksFromFile();
        }
    }

    /**
     * Handles reading / writing of Deck-specific files.
     */
    public class DeckFileManager {

        public ArrayList<String> getDeckFileInformation(String deckClass, String deckName) {
            String fileName = deckClass + " | " + deckName;
            openStatisticsFile(fileName);
            return readStatisticsFile(fileName);
        }

        /**
         * Should create or open a file names after the deck and save it.
         */
        private void openStatisticsFile(String fileName) {
            try {
                FileOutputStream fos = fActivity.openFileOutput(fileName, Context.MODE_APPEND);
                File file = context.getFileStreamPath(fileName);
                if (file.length() == 0)
                    fos.write(fileName.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Open Deck specific stat file and reads its content.
         */
        public ArrayList<String> readStatisticsFile(String fileName) {
            ArrayList<String> lines = new ArrayList<>();
            File file = context.getFileStreamPath(fileName);
            String line = "";
            byte[] buffer = new byte[4096];
            char c;
            int ret;

            try {
                // Checks if file exists
                if (file != null && file.exists()) {
                    FileInputStream fos = fActivity.openFileInput(fileName);

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
            return lines;
        }

        /**
         * Writes the inputted information in our deck-specific file.
         * Line format:
         * [V/D] [Class]
         */
        public void writeInStatFile(String deckClass, String deckName,
                                     String type, String wonAgainst) {
            try {
                String fileName = deckClass + " | " + deckName;
                FileOutputStream fos = fActivity.openFileOutput(fileName, Context.MODE_APPEND);

                if (type.equals("V")) {
                    fos.write("V ".getBytes());
                } else if (type.equals("D")) {
                    fos.write("D ".getBytes());
                }
                fos.write(wonAgainst.getBytes());
                fos.write('\n');
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
