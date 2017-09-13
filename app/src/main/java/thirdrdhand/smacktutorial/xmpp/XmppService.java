package thirdrdhand.smacktutorial.xmpp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import thirdrdhand.smacktutorial.Storage.BackEndDB;
import thirdrdhand.smacktutorial.activities.MainActivity;
import thirdrdhand.smacktutorial.constants.KEYS;
import thirdrdhand.smacktutorial.constants.TYPES;
import thirdrdhand.smacktutorial.xmpp.listeners.XmppConnectionHolder;

/**
 * Created by pacit on 2017/08/14.
 */

public class XmppService extends Service {
    private static final String TAG ="XmppService";
    public static XmppConnectionHolder mConnection;
    private static Handler handler = new Handler();
    private static Context mApplicationContext;
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

        return mApplicationContext;
    }

    public static void setContext(Context context) {
        XmppService.mApplicationContext = context;
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
                XmppService.mApplicationContext.sendBroadcast(i);
            }
        }, delay);
    }

    public static void showMainActivity() {
        Intent i = new Intent(KEYS.BroadCast.UI_AUTHENTICATED);
        i.setPackage(mApplicationContext.getPackageName());
        mApplicationContext.sendBroadcast(i);
    }

    public static void broadCastXmppInfo(String broadcast) {
        Intent i = new Intent(broadcast);
        i.setPackage(mApplicationContext.getPackageName());
        mApplicationContext.sendBroadcast(i);
    }

    public static void MakeToast(String content) {
        Toast.makeText(mApplicationContext, content, Toast.LENGTH_SHORT).show();

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

        mConnection = new XmppConnectionHolder();
    }

    try {
        // mConnection.connect();
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
