package thirdrdhand.smacktutorial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import thirdrdhand.smacktutorial.xmpp.XmppService;

public class MainActivity extends Activity {

    TextView tvLog;
    private BroadcastReceiver mBroadCastReceiver;



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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBroadCastReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                    String action=intent.getAction();
                switch (action){
                    case XmppService.BACKEND_CMD:
                        showLog("Received New Command :");
                        break;
                    case XmppService.RECEIVED_NEW_MSG:
                        LogMessage(intent);

                        break;
                }
            }
        };
        IntentFilter filter1 = new IntentFilter(XmppService.BACKEND_CMD);
        IntentFilter filter2 = new IntentFilter(XmppService.RECEIVED_NEW_MSG);
        this.registerReceiver(mBroadCastReceiver,filter1);
        this.registerReceiver(mBroadCastReceiver,filter2);
    }

    private void LogMessage(Intent intent) {
        showLog("Received New Message :");
        showLog("From -> "+intent.getStringExtra("from"));
        showLog("Body -> "+intent.getStringExtra("body"));
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
}
