package thirdrdhand.smacktutorial.xmpp.listeners;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.sasl.provided.SASLDigestMD5Mechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.impl.JidCreate;

import java.io.IOException;
import java.net.InetAddress;

import thirdrdhand.smacktutorial.constants.CREDENTIALS;
import thirdrdhand.smacktutorial.constants.KEYS;
import thirdrdhand.smacktutorial.constants.TYPES;

import static thirdrdhand.smacktutorial.constants.CREDENTIALS.Auth.Password;
import static thirdrdhand.smacktutorial.constants.CREDENTIALS.Auth.Username;
import static thirdrdhand.smacktutorial.constants.CREDENTIALS.Server.ServiceName;

/**
 * Created by pacit on 2017/08/14.
 */

public class XmppConnection implements ConnectionListener {
    private static final String TAG = "XmppConnection";



    public   final Context mApplicationContext;
    public XMPPTCPConnection mConnection;
    public TYPES.ConnectionState mConnectionStatus;
    public TYPES.LogInState mLoginState;
    ChatManager mChatManager;


    public XmppConnection(Context context) {
        Log.w(TAG, "Connection Constructor called");
        mApplicationContext = context.getApplicationContext();
    }


    public void connect(String username, String password) throws IOException, SmackException, XMPPException, InterruptedException {
        Log.w(TAG, "connectting to Server : " + ServiceName);

        CREDENTIALS.Server.generateServerConfig();

        if (!amIconnected()) {
            connectionFailure();
            return;
        }

        DomainBareJid domainBareJid = JidCreate.domainBareFrom( ServiceName) ;
        XMPPTCPConnectionConfiguration.Builder builder=
                XMPPTCPConnectionConfiguration.builder();
        builder.setServiceName(domainBareJid);
        builder.setHostAddress(InetAddress.getByName( CREDENTIALS.Server.Host));
        builder.setUsernameAndPassword(username, password);
        builder.setResource("resource");
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        mConnection = new XMPPTCPConnection(builder.build());

        //
        mConnection.addConnectionListener(this);
        mConnection.connect();

        SASLMechanism mechanism = new SASLDigestMD5Mechanism();
        SASLAuthentication.registerSASLMechanism(mechanism);
        SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
        SASLAuthentication.unBlacklistSASLMechanism("DIGEST-MD5");

        mConnection.login();
        //
        mChatManager =ChatManager.getInstanceFor(mConnection);
        mChatManager.addIncomingListener(new MessageListener());


        //
        ReconnectionManager reconnectionManager= ReconnectionManager.getInstanceFor(mConnection);
        reconnectionManager.setEnabledPerDefault(true);
        reconnectionManager.enableAutomaticReconnection();


        //Adding
        Username = username;
        Password = password;
    }

    private boolean amIconnected() {
        ConnectivityManager conMgr = (ConnectivityManager) mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            // notify user you are online
            return true;

        } else if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            return false;
        } else {
            return false;
        }
    }

    public void disconnect(){

        Log.w(TAG, "Disconnecting from Server " + ServiceName);

        if(mConnection!=null){
            mConnection.disconnect();

        }

        mConnection=null;
    }
    @Override
    public void connected(XMPPConnection connection) {
            mConnectionStatus= TYPES.ConnectionState.CONNECTED;
        Log.w(TAG, "Connected successfully");

    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        mConnectionStatus= TYPES.ConnectionState.AUTHENTICATED;
        mLoginState = TYPES.LogInState.LOGGED_IN;
        Log.w(TAG, "AUTHENTICATED successfully");

        showMainActivity();
    }

    public void showMainActivity() {
        Intent i = new Intent(KEYS.BroadCast.UI_AUTHENTICATED);
        i.setPackage(mApplicationContext.getPackageName());
        mApplicationContext.sendBroadcast(i);
    }


    @Override
    public void connectionClosed() {
        mConnectionStatus= TYPES.ConnectionState.DISCONNECTED;
        Log.w(TAG, "connection Closed");
        connectionFailure();
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        mConnectionStatus= TYPES.ConnectionState.DISCONNECTED;
        Log.w(TAG, "ConnectionClosedOnError, error " + e.toString());
        connectionFailure();

    }

    public void connectionFailure() {
        Intent i = new Intent(KEYS.BroadCast.CONNECTION_FAILURE);
        i.setPackage(mApplicationContext.getPackageName());
        mApplicationContext.sendBroadcast(i);
    }

    @Override
    public void reconnectingIn(int seconds) {
        mConnectionStatus= TYPES.ConnectionState.CONNECTING;
        Log.w(TAG, "ReconnectingIn() " + seconds);
    }

    @Override
    public void reconnectionSuccessful() {
        mConnectionStatus= TYPES.ConnectionState.CONNECTED;
        Log.d(TAG,"reconnectionSuccessful() " );
    }

    @Override
    public void reconnectionFailed(Exception e) {
        mConnectionStatus= TYPES.ConnectionState.DISCONNECTED;
        Log.d(TAG,"reconnection Failed() " );
    }
}
