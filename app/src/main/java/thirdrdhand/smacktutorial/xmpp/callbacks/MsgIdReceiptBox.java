package thirdrdhand.smacktutorial.xmpp.callbacks;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;

/**
 * Created by pacit on 2017/09/05.
 */

public class MsgIdReceiptBox extends BaseMessageListener {
    private DeliveryReceipt deliveryReceipt;
    private MessageReceiveivedListener receiveivedListener;


    public MsgIdReceiptBox(Message msg, DeliveryReceipt deliveryReceipt, MessageReceiveivedListener receiveivedListener) {
        this.message = msg;
        this.deliveryReceipt = deliveryReceipt;
        this.receiveivedListener = receiveivedListener;
        this.Type = this.RECEIT_RECEIVED;  //this is used to Identify this Box in the List
    }


    @Override
    SendMessageListener getSendMessageListener() {
        return null;
    }

    @Override
    MessageReceiveivedListener getReceiptMessageListener() {
        return receiveivedListener;
    }

    public String getReceiptId() {
        return deliveryReceipt.getId();
    }
}
