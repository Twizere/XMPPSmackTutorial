package thirdrdhand.smacktutorial.xmpp.interceptors;

import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.jxmpp.jid.Jid;

import thirdrdhand.smacktutorial.xmpp.XmppService;
import thirdrdhand.smacktutorial.xmpp.callbacks.CallbackTool;
import thirdrdhand.smacktutorial.xmpp.callbacks.MessageReceiveivedListener;

/**
 * Created by pacit on 2017/09/05.
 */

public class DeliveryReceiptInterceptor implements ReceiptReceivedListener {
    @Override
    public void onReceiptReceived(Jid fromJid, Jid toJid, String receiptId, Stanza receipt) {
        MessageReceiveivedListener messageReceiveivedListener = CallbackTool.getReceivedReceiptListener(XmppService.mConnection.msgIdReceiptBoxList, receiptId);
        long receiptDateTime = 0;
        messageReceiveivedListener.onReceive(toJid.getLocalpartOrNull().toString(), receiptId, receiptDateTime);
    }
}
