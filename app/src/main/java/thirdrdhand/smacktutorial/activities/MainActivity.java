package thirdrdhand.smacktutorial.activities;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import thirdrdhand.smacktutorial.Model.ReceivedMessage;
import thirdrdhand.smacktutorial.R;
import thirdrdhand.smacktutorial.Storage.BackEndDB;
import thirdrdhand.smacktutorial.activities.transactions.TransactionsActivity;
import thirdrdhand.smacktutorial.constants.GLOBAL;
import thirdrdhand.smacktutorial.constants.KEYS;
import thirdrdhand.smacktutorial.constants.TYPES;
import thirdrdhand.smacktutorial.ussd_factory.USSDTools;
import thirdrdhand.smacktutorial.ussd_factory.USSD_CMD;

public class MainActivity extends Activity {

    private static final int PERIMISSION_TAG = 12345;
    private static final String TAG = "MAIN_ACTIVITY";
    TextView tvLog;
    Button btDetails;
    private BroadcastReceiver mBroadCastReceiver;
    private boolean USSDBUSY = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
    tvLog=findViewById(R.id.tvmain_activity_log);
        tvLog.append("\t\t\t Successfully Loggen in \n");
        tvLog.append("____________________" +
                "_________________________\n");
        btDetails = findViewById(R.id.btmain_activity_details);
        btDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TransactionsActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBroadCastReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                    String action=intent.getAction();
                switch (action){
                    case KEYS.BroadCast.BACKEND_CMD:
                        showLog("Received New Command :");
                        break;
                    case KEYS.BroadCast.RECEIVED_NEW_MESSAGE:
                        ReceivedMessage message = intent.getParcelableExtra(KEYS.EXTRA.XMPP.Message.RECEIVED_MSG);
                        processMessage(message);
                        break;
                }
            }
        };
        IntentFilter filter1 = new IntentFilter(KEYS.BroadCast.BACKEND_CMD);
        IntentFilter filter2 = new IntentFilter(KEYS.BroadCast.RECEIVED_NEW_MESSAGE);
        this.registerReceiver(mBroadCastReceiver,filter1);
        this.registerReceiver(mBroadCastReceiver,filter2);
    }

    private void LogMessage(Intent intent) {
        showLog("Received New Message :");
        ReceivedMessage message = intent.getParcelableExtra(KEYS.EXTRA.XMPP.Message.RECEIVED_MSG);
        showLog("Server -> " + message.FromDomain);
        showLog("From -> " + message.Username);
        showLog("TYPE -> " + message.mType.Value);
        showLog("ACTION -> " + message.Payload);
        showLog("STATUS -> " + message.mStatus.Value);
        showLog("____________");
    }

    private void showLog(String s) {
        tvLog.append(s+ " \n");
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mBroadCastReceiver);
    }

    private void processMessage(ReceivedMessage msg) {
        if (msg == null) {

            Log.e(TAG, "Empty Message");

        }


        if (msg.mType.equals(TYPES.MessageType.COMMAND)) {

            if (msg.Payload.equals(TYPES.PayloadType.DEPOSIT_MONEY.Value)) {
                //The Amount of Money
                //The Telephone number to send To
                String[] contentParts = msg.Content.split(GLOBAL.BODY_CONTENT_SPLIT);
                String phoneNumber = contentParts[0];
                String amount = contentParts[1];

                //Get The Ussd for Depositing Money in
                String Ussd = USSD_CMD.TIGO.TIGO_CASH.depositMoneyUssd(phoneNumber, amount);
                executeUssd(Ussd);
                //Change the Status of the Message in the Database
                msg.mStatus = TYPES.MessageStatus.PROCESSED;
                BackEndDB.getInstance(this).updateMsg(msg);


            } else if (msg.Payload.equals(TYPES.PayloadType.AIRTIME_BALANCE)) {

            } else if (msg.Payload.equals(TYPES.PayloadType.MONEY_BALANCE)) {

            } else if (msg.Payload.equals(TYPES.PayloadType.SEND_MONEY)) {

            } else if (msg.Payload.equals(TYPES.PayloadType.WITHDRAW_MONEY)) {

            } else if (msg.Payload.equals(TYPES.PayloadType.BUY_AIRTIME)) {

            } else if (msg.Payload.equals(TYPES.PayloadType.BUY_CASHPOWER)) {

            } else if (msg.Payload.equals(TYPES.PayloadType.PAY_WATER)) {

            } else if (msg.Payload.equals(TYPES.PayloadType.PAY_SCHOOL_FEES)) {

            } else if (msg.Payload.equals(TYPES.PayloadType.PAY_FUEL)) {

            }

        }
    }

    private void executeUssd(String ussd) {
        // Calling the Ussd
//        Thread thread= new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(!USSDBUSY){
//
//                }
//            }
//        });
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);

            Uri ussdData = USSDTools.packageForCall(ussd);

            intent.setData(ussdData);
            startActivity(intent);
            return;
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PERIMISSION_TAG);
        }

    }
}
