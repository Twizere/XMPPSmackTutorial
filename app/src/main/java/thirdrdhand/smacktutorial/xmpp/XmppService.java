package thirdrdhand.smacktutorial.xmpp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import thirdrdhand.smacktutorial.Storage.BackEndDB;
import thirdrdhand.smacktutorial.activities.MainActivity;
import thirdrdhand.smacktutorial.constants.TYPES;
import thirdrdhand.smacktutorial.xmpp.listeners.XmppConnection;

/**
 * Created by pacit on 2017/08/14.
 */

public class XmppService extends Service {
    private static final String TAG ="XmppService";
    public static String loginUsername = "";
    public static String loginPassword = "";
    public static XmppConnection mConnection;
    private static Handler handler = new Handler();
    private boolean mActive;//Stores whether or not the thread is active
    private Thread mThread;
    private Handler mTHandler;//We use this handler to post messages to

    //the background thread.

    public XmppService(){


    }

    public static TYPES.ConnectionState getState(){

        if (mConnection == null) {
            return  TYPES.ConnectionState.DISCONNECTED;

        } else if (mConnection.mConnectionStatus == null)
            return TYPES.ConnectionState.DISCONNECTED;

        return mConnection.mConnectionStatus;
    }

    public static TYPES.LogInState getLoggedInState(){
        if (mConnection == null) {
            return TYPES.LogInState.LOGGED_OUT;

        } else if (mConnection.mLoginState == null) {
            return TYPES.LogInState.LOGGED_OUT;

        }
        return mConnection.mLoginState;
    }

    public static Context getContext() {
        try {
            if (mConnection == null) throw new Exception("Connection is Null");
            else if (mConnection.mApplicationContext == null)
                throw new Exception("Context is Null");

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());

        }
        return mConnection.mApplicationContext;
    }

    public static void launchMainActivity() {

        if (MainActivity.instance != null)
            MainActivity.instance.finish();
        Intent mainActivityIntent = new Intent();
        mainActivityIntent.setClass(getContext(), MainActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(mainActivityIntent);

    }

    public static void broadcastDelayed(final Intent i, int delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                XmppService.mConnection.mApplicationContext.sendBroadcast(i);
            }
        }, delay);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void start(){
        Log.w(TAG, "Service Started");
        if(!mActive){
            mActive=true;

            if(mThread==null || !mThread.isAlive()){
                mThread= new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        initConnection();
                        initDB();
                        mTHandler= new Handler();
                        Looper.loop();
                    }
                });
                mThread.start();

            }

        }

    }

    private void initDB() {
        BackEndDB backEndDb = new BackEndDB(getContext());

    }

    public void initConnection() {

        Log.w(TAG, "initConnection");

    if(mConnection==null){

        mConnection= new XmppConnection(this);
    }

    try {
        mConnection.connect(loginUsername, loginPassword);
    }catch (Exception e){
        Log.w(TAG, "Something went wrong while connecting ,make sure the credentials are right and try again");
        e.printStackTrace();
        mConnection.connectionFailure();
        //Stop the service all together.
        // stopSelf();
    }
}

    public void stop(){
        Log.w(TAG, "Stopped");
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
        Log.w(TAG, "onStartCommand");
        start();

        return Service.START_STICKY;

    }

    @Override
    public void onDestroy() {
        Log.w(TAG, "onDestroy()");
        super.onDestroy();
        stop();
    }
}
