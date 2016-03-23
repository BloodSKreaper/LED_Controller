package de.simon_dankelmann.ledcontroller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Switch;


/**
 * Created by dankelmann on 23.03.16.
 */

public class LedController{

    private SharedPreferences sharedPreferences;
    public Activity parentActivity;

    public LedController(Activity a){
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(a);
        parentActivity = a;
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

        Switch onOffSwitch = (Switch)parentActivity.findViewById(R.id.onOffSwitchTab);
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
        Snackbar.make(parentActivity.findViewById(R.id.pickerTab), sToastMessage, Snackbar.LENGTH_SHORT).show();
    }
}
