package thirdrdhand.smacktutorial.xmpp.listeners;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.sasl.provided.SASLDigestMD5Mechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.impl.JidCreate;

import java.io.IOException;
import java.net.InetAddress;

import thirdrdhand.smacktutorial.xmpp.XmppService;

/**
 * Created by pacit on 2017/08/14.
 */

public class XmppConnection implements ConnectionListener {
    private static final String TAG = "XmppConnection";



    private  final Context mApplicationContext;
    private String mUsername;
    private String mPassword;
    private  final String mServiceName ="localhost";
    private  final String mHost ="192.168.1.200";
    private  final int mPort = 5222;


    private XMPPTCPConnection mConnection;
    private ConnectionState mConnectionStatus;

    public static enum ConnectionState
    {
        CONNECTED ,AUTHENTICATED, CONNECTING ,DISCONNECTING ,DISCONNECTED;
    }
    public static enum LoggedInState
    {
        LOGGED_IN , LOGGED_OUT;
    }

    public XmppConnection(Context context){
        Log.d(TAG,"Connection Constructor called");
        mApplicationContext=context.getApplicationContext();

        mUsername= PreferenceManager.getDefaultSharedPreferences(mApplicationContext)
                .getString("username",null);
        mPassword=PreferenceManager.getDefaultSharedPreferences(mApplicationContext)
                .getString("password",null);

        if(mUsername==null ) mUsername="";
        if(mPassword==null) mPassword="";
    }

    public void connect() throws IOException, SmackException, XMPPException, InterruptedException {
        Log.d(TAG, "connectting to Server : "+ mServiceName);
        DomainBareJid domainBareJid = JidCreate.domainBareFrom(mServiceName) ;
        XMPPTCPConnectionConfiguration.Builder builder=
                XMPPTCPConnectionConfiguration.builder();
        builder.setServiceName(domainBareJid);
        builder.setHostAddress(InetAddress.getByName(mHost));
        builder.setUsernameAndPassword(mUsername, mPassword);
        builder.setResource("resource");
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        mConnection = new XMPPTCPConnection(builder.build());
        mConnection.addConnectionListener(this);
        mConnection.connect();
        SASLMechanism mechanism = new SASLDigestMD5Mechanism();
        SASLAuthentication.registerSASLMechanism(mechanism);
        SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
        SASLAuthentication.unBlacklistSASLMechanism("DIGEST-MD5");
        mConnection.login();
        //Set up the ui thread broadcast message receiver.
        //setupUiThreadBroadCastMessageReceiver();

        ReconnectionManager reconnectionManager= ReconnectionManager.getInstanceFor(mConnection);
        reconnectionManager.setEnabledPerDefault(true);
        reconnectionManager.enableAutomaticReconnection();




    }

    public void disconnect(){

        Log.d(TAG,"Disconnecting from Server "+ mServiceName);

        if(mConnection!=null){
            mConnection.disconnect();

        }

        mConnection=null;
    }
    @Override
    public void connected(XMPPConnection connection) {
            mConnectionStatus=ConnectionState.CONNECTED;
        Log.d(TAG,"Connected successfully");
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        mConnectionStatus=ConnectionState.AUTHENTICATED;
        Log.d(TAG,"AUTHENTICATED successfully");

        showMainActivity();
    }

    private void showMainActivity() {
        Intent i = new Intent(XmppService.UI_AUTHENTICATED);
        i.setPackage(mApplicationContext.getPackageName());
        mApplicationContext.sendBroadcast(i);
    }


    @Override
    public void connectionClosed() {
        mConnectionStatus=ConnectionState.DISCONNECTED;
        Log.d(TAG,"connection Closed");
        connectionFailure();
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        mConnectionStatus=ConnectionState.DISCONNECTED;
        Log.d(TAG,"ConnectionClosedOnError, error "+ e.toString());
        connectionFailure();
    }

    private void connectionFailure() {
        Intent i = new Intent(XmppService.CONNECTION_FAILURE);
        i.setPackage(mApplicationContext.getPackageName());
        mApplicationContext.sendBroadcast(i);
    }

    @Override
    public void reconnectingIn(int seconds) {
        mConnectionStatus=ConnectionState.CONNECTING;
        Log.d(TAG,"ReconnectingIn() " + seconds);
    }

    @Override
    public void reconnectionSuccessful() {
        mConnectionStatus=ConnectionState.CONNECTED;
        Log.d(TAG,"reconnectionSuccessful() " );
    }

    @Override
    public void reconnectionFailed(Exception e) {
        mConnectionStatus=ConnectionState.DISCONNECTED;
        Log.d(TAG,"reconnection Failed() " );
    }
}
