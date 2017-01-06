package de.simon_dankelmann.apps.ledcontroller;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.io.File;
import java.util.Set;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainControllerFragment.OnFragmentInteractionListener,
        PresetsFragment.OnFragmentInteractionListener,
        EffectsFragment.OnFragmentInteractionListener,
        LedServerFragment.OnFragmentInteractionListener{

    private SettingsManager settings = new SettingsManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /* DISPLAY MAINCONTROLLER ON STARTUP */
        Class fragmentClass = MainControllerFragment.class;
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        /* END DISPLAY MAINCONTROLLER ON STARTUP */

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_main_controller) {
            fragmentClass = MainControllerFragment.class;
        } else if (id == R.id.nav_presets) {
            fragmentClass = PresetsFragment.class;
        } else if(id == R.id.nav_effects){
            fragmentClass = EffectsFragment.class;
        } else if (id == R.id.nav_led_servers) {
            fragmentClass = LedServerFragment.class;
        } else if (id == R.id.nav_share) {
            // SHARE INTENT
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            //share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, "LED Controller APP");
            share.putExtra(Intent.EXTRA_TEXT, "https://github.com/simondankelmann/LED_Controller");
            startActivity(Intent.createChooser(share, "Share App"));

        } else if (id == R.id.nav_send) {
            // SEND MAIL INTENT
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse("mailto:apps@simon-dankelmann.de"));
            intent.setType("text/html");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"apps@simon-dankelmann.de"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "LED Controller Contact");
            intent.putExtra(Intent.EXTRA_TEXT, "Leave me a Message");
            startActivity(Intent.createChooser(intent, "Send Email"));
        } else if(id == R.id.nav_exit){
            boolean bSwitchOff = settings.getBoolean("PREF_TURNOFFONEXIT", true);
            if(bSwitchOff){
                String sServerIp = settings.getString("PREF_SERVER_IP","");
                int iPort = settings.getInt("PREF_SERVER_PORT", 0);
                LedController lc = new LedController(sServerIp,iPort);
                lc.switchOff();
            }
            finish();
        }

        if(fragmentClass != null){
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void  onFragmentInteraction(Uri uri){
        //We can keep this empty
    }







}
