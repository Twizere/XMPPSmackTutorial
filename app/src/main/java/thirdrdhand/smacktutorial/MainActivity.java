package thirdrdhand.smacktutorial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import thirdrdhand.smacktutorial.xmpp.XmppService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Login();

    }

    private void Login() {
        Log.d(TAG,"Login() Called");
        String password="123456";
        String username="pacifique";
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
                .commit();

        //Start the service
        Intent i1 = new Intent(this,XmppService.class);
        startService(i1);

    }
}
