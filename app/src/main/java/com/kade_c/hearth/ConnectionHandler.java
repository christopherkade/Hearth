package com.kade_c.hearth;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

/**
 * Handles connections via Merlin.
 */
class ConnectionHandler {

    private Merlin merlin;
    private Context context;
    private AlertDialog alertDialog;

    /**
     * Sets up our Merlin and creates a Connection Listener and Disconnection Listener
     * in order to catch the gain/loss of connection and monitor the use of our app.
     */
    ConnectionHandler(Context ctx) {
        this.context = ctx;
        merlin = new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .build(this.context);

        merlin.registerConnectable(new Connectable() {
            @Override
            public void onConnect() {
                checkConnection();
            }
        });

        merlin.registerDisconnectable(new Disconnectable() {
            @Override
            public void onDisconnect() {
                checkConnection();
            }
        });
    }

    /**
     * If user is NOT connected to the internet, we display an alert message that blocks user interaction.
     * Once connection is established, we dismiss the message.
     */
    void checkConnection() {
        if (isConnected()) {
            try {
                if (alertDialog != null && alertDialog.isShowing())
                    alertDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("No Internet connection !");
            builder.setMessage("Please check your connection in order to use Hearth.");
            builder.setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();
        }
    }

    /**
     * Returns true if the user has access to the internet.
     */
    private boolean isConnected() {
        MerlinsBeard merlinsBeard = MerlinsBeard.from(this.context);

        return merlinsBeard.isConnected();
    }

    void bind() {
        merlin.bind();
    }

    void unbind() {
        merlin.unbind();
    }

}
