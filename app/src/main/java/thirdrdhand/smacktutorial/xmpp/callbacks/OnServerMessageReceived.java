package thirdrdhand.smacktutorial.xmpp.callbacks;

import thirdrdhand.smacktutorial.Model.ReceivedMessage;
import thirdrdhand.smacktutorial.constants.TYPES;

/**
 * Created by pacit on 2017/09/06.
 */


public interface OnServerMessageReceived {
    abstract void onCommandReceive(TYPES.PayloadType commandType, String messageBody, String bodySeparator);

    abstract void onRequestReceive(TYPES.PayloadType requestType, ReceivedMessage message);

    abstract void onReceive(ReceivedMessage message);
}

