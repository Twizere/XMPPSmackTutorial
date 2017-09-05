package thirdrdhand.smacktutorial.xmpp.tools;


import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.Calendar;

import thirdrdhand.smacktutorial.constants.CREDENTIALS;

/**
 * Created by pacit on 2017/09/05.
 */

public class XmppTools {
    public static Jid ToJid(String name) {
        try {
            return JidCreate.bareFrom(name + "@" + CREDENTIALS.Server.getDomain());
        } catch (XmppStringprepException e) {
            return null;
        }
    }

    public static String fullJid(String name) {

        return name + "@" + CREDENTIALS.Server.getDomain();
    }

    public static Message getMessage(String to, String messageBody) {
        Message msg = new Message();
        String id = to + String.valueOf(Calendar.getInstance().getTimeInMillis());
        msg.setType(Message.Type.chat);
        msg.setStanzaId(id);
        msg.setTo(ToJid(to));
        msg.setBody(messageBody);
        return msg;
    }
}
