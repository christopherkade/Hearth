package com.example.kade_c.hearth;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ConnectionHandler connectionHandler;

    Fragment home;
    Fragment searchCard;
    Fragment cardDisplayer;
    Fragment statistics_general;
    Fragment statistics_deck;
    Fragment about;

    /**
     * Entry point to our application.
     * Checks is our user is connected and initializes our ConnectionHandler.
     * Sets up our Navigation Drawer and displays our Home screen.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectionHandler = new ConnectionHandler(this);
        connectionHandler.checkConnection();

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Sets up the Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        displaySelectedScreen(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Checks if our Fragment is called for the first time,
     * if so, it instantiated it, if not, we save its previous state in order to use it.
     * @param itemId id of the item selected.
     * @return the fragment to be displayed.
     */
    private Fragment checkFragmentState(int itemId) {
        Fragment fragment = null;

        switch (itemId) {
            // HOME
            case R.id.nav_home:
                if (home == null) {
                    fragment = new Home();
                    home = fragment;
                } else
                    fragment = home;
                break;
            // SEARCH CARD
            case R.id.nav_searchCard:
                if (searchCard == null) {
                    fragment = new SearchCard();
                    searchCard = fragment;
                } else
                    fragment = searchCard;
                break;
            // CARD DISPLAYER
            case R.id.nav_cardDisplayer:
                if (cardDisplayer == null) {
                    fragment = new CardDisplayer();
                    cardDisplayer = fragment;
                } else
                    fragment = cardDisplayer;
                break;
            // STATS GENERAL
            case R.id.nav_stat_general:
                if (statistics_general == null) {
                    fragment = new GeneralStatistics();
                    statistics_general = fragment;
                } else
                    fragment = statistics_general;
                break;
            // STATS DECK
            case R.id.nav_stat_deck:
                if (statistics_deck == null) {
                    fragment = new DeckStatistics();
                    statistics_deck = fragment;
                } else
                    fragment = statistics_deck;
                break;
            // ABOUT
            case R.id.nav_about:
                if (about == null) {
                    fragment = new About();
                    about = fragment;
                } else
                    fragment = about;
                break;
        }
        return fragment;
    }

    /**
     * Checks the selected fragments state and launches it.
     */
    private void displaySelectedScreen(int itemId) {
        Fragment fragment;
        fragment = checkFragmentState(itemId);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Close our Drawer since we have selected a valid item.
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    /**
     * If our app is resumed after a pause, we bind our connection checker.
     */
    @Override
    protected void onResume() {
        super.onResume();
        connectionHandler.bind();
    }

    /**
     * If our app is paused (screen lock etc.) we unbind our connection checker.
     */
    @Override
    protected void onPause() {
        connectionHandler.unbind();
        super.onPause();
    }
}
