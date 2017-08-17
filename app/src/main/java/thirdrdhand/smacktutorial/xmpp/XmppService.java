package thirdrdhand.smacktutorial.xmpp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import thirdrdhand.smacktutorial.xmpp.constants.TYPES;
import thirdrdhand.smacktutorial.xmpp.listeners.XmppConnection;

/**
 * Created by pacit on 2017/08/14.
 */

public class XmppService extends Service {
    private static final String TAG ="XmppService";



    private boolean mActive;//Stores whether or not the thread is active
    private Thread mThread;
    private Handler mTHandler;//We use this handler to post messages to
    public static XmppConnection mConnection;
    private static TYPES.ConnectionState mConnectionState;
    private static TYPES.LogInState mLogInState;

    //the background thread.

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public XmppService(){


    }

    public static TYPES.ConnectionState getState(){

        if(mConnectionState == null){
            return  TYPES.ConnectionState.DISCONNECTED;

        }
        return mConnectionState;
    }
    public static TYPES.LogInState getLoggedInState(){

        if(mConnectionState == null){
            return TYPES.LogInState.LOGGED_OUT;

        }
        return mLogInState;
    }


    public void start(){
        Log.d(TAG,"Service Started");
        if(!mActive){
            mActive=true;

            if(mThread==null || !mThread.isAlive()){
                mThread= new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        initConnection();
                        mTHandler= new Handler();
                        Looper.loop();
                    }
                });
                mThread.start();

            }

        }

    }
private  void  initConnection(){

    Log.d(TAG,"initConnection");

    if(mConnection==null){

        mConnection= new XmppConnection(this);
    }

    try {
        mConnection.connect();
    }catch (Exception e){
        Log.d(TAG,"Something went wrong while connecting ,make sure the credentials are right and try again");
        e.printStackTrace();
        //Stop the service all together.
        stopSelf();
    }
}
    public void stop(){
        Log.d(TAG,"Stopped");
        mActive=false;
        mTHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mConnection!=null){
                    mConnection.disconnect();
                }
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        start();

        return Service.START_STICKY;

    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy()");
        super.onDestroy();
        stop();
    }
}
