package com.kade_c.hearth;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kade_c.hearth.fragments.About;
import com.kade_c.hearth.fragments.CardDisplayer;
import com.kade_c.hearth.fragments.statistics_fragments.DeckStatistics;
import com.kade_c.hearth.fragments.statistics_fragments.GeneralStatisticsVP;
import com.kade_c.hearth.fragments.Home;
import com.kade_c.hearth.fragments.SearchCard;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DrawerLocker.DrawerLockerItf {

    // Merlin class that handles internet monitoring.
    ConnectionHandler connectionHandler;

    // Our Navigation Drawer.
    DrawerLayout drawer;

    // Our toggler for the Navigation Drawer.
    ActionBarDrawerToggle toggle;

    private NavigationView navigationView;

    private Fragment HomeFragment;

    boolean change;

    /**
     * Entry point to our application.
     * Checks is our user is connected and initializes our ConnectionHandler.
     * Sets up our Navigation Drawer and displays our Home screen.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handles internet availability checking
        connectionHandler = new ConnectionHandler(this);
        connectionHandler.checkConnection();

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Sets up the Navigation Drawer.
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Sets the 'Home' tab as selected (by default).
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

        // Display home Fragment.
        displaySelectedScreen(R.id.nav_home);
    }

    /**
     * Deactivated 'back' button press when drawer is closed.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // Uncomment for 'back' button normal functionality.
            // super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Checks which item has been selected and instantiates the corresponding Fragment.
     * Home will not be instantiated twice in order to avoid useless API requests.
     * @param itemId id of the item selected.
     * @return the fragment to be displayed.
     */
    private Fragment checkFragmentState(int itemId) {
        Fragment fragment = null;

        switch (itemId) {
            // HOME
            case R.id.nav_home:
                if (HomeFragment == null) {
                    fragment = new Home();
                    HomeFragment = fragment;
                } else {
                    fragment = HomeFragment;
                }
                break;
            // SEARCH CARD
            case R.id.nav_searchCard:
                fragment = new SearchCard();
                break;
            // CARD DISPLAYER
            case R.id.nav_cardDisplayer:
                fragment = new CardDisplayer();
                break;
            // STATS GENERAL
            case R.id.nav_stat_general:
                fragment = new GeneralStatisticsVP();
                break;
            // STATS DECK
            case R.id.nav_stat_deck:
                fragment = new DeckStatistics();
                break;
            // ABOUT
            case R.id.nav_about:
                fragment = new About();
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

        if (fragment != null)
            this.replaceFragment(fragment);
    }

    /**
     * Replaces the current fragment and closes the Drawer.
     * @param fragment
     */
    public void replaceFragment(final Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment, fragment.getClass().getSimpleName())
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Close our Drawer since we have selected a valid item.
        drawer.closeDrawer(GravityCompat.START);
    }

    /**
     * Adds a new fragment passed as parameter.
     * @param fragment
     */
    public void addFragment(final Fragment fragment) {
        final Fragment hideFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_frame, fragment, fragment.getClass().getSimpleName())
                .hide(hideFragment)
                .addToBackStack(hideFragment.getClass().getSimpleName())
                .commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        displaySelectedScreen(item.getItemId());
        drawer.closeDrawer(GravityCompat.START);

        if (item.getItemId() != R.id.nav_home)
            navigationView.getMenu().findItem(R.id.nav_home).setChecked(false);
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

    /**
     * Method that sets the Navigation Drawer visible or non-visible.
     * Used in certain fragments (e.g: Deck creation) for better UX.
     * @param enabled
     */
    @Override
    public void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawer.setDrawerLockMode(lockMode);
        toggle.setDrawerIndicatorEnabled(enabled);
    }

    /**
     * Is called in Fragments that should hide the button to access the Nav. Drawer.
     */
    public void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();

        }
        else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSupportNavigateUp();
                }
            });
            toggle.syncState();
        }
    }
}
