package thirdrdhand.smacktutorial.activities.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import thirdrdhand.smacktutorial.ApplicationOffice;
import thirdrdhand.smacktutorial.R;
import thirdrdhand.smacktutorial.activities.MainActivity;
import thirdrdhand.smacktutorial.xmpp.callbacks.UserLoginCallBack;
import thirdrdhand.smacktutorial.xmpp.listeners.XmppConnectionHolder;


public class LoginActivity extends AppCompatActivity implements UserLoginCallBack {

    private static final String TAG = "LOGIN_ACTIVITY" ;
    Handler handler;
    private Context mContext;
    private EditText etUsername,etPassword;
    private Button btLogin;
    private CheckBox cbRememberMe;
    private ProgressBar pbLogin;
    private boolean rememberMe=false;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        mContext=getApplicationContext();
        handler = new Handler();

    }



    private void initView() {
        tvRegister = (TextView) findViewById(R.id.tvLogin_register);
        etUsername = (EditText) findViewById(R.id.etlogin_username);
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
                showProgress(true);
                XmppConnectionHolder.getInstance().Login(username, password, LoginActivity.this);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });
    }




    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showProgress(boolean progress) {
        pbLogin.setVisibility(progress ? View.VISIBLE : View.INVISIBLE);

    }

    @Override
    public void onLoginOK() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgress(false);
                if (rememberMe)
                    ApplicationOffice.saveCredentials(getApplicationContext(),
                            etUsername.getText().toString(),
                            etPassword.getText().toString());
                Intent i2 = new Intent(mContext, MainActivity.class);
                startActivity(i2);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void onLoginFailed(final String reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgress(false);
                etUsername.setError(reason);
                etPassword.setError("Invalid Username or Password");
            }
        });
    }
}
