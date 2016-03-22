package de.simon_dankelmann.ledcontroller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

// IMPORTING THE COLORPICKER
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // GET SETTINGS
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

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

        // CONNECT COLORPICKER AND OPACITYBAR
        ColorPicker picker = (ColorPicker) findViewById(R.id.picker);
        OpacityBar opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
        picker.addOpacityBar(opacityBar);

        //SET COLORPICKER LISTENER
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                changeColor(color);
            }
        });

        Switch onOffSwitch = (Switch)findViewById(R.id.onOffSwitch);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ColorPicker picker = (ColorPicker) findViewById(R.id.picker);
                    changeColor(picker.getColor());
                }else{
                    switchOff();
                }
            }
        });
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // CLICKHANDLES ON NAVIGATIONITEMS WILL BE HANDLED HERE
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            // OPEN SETTINGS ACTIVITY
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_exit) {
            // SWITCH OFF LED'S IF NOT DEATIVATED
            boolean bSwitchOff = sharedPreferences.getBoolean("PREF_TURNOFFONEXIT", true);
            if(bSwitchOff){
                switchOff();
            }
            // EXIT APP
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // GET RGBA VALUES OF SELECTED COLOR
    public void changeColor(int color){

        // SPLIT COLORVALUE (INT) INTO R;G;B;A INTEGERS
        int iRed   = Color.red(color);
        int iGreen = Color.green(color);
        int iBlue  = Color.blue(color);
        int iAlpha = Color.alpha(color);

        // USE ALPHA TO LOWER THE INTENSITY
        int iReduce = 255 - iAlpha;

        // REDUCE INTENSITY BY DETECTED VALUE
        iRed = iRed - iReduce;
        iGreen = iGreen - iReduce;
        iBlue = iBlue - iReduce;

        // COLORS CANT BE NEGATIVE
        if(iRed < 0){iRed = 0;}
        if(iGreen < 0){iGreen = 0;}
        if(iBlue < 0){iBlue = 0;}

        setLedColor(iRed, iGreen, iBlue);

        Switch onOffSwitch = (Switch)findViewById(R.id.onOffSwitch);
        if(!onOffSwitch.isChecked()){
            onOffSwitch.setChecked(true);
        }
    }

    public void switchOff(){
        setLedColor(0,0,0);
    }

    //SEND THE COLORVALUES TO OUR SERVER
    public void setLedColor(int iRed,int iGreen, int iBlue){
        String sServerIp = sharedPreferences.getString("PREF_SERVER_IP", "");
        String sServerPort = sharedPreferences.getString("PREF_SERVER_PORT", "");
        // SEND RGB COLOR TO OUR LED-SERVER
        String sToastMessage = "R: " + iRed + " G: " + iGreen + " B: " + iBlue;
        Snackbar.make(findViewById(R.id.picker),sToastMessage,Snackbar.LENGTH_SHORT).show();
    }
}

