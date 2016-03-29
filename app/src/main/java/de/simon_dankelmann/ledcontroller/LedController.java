package de.simon_dankelmann.ledcontroller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.widget.Switch;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class LedController{

    private SharedPreferences sharedPreferences;
    private String sServerCommand = "";
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
        setLedColor(0, 0, 0);
    }

    //SEND THE COLORVALUES TO OUR SERVER
    public void setLedColor(int iRed,int iGreen, int iBlue){
        // SEND RGB COLOR TO OUR LED-SERVER
        String sCommand = iRed + "," + iGreen + "," + iBlue;
        sendCommand(sCommand);
    }

    private void sendToPort() throws IOException {
        new Thread(new Runnable()
        {
            @Override
            public void run() {
                try
                {
                    String sServerIp = sharedPreferences.getString("PREF_SERVER_IP", "");
                    Integer iServerPort = NumberUtils.toInt(sharedPreferences.getString("PREF_SERVER_PORT", ""), 0);
                    if (!sServerIp.isEmpty() && iServerPort != 0)
                    {
                        Socket socket;
                        socket = new Socket(sServerIp, iServerPort);
                        PrintWriter printwriter = new PrintWriter(socket.getOutputStream(),true);
                        printwriter.write(sServerCommand);
                        printwriter.flush();
                        printwriter.close();
                        socket.close();
                    }
                }
                catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendCommand(String command){
        sServerCommand = command;
        try {
            sendToPort();
            //Snackbar.make(parentActivity.findViewById(R.id.pickerTab), "SENT: " + command, Snackbar.LENGTH_SHORT).show();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}

