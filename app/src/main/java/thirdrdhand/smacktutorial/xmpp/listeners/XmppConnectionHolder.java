package thirdrdhand.smacktutorial.xmpp.listeners;

import android.content.Context;
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
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.xhtmlim.packet.XHTMLExtension;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import thirdrdhand.smacktutorial.activities.auth.AuthUser;
import thirdrdhand.smacktutorial.constants.CREDENTIALS;
import thirdrdhand.smacktutorial.constants.KEYS;
import thirdrdhand.smacktutorial.constants.TYPES;
import thirdrdhand.smacktutorial.xmpp.XmppService;
import thirdrdhand.smacktutorial.xmpp.callbacks.MessageReceiveivedListener;
import thirdrdhand.smacktutorial.xmpp.callbacks.MsgIdListenerBox;
import thirdrdhand.smacktutorial.xmpp.callbacks.MsgIdReceiptBox;
import thirdrdhand.smacktutorial.xmpp.callbacks.SendMessageListener;
import thirdrdhand.smacktutorial.xmpp.callbacks.UserLoginCallBack;
import thirdrdhand.smacktutorial.xmpp.callbacks.UserRegistrationCallBack;
import thirdrdhand.smacktutorial.xmpp.interceptors.MessageInterceptor;
import thirdrdhand.smacktutorial.xmpp.tools.XmppTools;

import static thirdrdhand.smacktutorial.constants.CREDENTIALS.Auth.Password;
import static thirdrdhand.smacktutorial.constants.CREDENTIALS.Auth.Username;
import static thirdrdhand.smacktutorial.constants.CREDENTIALS.Server.getDomain;
import static thirdrdhand.smacktutorial.constants.CREDENTIALS.Server.getHost;

/**
 * Created by pacit on 2017/08/14.
 */

public class XmppConnectionHolder implements ConnectionListener {
    private static final String TAG = "XmppConnectionHolder";
    private static final StanzaFilter MESSAGE_FILTER = new AndFilter(
            MessageTypeFilter.NORMAL_OR_CHAT,
            new OrFilter(MessageWithBodiesFilter.INSTANCE), new StanzaExtensionFilter(XHTMLExtension.ELEMENT, XHTMLExtension.NAMESPACE),
            new OrFilter(MessageWithBodiesFilter.INSTANCE, new StanzaExtensionFilter(XHTMLExtension.ELEMENT, XHTMLExtension.NAMESPACE))
    );
    private static XmppConnectionHolder instance;
    public List<MsgIdReceiptBox> msgIdReceiptBoxList;
    public List<MsgIdListenerBox> msgIdListenerBoxList;
    public XMPPTCPConnection mConnection;
    public TYPES.ConnectionState mConnectionStatus;
    public TYPES.LogInState mLoginState;
    DeliveryReceiptManager deliveryReceiptManager;
    ChatManager mChatManager;
    private AccountManager mAccountManager;

    public XmppConnectionHolder() {
        Log.w(TAG, "Connection Constructor called");
        msgIdListenerBoxList = new ArrayList<MsgIdListenerBox>();
        msgIdReceiptBoxList = new ArrayList<MsgIdReceiptBox>();
        instance = this;

    }

    public static XmppConnectionHolder getInstance() {
        if (instance == null)
            new XmppConnectionHolder();
        return instance;
    }

    public AccountManager getAccountManager() throws InterruptedException, XMPPException, SmackException, IOException {
        if (mAccountManager == null)
            mAccountManager = AccountManager.getInstance(mConnection);
        return mAccountManager;
    }

    public XMPPTCPConnection connect() throws IOException, InterruptedException, XMPPException, SmackException {


        mConnection = connect(Username == null ? "test" : Username, Password == null ? "test" : Password);

        return mConnection;
    }

    private XMPPTCPConnectionConfiguration.Builder getTestBuilder(String username, String password) throws XmppStringprepException, UnknownHostException {
        DomainBareJid domainBareJid = JidCreate.domainBareFrom(getDomain());
        XMPPTCPConnectionConfiguration.Builder builder=
                XMPPTCPConnectionConfiguration.builder();
        builder.setServiceName(domainBareJid);
        builder.setHostAddress(InetAddress.getByName(getHost()));
        builder.setUsernameAndPassword(username, password);
        builder.setResource("resource");
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        return builder;
    }

    public XMPPTCPConnection connect(String username, String password) throws IOException, SmackException, XMPPException, InterruptedException {
        if (!amIconnected()) {
            connectionFailure();
            return null;
        }
        if (mConnection != null) {
            mConnection.disconnect();
            mConnection.instantShutdown();
            mConnection = null;
        }

        Log.w(TAG, "connectting to Server : " + getDomain());
        CREDENTIALS.Server.generateServerConfig();
        mConnection = new XMPPTCPConnection(getTestBuilder(username, password).build());
        mConnection.addConnectionListener(this);

        mConnection.connect();
        ReconnectionManager reconnectionManager= ReconnectionManager.getInstanceFor(mConnection);
        reconnectionManager.setEnabledPerDefault(true);
        reconnectionManager.enableAutomaticReconnection();
        mAccountManager = AccountManager.getInstance(mConnection);
        return mConnection;

    }

    public void Login(final String username, final String password, final UserLoginCallBack callback) {

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Checking The Connection
                    if (mLoginState == TYPES.LogInState.LOGGED_IN) {
                        if (callback != null)
                            callback.onLoginFailed("Already LoggenIn Logout First");
                        return;
                    }

                    mConnection = getInstance().connect(username, password);

                    if (mConnectionStatus == TYPES.ConnectionState.CONNECTED) {

                        SASLMechanism mechanism = new SASLDigestMD5Mechanism();
                        SASLAuthentication.registerSASLMechanism(mechanism);
                        SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
                        SASLAuthentication.unBlacklistSASLMechanism("DIGEST-MD5");

                        mConnection.login();

                        mChatManager = ChatManager.getInstanceFor(mConnection);
                        mChatManager.addIncomingListener(new MessageListener());
                        mChatManager.addOutgoingListener(new MessageInterceptor());
                        mConnection.addPacketSendingListener(new MessageInterceptor(), MESSAGE_FILTER);
                        //Adding
                        Username = username;
                        Password = password;
                        if (callback != null) callback.onLoginOK();
                    }

                } catch (Exception e) {

                    if (callback != null) callback.onLoginFailed(e.getMessage());
                }
            }
        });
        th.start();
    }

    private boolean amIconnected() {
        ConnectivityManager conMgr = (ConnectivityManager) XmppService.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

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

        XmppService.showMainActivity();
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
        XmppService.broadCastXmppInfo(KEYS.BroadCast.CONNECTION_FAILURE);

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

    public void RegisterUser(final AuthUser user, final UserRegistrationCallBack userRegistrationCallBack) {
        Thread reg = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    connect();
                    getAccountManager();
                    mAccountManager.sensitiveOperationOverInsecureConnection(true);
                    mAccountManager.createAccount(Localpart.from(user.Username), user.Password);
                    userRegistrationCallBack.onRegisterOK();
                } catch (Exception e) {
                    e.printStackTrace();
                    userRegistrationCallBack.onRegisterFailed(e.getMessage());

                }

            }
        });
        reg.start();
    }
}


