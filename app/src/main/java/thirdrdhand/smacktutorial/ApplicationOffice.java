package thirdrdhand.smacktutorial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import thirdrdhand.smacktutorial.xmpp.constants.KEYS;

/**
 * Created by pacit on 2017/08/17.
 */

public class ApplicationOffice {


    private static final String TAG ="APPLICATION_OFFICE" ;

    private Context context;

    public ApplicationOffice(Context context) {
        this.context = context;
    }



    public  void saveCredentials(String username, String password)
{
    Log.d(TAG,"saveCredentials() called.");
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit()
            .putString(KEYS.PREF.Auth.USERNAME, username)
            .putString(KEYS.PREF.Auth.PASSWORD, password)
            .putBoolean(KEYS.PREF.Auth.AUTO_LOGIN,  true)
            .commit();


}
}
