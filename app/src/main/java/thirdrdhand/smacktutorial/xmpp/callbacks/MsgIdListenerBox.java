package thirdrdhand.smacktutorial.xmpp.callbacks;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by pacit on 2017/09/05.
 */

public class MsgIdListenerBox extends BaseMessageListener {
    private MessageReceiveivedListener receiveivedListener;
    private SendMessageListener sendMessageLister;

    public MsgIdListenerBox(Message msg, MessageReceiveivedListener receiveivedListener) {
        this.message = msg;
        this.receiveivedListener = receiveivedListener;
    }

    public MsgIdListenerBox(Message msg, SendMessageListener sendMessageLister) {
        this.message = msg;
        this.sendMessageLister = sendMessageLister;
        this.Type = this.SEND_MESSAGE;
    }

    @Override
    SendMessageListener getSendMessageListener() {
        return sendMessageLister;
    }

    @Override
    MessageReceiveivedListener getReceiptMessageListener() {
        return null;
    }
}
