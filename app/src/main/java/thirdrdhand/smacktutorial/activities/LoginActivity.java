package thirdrdhand.smacktutorial.activities;

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

import thirdrdhand.smacktutorial.ApplicationOffice;
import thirdrdhand.smacktutorial.R;
import thirdrdhand.smacktutorial.xmpp.XmppService;
import thirdrdhand.smacktutorial.xmpp.constants.CREDENTIALS;
import thirdrdhand.smacktutorial.xmpp.constants.KEYS;



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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
       rememberMe= prefs.getBoolean(KEYS.PREF.Auth.AUTO_LOGIN,false);
        String username =prefs.getString(KEYS.PREF.Auth.USERNAME,null);
        String password=prefs.getString(KEYS.PREF.Auth.PASSWORD,null);

        if(username==null || password ==null){

            //startActivity To Login
        }else {
            if (!username.equals("") && !password.equals("")) {
                if (rememberMe)
                    Login(username, password);

            }
        }
    }

    private void initView() {
    etUsername= (EditText) findViewById(R.id.etlogin_username);
        etPassword= (EditText) findViewById(R.id.etlogin_password);
        btLogin= (Button) findViewById(R.id.btlogin_login);
        cbRememberMe=(CheckBox) findViewById(R.id.cblogin_remember) ;
        pbLogin= (ProgressBar) findViewById(R.id.pblogin);
        pbLogin.setIndeterminate(true);
        showProgress(false);
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
                if(username!=null && password!=null)
                    Login(username,password);
            }
        });
    }

    private void Login(String username,String password) {
        Log.d(TAG,"Login() Called");
        showProgress(true);

        //Start the service
        Intent i1 = new Intent(this,XmppService.class);
        startService(i1);
    }



    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                switch (action)
                {
                    case KEYS.BroadCast.UI_AUTHENTICATED:
                        Log.d(TAG,"Got a broadcast to show the main app window");
                        if(rememberMe)
                            new ApplicationOffice(getApplicationContext()).saveCredentials(CREDENTIALS.Auth.Username,CREDENTIALS.Auth.Password);
                        //Show the main app window
                        Continue();
                        break;
                    case KEYS.BroadCast.CONNECTION_FAILURE:
                        Toast.makeText(getApplicationContext(),"Unable to Login",Toast.LENGTH_LONG).show();
                        showProgress(true);
                        break;
                }

            }
        };

        IntentFilter filter1 = new IntentFilter(KEYS.BroadCast.UI_AUTHENTICATED);
        IntentFilter filter2 = new IntentFilter(KEYS.BroadCast.CONNECTION_FAILURE);
        this.registerReceiver(mBroadcastReceiver, filter1);
        this.registerReceiver(mBroadcastReceiver, filter2);
    }

    private void Continue() {
        showProgress(false);

        Intent i2 = new Intent(mContext,MainActivity.class);
        startActivity(i2);
        finish();
    }

    private void showProgress(boolean  progress) {
        pbLogin.setVisibility(progress?View.VISIBLE:View.INVISIBLE);

    }
}
