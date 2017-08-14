package thirdrdhand.smacktutorial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import thirdrdhand.smacktutorial.xmpp.XmppService;
import thirdrdhand.smacktutorial.xmpp.listeners.XmppConnection;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGIN_ACTIVITY" ;
    private BroadcastReceiver mBroadcastReceiver;
    private Context mContext;

    private EditText etUsername,etPassword;
    private Button btLogin;
    private CheckBox cbRememberMe;
    private ProgressBar pbLogin;
    private boolean rememberMe=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        mContext=getApplicationContext();
        checkLogin();

    }

    private void checkLogin() {
    if(XmppService.getState().equals(XmppConnection.ConnectionState.AUTHENTICATED)
            &&XmppService.getLoggedInState().equals(XmppConnection.LoggedInState.LOGGED_IN)){

        Continue();
    }else{
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
       rememberMe= prefs.getBoolean("remember_login",false);
        if(rememberMe){

            Login(prefs.getString("username",""),prefs.getString("password",""));
        }

    }
    }

    private void initView() {
    etUsername= (EditText) findViewById(R.id.etlogin_username);
        etPassword= (EditText) findViewById(R.id.etlogin_password);
        btLogin= (Button) findViewById(R.id.btlogin_login);
        cbRememberMe=(CheckBox) findViewById(R.id.cblogin_remember) ;
        pbLogin= (ProgressBar) findViewById(R.id.pblogin); pbLogin.setMax(100);
        cbRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                rememberMe=b;
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username= etUsername.getText().toString();
                String password=etPassword.getText().toString();
                Login(username,password);
            }
        });
    }

    private void Login(String username,String password) {
        Log.d(TAG,"Login() Called");
        showProgress(30);
       // if(rememberMe)
            saveCredentialsAndLogin(username,password);
    }


    private void saveCredentialsAndLogin(String username, String password)
    {
        Log.d(TAG,"saveCredentialsAndLogin() called.");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit()
                .putString("username", username)
                .putString("password", password)
                .putBoolean("logged_in",true)
                .putBoolean("remember_login",rememberMe)
                .commit();

        //Start the service
        Intent i1 = new Intent(this,XmppService.class);
        startService(i1);
        showProgress(50);

    }
    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                switch (action)
                {
                    case XmppService.UI_AUTHENTICATED:
                        Log.d(TAG,"Got a broadcast to show the main app window");
                        //Show the main app window
                        Continue();
                        break;
                    case XmppService.CONNECTION_FAILURE:
                        Toast.makeText(getApplicationContext(),"Unable to Login",Toast.LENGTH_LONG).show();
                        showProgress(0);
                        break;
                }

            }
        };

        IntentFilter filter = new IntentFilter(XmppService.UI_AUTHENTICATED);
        this.registerReceiver(mBroadcastReceiver, filter);
    }

    private void Continue() {
        showProgress(100);

        Intent i2 = new Intent(mContext,MainActivity.class);
        startActivity(i2);
        finish();
    }

    private void showProgress(int  progress) {
        pbLogin.setProgress(progress);

    }
}
