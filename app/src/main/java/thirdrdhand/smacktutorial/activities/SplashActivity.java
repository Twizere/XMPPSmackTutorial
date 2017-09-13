package thirdrdhand.smacktutorial.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import thirdrdhand.smacktutorial.ApplicationOffice;
import thirdrdhand.smacktutorial.R;
import thirdrdhand.smacktutorial.activities.auth.LoginActivity;
import thirdrdhand.smacktutorial.constants.KEYS;
import thirdrdhand.smacktutorial.constants.TYPES;
import thirdrdhand.smacktutorial.smoothanimation.AnimatorPath;
import thirdrdhand.smacktutorial.smoothanimation.PathEvaluator;
import thirdrdhand.smacktutorial.smoothanimation.PathPoint;
import thirdrdhand.smacktutorial.xmpp.XmppService;
import thirdrdhand.smacktutorial.xmpp.callbacks.UserLoginCallBack;
import thirdrdhand.smacktutorial.xmpp.listeners.XmppConnectionHolder;

public class SplashActivity extends Activity implements UserLoginCallBack {
    private static final String TAG = "SPLASH_ACTIVITY";
    Handler handler;
    SoundPool soundPool;
    HashMap<Integer, Integer> soundPoolMap;
    int siID = 1;   //si
    int doID = 2;   //do
    TextView tvSiOut,tvSiIn,tvDoOut,tvDoIn, tvSendMoney,tvReceiveMoney,
            tvSIDO,tvMoneyTransfer;
    PathEvaluator mEvaluator = new PathEvaluator();
    private BroadcastReceiver mBroadcastReceiver;
    private int timer_count=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);
        //Initialization
        initView();
        //Starting the Service
        XmppService.setContext(getApplicationContext());
        Intent i1 = new Intent(this, XmppService.class);
        startService(i1);

        //Check Login
        if (XmppService.getLoggedInState() == TYPES.LogInState.LOGGED_IN) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        } else {
            //Loading Sounds
            initSound();
            //Initialise Handler or Timer
            initHandler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    checkLogin();
                }
            });


        }



    }



    private void checkLogin() {

        SharedPreferences prefs = ApplicationOffice.getPrefs(getApplicationContext());
        boolean rememberMe = prefs.getBoolean(KEYS.PREF.Auth.AUTO_LOGIN, false);
        String username = prefs.getString(KEYS.PREF.Auth.USERNAME, null);
        String password = prefs.getString(KEYS.PREF.Auth.PASSWORD, null);

        if (username == null || password == null) {
            openLogin();
        } else {
            if (!username.equals("") && !password.equals("")) {
                if (rememberMe)
                    XmppConnectionHolder.getInstance().Login(username, password, this);

            }
        }
    }
    private void initView() {

        tvDoIn= findViewById(R.id.tvSplash_do_inner);
        tvDoOut= findViewById(R.id.tvSplash_do);
        tvSiIn= findViewById(R.id.tvSplash_si_inner);
        tvSiOut= findViewById(R.id.tvSplash_si);

        //Money balls
        tvSendMoney= findViewById(R.id.tvSplash_sendMoney);
        tvReceiveMoney= findViewById(R.id.tvSplash_receiveMoney);

        //SIDO and MoneyTransfer text
        tvSIDO= findViewById(R.id.tvSplash_SIDO);
        tvMoneyTransfer=findViewById(R.id.tvSplash_MoneyTransfer);
    }

    private void initHandler() {
        handler = new Handler();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer_count++;
                updateUI();

                /**
                 * After 3 Seconds Close Splash
                 * if loggedIn goto Main Activity
                 * else go to Login Acitivity
                 *
                 */
                if(timer_count>30){

                    this.cancel();


                }
            }
        }, 500, 100);  // every 10 Millisecond For the Animation

    }

    private void updateUI() {

        /**
         * No UI update is alowwed in onether Thread  so use runOnUiThread()
         */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(timer_count==5) {

                    //Animation Starts by Singing Si
                    animateReceiveMoney();
                    animateSendMoney();
                    PlayIntoSound(siID);

                    tvSiOut.setVisibility(View.VISIBLE);
                    tvSiIn.setVisibility(View.VISIBLE);
                    tvDoIn.setVisibility(View.VISIBLE);
                    tvDoOut.setVisibility(View.VISIBLE);
                    tvSIDO.setVisibility(View.VISIBLE);
                    tvSIDO.setText("SI");
                }

                else if(timer_count==20){
                    //Animation Ends by Singing Do
                    PlayIntoSound(doID);

                    //dISPLAY DO TEXT
                    tvSIDO.setText("SIDO");


                }

                else if(timer_count==25){

                    //DISPLAY mONEY TRANSFER TEXT
                    tvMoneyTransfer.setVisibility(View.VISIBLE);

                }

            }
        });
    }

    /**
     * This Animation that feels natural is done by using the 3 Libraries
     * AnimationPath , PathEvaluator , PathPoint
     *
     * It is published by a Google Developper on Youtube channel
     * https://www.youtube.com/channel/UCVHFbqXqoYvEWM1Ddxl0QDg
     */
    public  void animateSendMoney(){
        tvSendMoney.setVisibility(View.VISIBLE);

        //Getting the Location of a View on the Screen
        int[] siLoc = new int[2];
        tvSiOut.getLocationOnScreen(siLoc);

        int[] doLoc = new int[2];
        tvDoOut.getLocationOnScreen(doLoc);

        // Set up the path we're animating along
        AnimatorPath path = new AnimatorPath();
        path.moveTo(siLoc[0], siLoc[1]);    //Move the View to the desired location
        // path.lineTo(doLoc[0], doLoc[1]);
        int x = (doLoc[0] - siLoc[0]);
        int y = (doLoc[1] - siLoc[1]);

        /***
         * Drawing a Curve
         * 0,0 Means that remain on the point where you are
         * x/2 (The Middle X between the  2 points)
         *
         * x+10 because the First point has padding of 10 dp
         *x+10, siLoc[1], is the location of End Point
         */
        path.curveTo(0,0,x/2,300,x+10,doLoc[1]);
        // Set up the animation
        final ObjectAnimator anim = ObjectAnimator.ofObject(this, "SendLoc",
                new PathEvaluator(), path.getPoints().toArray());
        anim.setDuration(1500);
        anim.start();

    }
    public void setSendLoc(PathPoint newLoc) {
        tvSendMoney.setTranslationX(newLoc.mX);
        tvSendMoney.setTranslationY(newLoc.mY);
    }
    public  void animateReceiveMoney(){
        tvReceiveMoney.setVisibility(View.VISIBLE);
        int[] siLoc= new int[2];
        tvSiOut.getLocationOnScreen(siLoc);

        int[] doLoc= new int[2];
        tvDoOut.getLocationOnScreen(doLoc);

        // Set up the path we're animating along
        AnimatorPath path = new AnimatorPath();
        path.moveTo(doLoc[0],doLoc[1]);
        // path.lineTo(doLoc[0], doLoc[1]);
        int x=(doLoc[0]-siLoc[0]);
        int y=(doLoc[1]-siLoc[1]);
        path.curveTo(0,0,x/2,300,10,doLoc[1]);
        // Set up the animation
        final ObjectAnimator anim = ObjectAnimator.ofObject(this, "ReceiveLoc",
                new PathEvaluator(), path.getPoints().toArray());
        anim.setDuration(1500);
        anim.start();

    }

    private void initSound() {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(siID, soundPool.load(this, R.raw.si_mp3, 1));
        soundPoolMap.put(doID, soundPool.load(this, R.raw.do_mp3, 1));
    }

    private void PlayIntoSound(int soundID) {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float leftVolume = curVolume / maxVolume;
        float rightVolume = curVolume / maxVolume;
        int priority = 1;
        int no_loop = 0;
        float normal_playback_rate = 1f;
        soundPool.play(soundPoolMap.get(soundID), leftVolume, rightVolume, priority, no_loop, normal_playback_rate);

    }


    private void openLogin() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashActivity.this, LoginActivity.class));

                //Toast.makeText(getApplicationContext(),"Unable to Login",Toast.LENGTH_LONG).show();
                finish();
            }
        }, (30 - timer_count) * 100); // To make sure that The animation finishes


    }

    private void byPassLogin() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent bypassLoginIntent = new Intent(SplashActivity.this, MainActivity.class);
                // bypassLoginIntent.putExtra("username", CREDENTIALS.Auth.Username);
                startActivity(bypassLoginIntent);
                finish();
            }
        }, (30 - timer_count) * 100); // To make sure that The animation finishes

    }

    private void ReLogin() {
        checkLogin();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onLoginOK() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                byPassLogin();
            }
        });
    }

    @Override
    public void onLoginFailed(String reason) {
        openLogin();
    }
}
