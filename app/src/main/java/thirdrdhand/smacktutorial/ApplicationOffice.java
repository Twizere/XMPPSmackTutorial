package thirdrdhand.smacktutorial;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import thirdrdhand.smacktutorial.constants.KEYS;

/**
 * Created by pacit on 2017/08/17.
 */

public class ApplicationOffice {


    private static final String TAG ="APPLICATION_OFFICE" ;


    public static void saveCredentials(Context context, String username, String password)
{
    Log.w(TAG, "Saving Credentials ");
    SharedPreferences prefs = getPrefs(context);
    SharedPreferences.Editor editor = prefs.edit();
    editor.clear();
    editor.putString(KEYS.PREF.Auth.USERNAME, username);
    editor.putString(KEYS.PREF.Auth.PASSWORD, password);
    editor.putBoolean(KEYS.PREF.Auth.AUTO_LOGIN, true);
    editor.commit();

    SharedPreferences savedPrefs = getPrefs(context);
    boolean savedRememberMe = savedPrefs.getBoolean(KEYS.PREF.Auth.AUTO_LOGIN, false);
    String savedUsername = savedPrefs.getString(KEYS.PREF.Auth.USERNAME, null);
    String savedPassword = savedPrefs.getString(KEYS.PREF.Auth.PASSWORD, null);
    Log.w(TAG, savedUsername + "|||" + savedPassword + "|||" + savedRememberMe);
}

    public static SharedPreferences getPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                KEYS.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return prefs;
    }
}
