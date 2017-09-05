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
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.MessageWithBodiesFilter;
import org.jivesoftware.smack.filter.OrFilter;
import org.jivesoftware.smack.filter.StanzaExtensionFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.sasl.provided.SASLDigestMD5Mechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.xhtmlim.packet.XHTMLExtension;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import thirdrdhand.smacktutorial.constants.CREDENTIALS;
import thirdrdhand.smacktutorial.constants.KEYS;
import thirdrdhand.smacktutorial.constants.TYPES;
import thirdrdhand.smacktutorial.xmpp.XmppService;
import thirdrdhand.smacktutorial.xmpp.callbacks.MessageReceiveivedListener;
import thirdrdhand.smacktutorial.xmpp.callbacks.MsgIdListenerBox;
import thirdrdhand.smacktutorial.xmpp.callbacks.MsgIdReceiptBox;
import thirdrdhand.smacktutorial.xmpp.callbacks.SendMessageListener;
import thirdrdhand.smacktutorial.xmpp.interceptors.MessageInterceptor;
import thirdrdhand.smacktutorial.xmpp.tools.XmppTools;

import static thirdrdhand.smacktutorial.constants.CREDENTIALS.Auth.Password;
import static thirdrdhand.smacktutorial.constants.CREDENTIALS.Auth.Username;
import static thirdrdhand.smacktutorial.constants.CREDENTIALS.Server.getDomain;
import static thirdrdhand.smacktutorial.constants.CREDENTIALS.Server.getHost;

/**
 * Created by pacit on 2017/08/14.
 */

public class XmppConnection implements ConnectionListener {
    private static final String TAG = "XmppConnection";
    private static final StanzaFilter MESSAGE_FILTER = new AndFilter(
            MessageTypeFilter.NORMAL_OR_CHAT,
            new OrFilter(MessageWithBodiesFilter.INSTANCE), new StanzaExtensionFilter(XHTMLExtension.ELEMENT, XHTMLExtension.NAMESPACE),
            new OrFilter(MessageWithBodiesFilter.INSTANCE, new StanzaExtensionFilter(XHTMLExtension.ELEMENT, XHTMLExtension.NAMESPACE))
    );
    public   final Context mApplicationContext;
    public List<MsgIdReceiptBox> msgIdReceiptBoxList;
    public List<MsgIdListenerBox> msgIdListenerBoxList;
    public XMPPTCPConnection mConnection;
    public TYPES.ConnectionState mConnectionStatus;
    public TYPES.LogInState mLoginState;
    DeliveryReceiptManager deliveryReceiptManager;
    ChatManager mChatManager;
    public XmppConnection(Context context) {
        Log.w(TAG, "Connection Constructor called");
        mApplicationContext = context.getApplicationContext();
        msgIdListenerBoxList = new ArrayList<MsgIdListenerBox>();
        msgIdReceiptBoxList = new ArrayList<MsgIdReceiptBox>();
    }



    public void connect(String username, String password) throws IOException, SmackException, XMPPException, InterruptedException {
        Log.w(TAG, "connectting to Server : " + getDomain());

        CREDENTIALS.Server.generateServerConfig();

        if (!amIconnected()) {
            connectionFailure();
            return;
        }

        DomainBareJid domainBareJid = JidCreate.domainBareFrom(getDomain());
        XMPPTCPConnectionConfiguration.Builder builder=
                XMPPTCPConnectionConfiguration.builder();
        builder.setServiceName(domainBareJid);
        builder.setHostAddress(InetAddress.getByName(getHost()));
        builder.setUsernameAndPassword(username, password);
        builder.setResource("resource");
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        mConnection = new XMPPTCPConnection(builder.build());

        //
        mConnection.addConnectionListener(this);
        //
        mConnection.connect();

        SASLMechanism mechanism = new SASLDigestMD5Mechanism();
        SASLAuthentication.registerSASLMechanism(mechanism);
        SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
        SASLAuthentication.unBlacklistSASLMechanism("DIGEST-MD5");

        mConnection.login();


        //


        ReconnectionManager reconnectionManager= ReconnectionManager.getInstanceFor(mConnection);
        reconnectionManager.setEnabledPerDefault(true);
        reconnectionManager.enableAutomaticReconnection();


        mChatManager = ChatManager.getInstanceFor(mConnection);
        mChatManager.addIncomingListener(new MessageListener());
        mChatManager.addOutgoingListener(new MessageInterceptor());
        mConnection.addPacketSendingListener(new MessageInterceptor(), MESSAGE_FILTER);





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

        Log.w(TAG, "Disconnecting from Server " + getDomain());

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

    public void sendMessage(String to, String messageBody, SendMessageListener sendMessageLister) {
        Message msg = XmppTools.getMessage(to, messageBody);
        sendMessage(msg, sendMessageLister);
    }

    public void sendMessage(Message msg, SendMessageListener sendMessageLister) {
        try {
            EntityBareJid to = msg.getTo().asEntityBareJidIfPossible();
            Chat chat = mChatManager.chatWith(to);
            msgIdListenerBoxList.add(new MsgIdListenerBox(msg, sendMessageLister));
            String myUser = XmppTools.fullJid(CREDENTIALS.Auth.Username);
            if (to.equals(myUser)) {
                sendMessageLister.onSendFailed(msg, " Can't send to MySelf");
                ;
                return;
            }
            chat.send(msg);
            if (XmppService.getState() == TYPES.ConnectionState.AUTHENTICATED)
                sendMessageLister.onSendOK(msg);

        } catch (Exception ex) {

            if (sendMessageLister != null) sendMessageLister.onSendFailed(msg, ex.getMessage());
        }

    }

    public void sendMessageForResult(Message msg, SendMessageListener sendMessageLister, MessageReceiveivedListener messageReceivedListener) {


        DeliveryReceipt receipt = new DeliveryReceipt(msg.getStanzaId());
        msg.addExtension(receipt);
        msgIdReceiptBoxList.add(new MsgIdReceiptBox(msg, receipt, messageReceivedListener));
        sendMessage(msg, sendMessageLister);


    }

    public void sendMessageForResult(String to, String messageBody, SendMessageListener sendMessageLister, MessageReceiveivedListener receiptReceivedListener) {
        Message msg = XmppTools.getMessage(to, messageBody);
        sendMessageForResult(msg, sendMessageLister, receiptReceivedListener);

    }

}


