package thirdrdhand.smacktutorial.parser;

import android.util.Log;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.delay.packet.DelayInformation;

import java.util.Date;

import thirdrdhand.smacktutorial.Model.ReceivedMessage;
import thirdrdhand.smacktutorial.constants.GLOBAL;
import thirdrdhand.smacktutorial.constants.TYPES;

import static thirdrdhand.smacktutorial.constants.CREDENTIALS.Modes.ParsingMode;

/**
 * Created by pacit on 2017/08/24.
 */

public class MessageParser {

    private static final String TAG = "MESSAGE-PARSER";

    public static ReceivedMessage ParseReceivedMessage(Message xmppMessage) {
        ReceivedMessage message = new ReceivedMessage();
        //Working Mode
        // {Extension Mode or Message Mode?}
        if (ParsingMode == TYPES.ParsingMode.WITH_EXTENSION) {

            // Extension mode can be done Later
        } else {
            message.msgId = xmppMessage.getStanzaId();
            message.XmppType = xmppMessage.getType() == Message.Type.chat ? 1 : 0;
            message.FromDomain = xmppMessage.getFrom().getDomain().toString();
            message.Username = xmppMessage.getFrom().getLocalpartOrNull().toString();   // Username which sent it From my Server or Not
            String body = xmppMessage.getBody();
            String[] bodyParts = null;
            try {
                if (body.contains(GLOBAL.XMPP_BODY_SPLIT))
                    bodyParts = body.split(GLOBAL.XMPP_BODY_SPLIT);
                else return null;

            } catch (Exception e) {

                String msg = e.getMessage();
                Log.e(TAG, msg);
            }
            if (bodyParts.length < 2) return null;

            //Detecting Type
            if (bodyParts[0].equals(TYPES.MessageType.COMMAND.Value))
                message.mType = TYPES.MessageType.COMMAND;
            if (bodyParts[0].equals(TYPES.MessageType.REQUEST.Value))
                message.mType = TYPES.MessageType.REQUEST;
            if (bodyParts[0].equals(TYPES.MessageType.INFO.Value))
                message.mType = TYPES.MessageType.INFO;
            //
            message.Payload = bodyParts[1];
            message.Content = bodyParts[2];
            message.TimeStamp = getSentTime(xmppMessage);
            message.mStatus = TYPES.MessageStatus.RECEIVED;
            message.mError = TYPES.MessageError.NONE;
            message.isDelete = 0;

        }

        return message;
    }

    private static long getSentTime(Message xmppMessage) {
        DelayInformation info = (DelayInformation) xmppMessage.getExtension("x",
                "jabber:x:delay");
        if (info == null) return new Date().getTime();
        else
            return info.getStamp().getTime();

    }

}
