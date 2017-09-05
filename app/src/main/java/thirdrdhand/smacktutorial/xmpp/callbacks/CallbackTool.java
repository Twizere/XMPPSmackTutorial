package thirdrdhand.smacktutorial.xmpp.callbacks;

import org.jivesoftware.smack.packet.Message;

import java.util.List;

/**
 * Created by pacit on 2017/09/05.
 */

public class CallbackTool {
    public static SendMessageListener getSendListener(List<MsgIdListenerBox> msgIdListenerBoxList, Message message) {
        for (BaseMessageListener messageListener : msgIdListenerBoxList) {
            if (messageListener.Type.equals(messageListener.SEND_MESSAGE))
                if (messageListener.message.getStanzaId().equals(message.getStanzaId())) {
                    return messageListener.getSendMessageListener();

                }

        }


        return null;
    }

    public static MessageReceiveivedListener getReceivedReceiptListener(List<MsgIdReceiptBox> msgIdListenerBoxList, String receiptId) {
        for (BaseMessageListener messageListener : msgIdListenerBoxList) {
            if (messageListener.Type.equals(messageListener.RECEIT_RECEIVED)) {
                MsgIdReceiptBox receiptBox = (MsgIdReceiptBox) messageListener;
                if (receiptBox.getReceiptId().equals(receiptId)) {
                    return messageListener.getReceiptMessageListener();
                }
            }


        }

        return null;
    }
}
