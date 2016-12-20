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
import com.example.kade_c.hearth.fragments.About;
import com.example.kade_c.hearth.fragments.CardDisplayer;
import com.example.kade_c.hearth.fragments.statistics_fragments.DeckStatistics;
import com.example.kade_c.hearth.fragments.statistics_fragments.GeneralStatistics;
import com.example.kade_c.hearth.fragments.Home;
import com.example.kade_c.hearth.fragments.SearchCard;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DrawerLocker.DrawerLockerItf {

    // Merlin class that handles internet monitoring.
    ConnectionHandler connectionHandler;

    // Our Navigation Drawer.
    DrawerLayout drawer;

    // Our toggler for the Navigation Drawer.
    ActionBarDrawerToggle toggle;

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
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

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
     * @param itemId id of the item selected.
     * @return the fragment to be displayed.
     */
    private Fragment checkFragmentState(int itemId) {
        Fragment fragment = null;

        switch (itemId) {
            // HOME
            case R.id.nav_home:
                fragment = new Home();
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
                fragment = new GeneralStatistics();
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

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Close our Drawer since we have selected a valid item.
        drawer.closeDrawer(GravityCompat.START);
    }

    public void replaceFragment(final Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    /**
     * Switches the current fragment with the one passed as parameter.
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
}
