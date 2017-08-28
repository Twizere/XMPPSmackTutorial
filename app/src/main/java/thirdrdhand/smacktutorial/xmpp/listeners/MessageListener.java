package thirdrdhand.smacktutorial.xmpp.listeners;

import android.content.Intent;
import android.util.Log;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

import thirdrdhand.smacktutorial.Model.ReceivedMessage;
import thirdrdhand.smacktutorial.Storage.BackEndDB;
import thirdrdhand.smacktutorial.constants.CREDENTIALS;
import thirdrdhand.smacktutorial.constants.GLOBAL;
import thirdrdhand.smacktutorial.constants.KEYS;
import thirdrdhand.smacktutorial.parser.MessageParser;
import thirdrdhand.smacktutorial.xmpp.XmppService;

/**
 * Created by pacit on 2017/08/14.
 */

public class MessageListener implements IncomingChatMessageListener {

    private static final String TAG = "MESSAGE_LISTENER";

    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {

        /**
         * *****Steps****
         * *Receiving Messages
         **Detecting it's Type   [COMMAND],[REQUEST],[INFO]
         **Saving the Messages Received
         **Broadcast the Message Make a Notification
         */
        String parsedMsg = message.toXML().toString();
        Log.d(TAG, "Message received :" + parsedMsg);


        ReceivedMessage receivedMessage = MessageParser.ParseReceivedMessage(message);
        // Check if the Message is from My Server
        if (GLOBAL.FilterMyServer)
            if (!IsMine(receivedMessage)) {

                Log.e(TAG, "hacking attempt");
                //Vibrate and Siren
                return;
            }
        BackEndDB.getInstance(XmppService.getContext()).save(receivedMessage);

        Intent i = new Intent(KEYS.BroadCast.RECEIVED_NEW_MESSAGE);
        i.putExtra(KEYS.EXTRA.XMPP.Message.RECEIVED_MSG, receivedMessage);

        //Initiating The Task;
        //Decide the Ussd based on the Payload
        //Launch the Ussd
        //Record a Transaction <Phone number, Amount, >

        XmppService.mConnection.mApplicationContext.sendBroadcast(i);


    }


    private boolean IsMine(ReceivedMessage receivedMessage) {
        boolean isMine = true;
        if (!receivedMessage.FromDomain.equals(CREDENTIALS.Server.ServiceName))
            return false; //From another domain
        if (!receivedMessage.Username.equals(CREDENTIALS.Server.WebsiteUser))
            return false; //From another domain
        return isMine;
    }


}
