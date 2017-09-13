package thirdrdhand.smacktutorial.SMS_Factory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import static android.provider.Telephony.Sms.Intents.getMessagesFromIntent;
import static thirdrdhand.smacktutorial.xmpp.XmppService.MakeToast;

/**
 * Created by pacit on 2017/09/01.
 */

public class SmsMonitor extends BroadcastReceiver {

    private static final String TAG = "SMS_MONITOR";
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {
        SmsMessage[] msgs = null;
        SmsMessage smsMessage;
        final Bundle bundle = intent.getExtras();
        if (Build.VERSION.SDK_INT >= 19) { //KITKAT
            msgs = getMessagesFromIntent(intent);
            smsMessage = msgs[0];
        } else {
            Object pdus[] = (Object[]) bundle.get("pdus");
            smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
        }

        String format = intent.getStringExtra("format");


        SmsMessage sms = smsMessage;
        Log.v(TAG, "handleSmsReceived --" +
                " messageUri: " +
                ", address: " + sms.getOriginatingAddress() +
                ", body: " + sms.getMessageBody());


        String message = sms.getMessageBody();
        MakeToast(message);
    }
}