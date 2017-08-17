package thirdrdhand.smacktutorial.xmpp.listeners;

import android.content.Intent;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

import thirdrdhand.smacktutorial.xmpp.XmppService;
import thirdrdhand.smacktutorial.xmpp.constants.KEYS;

/**
 * Created by pacit on 2017/08/14.
 */

public class MessageListener implements IncomingChatMessageListener {

    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {

        Intent i = new Intent(KEYS.BroadCast.RECEIVED_NEW_MESSAGE);
        i.putExtra(KEYS.EXTRA.XMPP.Message.FROM,from.toString());
        i.putExtra(KEYS.EXTRA.XMPP.Message.BODY,message.getBody());
        XmppService.mConnection.mApplicationContext.sendBroadcast(i);
    }
}
