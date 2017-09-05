package thirdrdhand.smacktutorial.xmpp.interceptors;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jxmpp.jid.EntityBareJid;

import thirdrdhand.smacktutorial.xmpp.XmppService;
import thirdrdhand.smacktutorial.xmpp.callbacks.CallbackTool;
import thirdrdhand.smacktutorial.xmpp.callbacks.SendMessageListener;

/**
 * Created by pacit on 2017/09/05.
 */

public class MessageInterceptor implements OutgoingChatMessageListener, StanzaListener {


    @Override
    public void newOutgoingMessage(EntityBareJid to, Message message, Chat chat) {

        SendMessageListener sendListener = CallbackTool.getSendListener(XmppService.mConnection.msgIdListenerBoxList, message);
        sendListener.onSendOK(message);
    }

    @Override
    public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException {
        SendMessageListener sendListener = CallbackTool.getSendListener(XmppService.mConnection.msgIdListenerBoxList, (Message) packet);
        sendListener.onSendOK((Message) packet);
    }
}
