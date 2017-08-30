package thirdrdhand.smacktutorial.ussd_factory;

import android.content.Context;
import android.util.Log;

import java.util.Date;

/**
 * Created by pacit on 2017/08/30.
 */
public class CallReceiver extends PhoneCallReceiver {

    private static final String TAG = "CallReceiver";

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {

        Log.d(TAG, "incoming " + number);
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        Log.d(TAG, "outgoing  " + number);
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {

        Log.d(TAG, "ended incoming " + number);
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {

        Log.d(TAG, "ended outgoing " + number);
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
    }

}