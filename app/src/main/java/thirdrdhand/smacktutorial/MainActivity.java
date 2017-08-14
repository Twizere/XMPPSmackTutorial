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
        tvLog.append("____________________________" +
                "_________________________________\n");
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
                        tvLog.append("Received Command \n");
                        break;
                }
            }
        };
        IntentFilter filter = new IntentFilter(XmppService.BACKEND_CMD);
        this.registerReceiver(mBroadCastReceiver,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mBroadCastReceiver);
    }
}
