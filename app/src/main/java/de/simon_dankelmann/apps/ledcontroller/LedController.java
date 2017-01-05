package de.simon_dankelmann.apps.ledcontroller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;

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
    private Timer oEffectTimer = new Timer();


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
        setLedColor(0, 0, 0);
    }

    //SEND THE COLORVALUES TO OUR SERVER
    public void setLedColor(int iRed,int iGreen, int iBlue){
        // SEND RGB COLOR TO OUR LED-SERVER
        String sCommand = iRed + "," + iGreen + "," + iBlue;
        sendCommand(sCommand);
    }

    public void setEffect(String sEffectName){

        if(sEffectName == "COPS"){
            CopEffectTimerTask copEffectTask = new CopEffectTimerTask();
            copEffectTask.setLedController(this);
            oEffectTimer.schedule(copEffectTask, 0, 750);
        }
    }

    public void stopEffects(){
        oEffectTimer.cancel();
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



    class CopEffectTimerTask extends TimerTask {
        private boolean bBufferCopEffect = true;
        private LedController ledController;

        public void setLedController(LedController oLedController){
            this.ledController = oLedController;
        }
        public void run() {
            if(this.bBufferCopEffect == true){
                this.ledController.setLedColor(255,0,0);
            } else {
                this.ledController.setLedColor(0,0,255);
            }
            this.bBufferCopEffect = !bBufferCopEffect;
        }
    }



}

