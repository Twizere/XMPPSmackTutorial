package thirdrdhand.smacktutorial.xmpp.callbacks;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by pacit on 2017/09/05.
 */

public interface SendMessageListener {
    abstract void onSendOK(Message msg);

    abstract void onSendFailed(Message msg, String reason);
}
