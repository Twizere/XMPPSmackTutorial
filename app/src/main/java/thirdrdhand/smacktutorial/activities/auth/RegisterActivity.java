package thirdrdhand.smacktutorial.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import thirdrdhand.smacktutorial.R;
import thirdrdhand.smacktutorial.xmpp.callbacks.UserRegistrationCallBack;
import thirdrdhand.smacktutorial.xmpp.listeners.XmppConnectionHolder;

import static thirdrdhand.smacktutorial.xmpp.XmppService.MakeToast;

public class RegisterActivity extends AppCompatActivity implements UserRegistrationCallBack {
    Handler handler = new Handler();
    Button btRegister;
    TextView tvLogin;
    EditText etUsername, etPassword, etConfirmPassword;
    ProgressBar pbRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();


    }

    private void initView() {

        btRegister = (Button) findViewById(R.id.btRegister_register);
        tvLogin = (TextView) findViewById(R.id.tvRegister_login);
        etUsername = (EditText) findViewById(R.id.etRegister_username);
        etPassword = (EditText) findViewById(R.id.etRegister_password);
        etConfirmPassword = (EditText) findViewById(R.id.etrRegister_confirmpassword);
        pbRegister = (ProgressBar) findViewById(R.id.pbRegister);


        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String confpassword = etConfirmPassword.getText().toString();
                if (!password.equals(confpassword)) {
                    etConfirmPassword.setError("Password do not Match");
                    return;
                } else if (password.equals("")) {
                    etPassword.setError("Password is Empty");
                    return;
                }
                //Registering
                pbRegister.setIndeterminate(true);
                pbRegister.setVisibility(View.VISIBLE);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AuthUser user = new AuthUser(username, password);
                        XmppConnectionHolder.getInstance()
                                .RegisterUser(user, RegisterActivity.this);
                    }
                });
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onRegisterOK() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MakeToast("Registered Successfully.....");
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                }, 1000);

            }
        });
    }

    @Override
    public void onRegisterFailed(final String reason) {
        // Toast.makeText(getApplicationContext(),"Register Failed, Please Try again",Toast.LENGTH_LONG).show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (reason.contains("conflict")) {
                    etUsername.setError("User already exist");
                } else if (reason.contains("Password must not be null")) {
                    etPassword.setError("Password can't be empty");
                } else
                    MakeToast("Network Problem, Try Again");
                pbRegister.setVisibility(View.INVISIBLE);
            }
        });
    }
}
