package thirdrdhand.smacktutorial.xmpp.callbacks;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by pacit on 2017/09/05.
 */

abstract class BaseMessageListener {
    public static final String RECEIT_RECEIVED = "RECEIT_RECEIVED_LISTENER";
    public static final String SEND_MESSAGE = "SEND_MESSAGE_LISTENER";

    public Message message;
    public String Type;  //this is used to Identify this Box in the List

    abstract SendMessageListener getSendMessageListener();

    abstract MessageReceiveivedListener getReceiptMessageListener();
}
