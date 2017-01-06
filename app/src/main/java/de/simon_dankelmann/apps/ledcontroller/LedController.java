package de.simon_dankelmann.apps.ledcontroller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class LedController{

    private String sServerCommand = "";
    private String sServerIp = "127.0.0.1";
    private int iServerPort = 12345;

    public LedController(String sServerIp, Integer iServerPort){
        this.sServerIp = sServerIp;
        this.iServerPort = iServerPort;
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

    }

    public void switchOff(){
        this.sendCommand("OFF");
    }

    //SEND THE COLORVALUES TO OUR SERVER
    public void setLedColor(int iRed,int iGreen, int iBlue){
        // SEND RGB COLOR TO OUR LED-SERVER
        if(iRed > 255){iRed = 255;}
        if(iGreen > 255){iGreen = 255;}
        if(iBlue > 255){iBlue = 255;}

        if(iRed < 0){iRed = 0;}
        if(iGreen < 0){iGreen = 0;}
        if(iBlue < 0){iBlue = 0;}

        String sCommand = iRed + "," + iGreen + "," + iBlue;
        sendCommand(sCommand);
    }

    public void setEffect(String sEffectName){
        if(sEffectName == "COPS"){
           this.sendCommand(sEffectName);
        }

        if(sEffectName == "FADE"){
           this.sendCommand(sEffectName);
        }
    }

    public void stopEffects(){
        this.sendCommand("STOP");
    }

    private void sendToPort() throws IOException {
        new Thread(new Runnable()
        {
            @Override
            public void run() {
                try
                {
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

