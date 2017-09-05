package thirdrdhand.smacktutorial.xmpp.callbacks;

/**
 * Created by pacit on 2017/09/05.
 */

public interface MessageReceiveivedListener {
    abstract void onReceive(String to, String reciptId, long timeReceived);

    abstract void onRead(String from, String messageId, long timeReceived);
}

